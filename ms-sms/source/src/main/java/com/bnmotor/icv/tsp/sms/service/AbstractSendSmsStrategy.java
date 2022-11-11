package com.bnmotor.icv.tsp.sms.service;

import cn.jiguang.common.resp.BaseResult;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jsms.api.SendSMSResult;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.sms.common.Constant;
import com.bnmotor.icv.tsp.sms.common.enums.BusinessRetEnum;
import com.bnmotor.icv.tsp.sms.common.enums.InteceptTypeEnum;
import com.bnmotor.icv.tsp.sms.common.util.CommonUtils;
import com.bnmotor.icv.tsp.sms.component.ApplicationContextComp;
import com.bnmotor.icv.tsp.sms.component.ChannelRuleCache;
import com.bnmotor.icv.tsp.sms.component.EnvironmentComp;
import com.bnmotor.icv.tsp.sms.model.entity.ChannelRulePo;
import com.bnmotor.icv.tsp.sms.model.entity.SmsInfoPo;
import com.bnmotor.icv.tsp.sms.model.request.AbstractSmsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

/**
 * @ClassName: AbstractSendSmsStrategy
 * @Description: 抽象发送短信策略实现类
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public abstract class AbstractSendSmsStrategy<R extends BaseResult, T extends AbstractSmsDto> implements ISendSmsStrategy<R, T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractSendSmsStrategy.class);

    @Autowired
    private EnvironmentComp environmentComp;
    /**
     * redis操作模板
     */
    private final RedisTemplate redisTemplate;

    /**
     * 构造方法
     */
    public AbstractSendSmsStrategy() {
        redisTemplate = CommonUtils.setRedisTemplateSerializer(ApplicationContextComp.getBeanOfType(RedisTemplate.class));
    }

    /**
     * 判断是否允许发送短信
     * @return boolean
     */
    @Override
    public boolean checkEnableSms(T parameter) {
        ChannelRulePo channelRulePo = ChannelRuleCache.getRulesMap().get(parameter.getSendChannel());
        if(channelRulePo != null) {
            String saveRedisKey = new StringBuffer().append(Constant.REDIS_PROJECT_PREFIX)
                    .append(Constant.REDIS_JOINER_CHAR)
                    .append(Constant.ROSTER_MODEL)
                    .append(Constant.REDIS_JOINER_CHAR)
                    .append(parameter.getSendChannel()).toString();
            if(InteceptTypeEnum.PASS_ALL.getValue().equals(channelRulePo.getInterceptType())) {
                //如果放行全部 查看是否存在黑名单
                return !redisTemplate.opsForSet().isMember(saveRedisKey, parameter.getSendPhone());
            } else {
                //如果拦截全部 查看是否存在白名单
                return redisTemplate.opsForSet().isMember(saveRedisKey, parameter.getSendPhone());
            }
        }
        return true;
    }

    /**
     * 发送短信
     * @return R 结果
     */
    @Override
    public R sendSms(T parameter) {
        try {
            boolean enabledSms = environmentComp.getProperty(Constant.ENABLE_SMS_KEY, Boolean.class);
            boolean enableBlackWhiteList = environmentComp.getProperty(Constant.ENABLE_BLACK_WHITE_LIST_KEY, Boolean.class);
            //判断黑白名单拦截
            if(enabledSms && enableBlackWhiteList && !checkEnableSms(parameter)) {
                throw new AdamException(BusinessRetEnum.INTECEPT_RETURN);
            }
            //记录数据库
            SmsInfoPo smsInfoPo = saveSmsInfo(parameter);
            //判断是否开启了短信功能
            if(!enabledSms) {
                ResponseWrapper responseWrapper = new ResponseWrapper();
                responseWrapper.responseCode = HttpStatus.OK.value();
                responseWrapper.responseContent = null;
                SendSMSResult smsResult = new SendSMSResult();
                smsResult.setResponseWrapper(responseWrapper);
                return (R)smsResult;
            }
            //发送并保存结果
            return callSendAndRecordResult(parameter, smsInfoPo);
        } catch (AdamException e) {
            throw e;
        } catch (Exception e) {
            log.error("发送短信发生异常", e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 保存短信信息
     * @param parameter
     */
    protected abstract SmsInfoPo saveSmsInfo(T parameter);

    /**
     * 调用发送并记录结果
     * @param parameter
     * @return R
     */
    protected abstract R callSendAndRecordResult(T parameter, SmsInfoPo smsInfoPo);

}
