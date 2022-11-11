package com.bnmotor.icv.tsp.cpsp.engine.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.config.feign.CPSPApiConfigFeignService;
import com.bnmotor.icv.tsp.cpsp.config.feign.CPSPProviderServiceFeignService;
import com.bnmotor.icv.tsp.cpsp.config.feign.CPSPQosFeignService;
import com.bnmotor.icv.tsp.cpsp.config.model.request.CpspLogAndApiQosDto;
import com.bnmotor.icv.tsp.cpsp.config.model.response.CpspServiceApiConfigVo;
import com.bnmotor.icv.tsp.cpsp.config.model.response.CpspServiceProviderVo;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.factory.StrategyFactory;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.constant.Constants;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.context.RulesStrategyContext;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.biz.BaseDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.ChannelDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.InstChannelApiDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.strategy.RulesStrategy;
import com.bnmotor.icv.tsp.cpsp.engine.utils.MongoUtil;
import com.bnmotor.icv.tsp.cpsp.engine.utils.RedisUtil;
import com.bnmotor.icv.tsp.cpsp.engine.utils.ThreadPoolUtil;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.bnmotor.icv.tsp.cpsp.engine.support.constant.Constants.*;

/**
 * @ClassName: CPSPProxy
 * @Description: CPSP服务代理
 * @author liuyiwei1
 * @date 2020-08-18 16:26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Component
public class CPSPProxy {

    @Autowired
    private CPSPApiConfigFeignService apiConfigFeignService;

    @Autowired
    private CPSPProviderServiceFeignService providerServiceFeignService;

    @Autowired
    private CPSPQosFeignService qosFeignService;

    @Autowired
    private RulesStrategyContext rulesStrategyContext;

    @Autowired
    private StrategyFactory strategyFactory;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MongoUtil mongoUtil;

    @Autowired
    private ThreadPoolUtil threadPoolUtil;

    @Value("${retry.initial_sleep_time:200}")
    private long INITIAL_SLEEP_TIME;

    @Value("${retry.increment_time:100}")
    private long INCREMENT_TIME;

    public <T extends CPSPOutput> T call(CPSPInput<T> input) {
        //0.获取接口配置信息
        CpspServiceApiConfigVo apiConfig = currentAPIConfig(input.getProjectId(), input.getServiceCode(), input.getCurrentAPI());

        //1.根据接口配置策略, 获取本地缓存或持久化数据
        if (apiConfig.getCacheOrder() == CACHE_BEFORE || apiConfig.getPersistentFlag() == BIN_YES) {
            T result = accessLocal(apiConfig, input);
            if (result != null) {
                return result;
            }
        }

        //2.调用提供商接口，请求实时数据; 缓存本地数据
        T result = process(apiConfig, input);

        return result;
    }


    /**
     * 获取本地数据（接口缓存或持久化的数据）
     * @param apiConfig
     * @param <T>
     * @return
     */
    private <T extends CPSPOutput> T accessLocal(CpspServiceApiConfigVo apiConfig, CPSPInput input) {
        T result = null;
        if (apiConfig.getCacheOrder() == CACHE_BEFORE) {
            result = (T) redisUtil.get(input.getCacheKey(), input.getCacheClass());
            return result;
        }

        if (apiConfig.getPersistentFlag() == BIN_YES) {
            result = (T) mongoUtil.findById(input.getCacheKey(), input.getCacheClass());
            return result;
        }

        return result;
    }

    /**
     * 接口故障后， 获取本地数据（接口缓存或持久化的数据）
     * @param apiConfig
     * @param <T>
     * @return
     */
    private <T extends CPSPOutput> T accessLocalAfter(CpspServiceApiConfigVo apiConfig, CPSPInput input) {
        T result = null;
        if (apiConfig.getCacheOrder() == CACHE_AFTER) {
            result = (T) redisUtil.get(input.getCacheKey(), input.getCacheClass());
            return result;
        }

        if (apiConfig.getPersistentFlag() == BIN_YES) {
            result = (T) mongoUtil.findById(input.getCacheKey(), input.getCacheClass());
            return result;
        }

        return result;
    }

    /**
     * 根据接口的配置策略， 缓存接口数据到本地
     * @param cacheKey
     * @param apiConfig
     * @param cacheObject
     * @param <T>
     */
    private <T extends CPSPOutput> void cacheLocal(String cacheKey, CpspServiceApiConfigVo apiConfig, Object cacheObject) {
        if (apiConfig.getCacheFlag() == 1) {
            redisUtil.set(cacheKey, apiConfig.getCacheDuration(), cacheObject);
        } else if (apiConfig.getPersistentFlag() == 1) {
            mongoUtil.save(cacheObject);
        }
    }

    /**
     * 调用第三方平台接口处理过程
     * @param input
     * @param apiConfig
     * @param <T>
     * @return
     */
    private <T extends CPSPOutput> T process(CpspServiceApiConfigVo apiConfig, CPSPInput<T> input) {
        //0.获取当前服务接口所有提供商配置策略
        ChannelDomain channelDomain = channelConfig(input);

        //1.调用规则引擎，计算出目标渠道路由
        String apiProviderKey = ruleEngine(channelDomain, input.getCurrentAPI());

        //2.路由适配目标提供商具体业务接口，返回数据
        AbstractStrategy strategy = strategyFactory.getInstance(apiProviderKey);
        T result = callWithRetry(apiConfig, channelDomain, strategy, input);

        //根据接口配置策略缓存数据或持久化(TODO mongodb id setting）
//        result.setId(input.getCacheKey());
        cacheLocal(input.getCacheKey(), apiConfig, result);

        return result;
    }

    /**
     * 支持重试的第三方平台接口调用
     * @param <T>
     * @param apiConfig
     * @param strategy
     * @param input
     * @return
     */
    private <T extends CPSPOutput> T callWithRetry(CpspServiceApiConfigVo apiConfig, ChannelDomain channel, AbstractStrategy strategy, CPSPInput input) {

        AtomicReference<T> result = new AtomicReference<>();
        String msg = null;
        boolean flag = false;
        try {
            //1.不重试
            if (apiConfig.getRetryFlag() == 0) {
                return (T) strategy.call(input);
            }

            //2.重试
            Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder().retryIfException()
                    .withWaitStrategy(WaitStrategies.incrementingWait(INITIAL_SLEEP_TIME, TimeUnit.MILLISECONDS, INCREMENT_TIME, TimeUnit.MILLISECONDS))
                    .withStopStrategy(StopStrategies.stopAfterAttempt(apiConfig.getRetryTime()))
                    .withRetryListener(new RetryListener() {
                        @Override
                        public <V> void onRetry(Attempt<V> attempt) {
                            if (attempt.hasException()) {
                                log.error("接口调用重试时异常", attempt.getExceptionCause());
                                handleMalfunction(apiConfig.getId(), channel.getServiceProviderId(), attempt.getExceptionCause().getMessage());
                            }
                        }
                    }).build();

            AtomicInteger count = new AtomicInteger();

            retryer.call(() -> {
                log.info("#CPSP调用接口记录, {} times#", count.addAndGet(1));

                CPSPOutput call = null;

                //渠道路由适配目标提供商服务.
                if (count.get() == 1) {
                    call = strategy.call(input);
                } else {
                    //0.获取接口配置信息
                    ChannelDomain reChannel = channelConfig(input);

                    //1.重新调用规则引擎，计算出目标渠道路由
                    String apiProviderKey = ruleEngine(reChannel, input.getCurrentAPI());

                    //2.路由适配目标提供商具体业务接口，返回数据
                    AbstractStrategy reStrategy = strategyFactory.getInstance(apiProviderKey);
                    call = reStrategy.call(input);
                }

                if (call != null) {
                    result.set((T) call);
                }
                return true;
            });
        } catch (ExecutionException e) {
            flag = true;
            msg = "接口重试callable调用失败";
            log.error(msg, e);
        } catch (RetryException e) {
            flag = true;
            msg = "接口重试执行异常";
            log.error(msg, e);
        } catch (Exception e) {
            flag = true;
            msg = "第三方平台提供商接口调用失败";
            handleMalfunction(apiConfig.getId(), channel.getServiceProviderId(), msg);
            log.error(msg, e);
        }

        if (flag) {
            //缓存模式二：接口故障时，尝试从缓存或持久化数据中读取
            T resultLocal = (T) accessLocalAfter(apiConfig, input);

            if (resultLocal == null) {
                log.error("invoke thirdparty error, get data from local. params: {}, msg: {}", input, msg);
                //TODO 异常信息内容优化
                throw new AdamException(RespCode.OTHER_SERVICE_INVOKE_ERROR, "服务器异常，请稍后再试！");
            }

            return resultLocal;
        }

        return result.get();
    }

    private void handleMalfunction(Long serviceApiConfigId, Long serviceProviderId, String errLog) {
        //TODO 优化 530版本
        log.error("handleMalfunction:{}, {}, {}", serviceApiConfigId, serviceProviderId, errLog);
//        threadPoolUtil.execute(() -> {
//            //插入错误日志并且更新Qos评分
//            CpspLogAndApiQosDto dto = new CpspLogAndApiQosDto();
//            dto.setServiceApiConfigId(String.valueOf(serviceApiConfigId));
//            dto.setServiceProviderId(String.valueOf(serviceProviderId));
//            dto.setSignificantFlag(1);
//            dto.setSuccessFlag("0");
//            dto.setRespTime(100);
//            dto.setErrLog(errLog);
//            qosFeignService.dealLogAndApiQos(dto);
//        });
    }

    /**
     * 规则引擎获取目标渠道路由
     * @param channelDomain
     * @param currentAPI
     * @return
     */
    private String ruleEngine(ChannelDomain channelDomain, String currentAPI) {
        RulesStrategy rulesStrategy = rulesStrategyContext.getRulesStrategy(Constants.STRATEGY_DEFAULT);
        rulesStrategy.call(channelDomain, new BaseDomain());
        InstChannelApiDomain instChannel = channelDomain.getInstChannelApis().get(0);
        channelDomain.setServiceProviderId(instChannel.getServiceProviderId());
        String strategyKey = new StringBuilder(instChannel.getGatewayChannelApi()).append(Constants.SEP).append(currentAPI).toString();
        return strategyKey;
    }

    /**
     * 当前请求CPSP服务接口配置
     * @param projectId
     * @param serviceCode
     * @param apiCode
     * @return
     */
    private CpspServiceApiConfigVo currentAPIConfig(String projectId, String serviceCode, String apiCode) {
        String genKey = redisUtil.createKey(Constants.CACHE_KEY_API_CONFIG, projectId, serviceCode, apiCode);
        if(redisUtil.hasKey(genKey)){
            return (CpspServiceApiConfigVo) redisUtil.get(genKey,CpspServiceApiConfigVo.class);
        }else {
            RestResponse<List<CpspServiceApiConfigVo>> configResponse = apiConfigFeignService.getServiceApiConfig(projectId, serviceCode, apiCode);
            List<CpspServiceApiConfigVo> cacheConfigList = configResponse.getRespData();
            if (CollectionUtils.isEmpty(cacheConfigList)) {
                log.error("接口配置为空：projectId-{}, serviceCode-{}, apiCode-{}", projectId, serviceCode, apiCode);
                throw new AdamException(RespCode.SERVER_DATA_NOT_FOUND, "接口配置为空");
            }
            redisUtil.set(genKey,CACHE_DURATION,cacheConfigList.get(0));
            return cacheConfigList.get(0);
        }
    }

    /**
     * 当前渠道所有提供商的配置策略
     * @param input
     * @return
     */
    private ChannelDomain channelConfig(CPSPInput input) {
        List<CpspServiceProviderVo> channelConfig;

        String genKey = redisUtil.createKey(Constants.CACHE_KEY_PROVIDER_SERVICE, input.getProjectId(), input.getServiceCode());
        if(redisUtil.hasKey(genKey)){
            channelConfig = JSON.parseObject(JSON.toJSONString(redisUtil.get(genKey,List.class)), new TypeReference<>() {
            });
        }else {
            channelConfig = providerServiceFeignService.getProviderService(input.getProjectId(), input.getServiceCode()).getRespData();
            redisUtil.set(genKey,CACHE_DURATION,channelConfig);
        }

        List<InstChannelApiDomain> channels = new ArrayList<>();
        channelConfig.stream().forEach(entry -> {
            InstChannelApiDomain channel = new InstChannelApiDomain();
            channel.setServiceProviderId(entry.getId());
            channel.setGatewayChannelApi(entry.getProviderCode());
            channel.setPriority(entry.getPriority());
            channel.setDefaultChannel(entry.getDefaultFlag() == 1);
            channel.setQosRate(entry.getQosRate());
            channels.add(channel);
        });

        ChannelDomain channelDomain = new ChannelDomain();
        channelDomain.setInstChannelApis(channels);

        return channelDomain;
    }
}
