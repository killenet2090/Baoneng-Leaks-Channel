package com.bnmotor.icv.tsp.sms.component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.sms.common.Constant;
import com.bnmotor.icv.tsp.sms.common.enums.DelFlagEnum;
import com.bnmotor.icv.tsp.sms.common.enums.EnabledFlagEnum;
import com.bnmotor.icv.tsp.sms.common.enums.InteceptTypeEnum;
import com.bnmotor.icv.tsp.sms.common.enums.RosterTypeEnum;
import com.bnmotor.icv.tsp.sms.common.util.CommonUtils;
import com.bnmotor.icv.tsp.sms.common.util.DateUtils;
import com.bnmotor.icv.tsp.sms.mapper.ChannelRosterMapper;
import com.bnmotor.icv.tsp.sms.mapper.ChannelRuleMapper;
import com.bnmotor.icv.tsp.sms.model.entity.ChannelRosterPo;
import com.bnmotor.icv.tsp.sms.model.entity.ChannelRulePo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ChannelRuleCache
 * @Description: 渠道规则缓存
 * @author: huangyun1
 * @date: 2020/8/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class ChannelRuleCache {
    @Resource
    private ChannelRuleMapper channelRuleMapper;
    @Resource
    private ChannelRosterMapper channelRosterMapper;
    @Autowired
    private EnvironmentComp environmentComp;
    /**
     * 渠道规则map
     */
    private static final Map<Integer, ChannelRulePo> RULES_MAP = new HashMap<>();
    /**
     * redis操作template
     */
    private final RedisTemplate redisTemplate;
    /**
     * 渠道规则更新时间间隔(ms)
     */
    private static final Long UPDATE_TIME_INTERVAL = 60_000L;



    /**
     * 构造函数
     * @param redisTemplate
     */
    @Autowired
    public ChannelRuleCache(RedisTemplate redisTemplate) {
        this.redisTemplate = CommonUtils.setRedisTemplateSerializer(redisTemplate);
    }

    /**
     * 初始化渠道规则缓存
     */
    @PostConstruct
    private void initChannelRuleCache() {
        //从数据库读取渠道规则和黑白名单
        loadChannelRuleAndRoster(true);
        //定时更新 暂时功能
        intervalUpdateRosterTask();
    }

    /**
     * 加载渠道规则和黑白名单
     */
    private void loadChannelRuleAndRoster(boolean isInitialization) {
        ChannelRulePo queryBean = new ChannelRulePo();
        queryBean.setEnableFlag(EnabledFlagEnum.ENABLE.getValue());
        List<ChannelRulePo> ruleList = channelRuleMapper.queryRuleList(queryBean);
        Map<Integer, Boolean> keysMap = new HashMap<>(8);
        //增量更新
        ruleList.stream().forEach(rule -> {
            RULES_MAP.put(rule.getSmsChannel(), rule);
            keysMap.put(rule.getSmsChannel(), true);
        });
        String prefixRedisKey = new StringBuffer().append(Constant.REDIS_PROJECT_PREFIX)
                .append(Constant.REDIS_JOINER_CHAR)
                .append(Constant.ROSTER_MODEL)
                .append(Constant.REDIS_JOINER_CHAR).toString();

        for(Iterator<Map.Entry<Integer, ChannelRulePo>> it = RULES_MAP.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, ChannelRulePo> entry = it.next();
            if(keysMap.get(entry.getKey()) == null) {
                it.remove();
                String redisKey = prefixRedisKey + entry.getKey();
                redisTemplate.delete(redisKey);
            }
        }

        //是否已初始化黑白名单缓存
        ruleList.stream().forEach(rule -> {
            String saveRedisKey =prefixRedisKey + rule.getSmsChannel();
            if(!redisTemplate.opsForSet().isMember(saveRedisKey, Constant.HAS_INIT_FLAG)) {
                redisTemplate.opsForSet().add(saveRedisKey, Constant.HAS_INIT_FLAG);
                //从数据库加载
                loadRosterToRedis(saveRedisKey, rule, true);
            } else {
                //从数据库加载
                loadRosterToRedis(saveRedisKey, rule, isInitialization);
            }
        });
    }

    /**
     * 加载黑白名单分页列表
     */
    private void loadRosterToRedis(String setKey, ChannelRulePo channelRule, boolean isInitialization) {
        //进行分页查询相应黑白名单记录
        ChannelRosterPo channelRosterPo = new ChannelRosterPo();
        channelRosterPo.setChannelRuleId(channelRule.getId());

        if(!isInitialization) {
            Long updateTimesMillis = System.currentTimeMillis() - UPDATE_TIME_INTERVAL * 2;
            channelRosterPo.setUpdateTime(DateUtils.getLocalDateTimeFromMill(updateTimesMillis));
        }

        if(InteceptTypeEnum.PASS_ALL.getValue().equals(channelRule.getInterceptType())) {
            channelRosterPo.setRosterType(RosterTypeEnum.BLACK_ROSTER.getValue());
        } else {
            channelRosterPo.setRosterType(RosterTypeEnum.WHITE_ROSTER.getValue());
        }

        try {
            long pageIndex = 1;
            long pageSize = 500;
            Page firstPage = new Page<>(pageIndex, pageSize);
            Page firstResult = channelRosterMapper.queryRosterPageList(firstPage, channelRosterPo);
            saveRosterToRedis(setKey, firstResult);
            if(firstResult.getPages() > 1) {
                for(pageIndex += 1; pageIndex < firstResult.getPages(); pageIndex++) {
                    Page page = new Page<>(pageIndex, pageSize);
                    Page<ChannelRosterPo> result = channelRosterMapper.queryRosterPageList(page, channelRosterPo);
                    saveRosterToRedis(setKey, result);
                }
            }
        } catch (Exception e) {
            log.error("加载黑白名单到缓存发生异常[{}]", e);
        }
    }

    /**
     * 保存黑白名单到redis缓存
     */
    private void saveRosterToRedis(String setKey, Page<ChannelRosterPo> result) {
        redisTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                result.getRecords().stream().forEach(record -> {
                    //判断记录状态是删除/未启用 还是正常状态
                    if(EnabledFlagEnum.NO_ENABLE.getValue().equals(record.getEnableFlag())
                            || DelFlagEnum.DELETE.getValue().equals(record.getDelFlag())) {
                        //存在则删除
                        operations.opsForSet().remove(setKey, record.getPhone());
                    } else {
                        //不存在则新增
                        operations.opsForSet().add(setKey, record.getPhone());
                    }
                });
                return null;
            }
        });
    }

    /**
     * 为兼容现在没有运营后台设置,定时更新 后续移除
     */
    private void intervalUpdateRosterTask() {
        Thread thread = new Thread(() -> {
            while(true) {
                try {
                    boolean enableBlackWhiteList = environmentComp.getProperty(Constant.ENABLE_BLACK_WHITE_LIST_KEY, Boolean.class);
                    if(!enableBlackWhiteList) {
                        Thread.sleep(UPDATE_TIME_INTERVAL);
                        continue;
                    }
                    loadChannelRuleAndRoster(false);
                    Thread.sleep(UPDATE_TIME_INTERVAL);
                    RULES_MAP.values().forEach(rule -> {
                        String saveRedisKey = new StringBuffer().append(Constant.REDIS_PROJECT_PREFIX)
                                .append(Constant.REDIS_JOINER_CHAR)
                                .append(Constant.ROSTER_MODEL)
                                .append(Constant.REDIS_JOINER_CHAR)
                                .append(rule.getSmsChannel()).toString();
                    });
                } catch (InterruptedException e) {
                    log.error("处理定时更新黑白名单发生异常[{}]", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static Map<Integer, ChannelRulePo> getRulesMap() {
        return new HashMap<>(RULES_MAP);
    }
}
