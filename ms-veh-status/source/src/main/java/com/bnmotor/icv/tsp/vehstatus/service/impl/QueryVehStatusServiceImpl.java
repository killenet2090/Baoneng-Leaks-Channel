package com.bnmotor.icv.tsp.vehstatus.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.data.redis.StringRedisProvider;
import com.bnmotor.icv.tsp.vehstatus.aop.annotation.ResultTransfer;
import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.common.util.CommonUtils;
import com.bnmotor.icv.tsp.vehstatus.config.convert.VehStatusConvertForMap;
import com.bnmotor.icv.tsp.vehstatus.service.QueryVehStatusService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName: VehControlServiceImpl
 * @Description: 查询车况service接口实现类
 * @author: huangyun1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class QueryVehStatusServiceImpl implements QueryVehStatusService {

    @Autowired
    private StringRedisProvider stringRedisProvider;
    @Autowired
    VehStatusConvertForMap<String> statusNameForStatusConvertMap;
    @Autowired
    VehStatusConvertForMap<String> statusKeyForStatusConvertMap;


    @Override
    public Map<String, Object> queryVehStatusMap(String vin, String[] mappingColumnNames) {
        Map<String, Object> resultMap = new HashMap<>(130);
        if(mappingColumnNames == null || mappingColumnNames.length <= 0) {
            return null;
        }
        List<String> fields = new ArrayList<>();
        if(mappingColumnNames != null && mappingColumnNames.length > 0) {
            for(String mappingName : mappingColumnNames) {
                fields.add(statusNameForStatusConvertMap.get(mappingName).getCode());
            }
        }
        String[] fieldsArray = new String[fields.size()];
        fields.toArray(fieldsArray);
        String redisKey = CommonUtils.appendString(Constant.REDIS_JOINER_CHAR, Constant.REDIS_PROJECT_PREFIX, Constant.STATUS_MODEL, vin);
        List<Object> redisResultList = stringRedisProvider.getMultiHash(redisKey, fieldsArray);
        if(redisResultList != null && !redisResultList.isEmpty()) {
            for(int i = 0; i < redisResultList.size(); i++) {
                resultMap.put(mappingColumnNames[i], redisResultList.get(i));
            }
        }
        return resultMap;
    }

    @Override
    @ResultTransfer
    public Map<String, String> queryVehStatusBean(String vin, Set<String> mappingColumnNames) {
        Map returnMap = Maps.newHashMap();
        List<String> fields = new ArrayList<>();
        String redisKey = CommonUtils.appendString(Constant.REDIS_JOINER_CHAR, Constant.REDIS_PROJECT_PREFIX, Constant.STATUS_MODEL, vin);
        if(mappingColumnNames != null && mappingColumnNames.size() > 0) {
            mappingColumnNames.stream().forEach(mappingName -> {
                //处理不存在的列
                VehStatusEnum vehStatusEnum = statusNameForStatusConvertMap.get(mappingName);
                if(vehStatusEnum == null) {
                    return;
                }
                fields.add(vehStatusEnum.getCode());
            });
            String[] fieldsArray = new String[fields.size()];
            fields.toArray(fieldsArray);
            List<Object> redisResultList = stringRedisProvider.getMultiHash(redisKey, fieldsArray);
            Iterator iterator = mappingColumnNames.iterator();
            if(redisResultList != null && !redisResultList.isEmpty()) {
                for(int i = 0; i < redisResultList.size(); i++) {
                    returnMap.put(iterator.next(), redisResultList.get(i));
                }
            }
        } else {
            Map redisResultMap = stringRedisProvider.getHashAll(redisKey);
            for(Object key : redisResultMap.keySet()) {
                try {
                    //处理不存在的列
                    VehStatusEnum vehStatusEnum = statusKeyForStatusConvertMap.get(String.valueOf(key));
                    if(vehStatusEnum == null) {
                        continue;
                    }
                    returnMap.put(vehStatusEnum.getColumnName(), redisResultMap.get(key));
                } catch (Exception e) {
                    log.error("处理redis结果集发生异常", e);
                    throw new AdamException(RespCode.UNKNOWN_ERROR);
                }
            }

        }
        return returnMap;
    }
}
