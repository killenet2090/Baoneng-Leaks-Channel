package com.bnmotor.icv.tsp.device.job;

import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.job.dispatch.AsynQueryStatus;
import com.bnmotor.icv.tsp.device.common.enums.dataaysn.MessageType;
import com.bnmotor.icv.tsp.device.job.dispatch.QueryType;
import com.bnmotor.icv.tsp.device.mapper.VehicleAsynQueryMapper;
import com.bnmotor.icv.tsp.device.mapper.VehicleMapper;
import com.bnmotor.icv.tsp.device.mapper.VehicleTagMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleAsynQueryPo;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleTagPo;
import com.bnmotor.icv.tsp.device.model.request.feign.MqMessageDto;
import com.bnmotor.icv.tsp.device.service.mq.producer.KafkaSender;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AsynQueryVehicleJob
 * @Description: 异步查询车辆任务
 * @author: zhangwei2
 * @date: 2020/10/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class AsynQueryVehicleJob {
    private static final Integer LIMIT = 10;
    @Resource
    private VehicleAsynQueryMapper aysnQueryMapper;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private VehicleTagMapper vehTagMapper;
    @Resource
    private KafkaSender mqSender;

    @XxlJob("aysnQueryVehicle")
    public ReturnT<String> aysnQueryVehicle(String param) {
        List<VehicleAsynQueryPo> noExecuteQuerys = aysnQueryMapper.listNoExecute(LIMIT, 0);
        while (CollectionUtils.isNotEmpty(noExecuteQuerys)) {
            for (VehicleAsynQueryPo queryPo : noExecuteQuerys) {
                processAsynQuery(queryPo);
                // 更新状态
                queryPo.setStatus(AsynQueryStatus.SUCCESSED.getStatus());
                queryPo.setUpdateBy(Constant.SYSTEM);
                aysnQueryMapper.updateById(queryPo);
            }

            noExecuteQuerys = aysnQueryMapper.listNoExecute(LIMIT, 0);
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 处理查询
     *
     * @param queryPo 查询实体
     */
    private void processAsynQuery(VehicleAsynQueryPo queryPo) {
        if (QueryType.CONFIG_ID.getType().equals(queryPo.getQueryType())) {
            List<VehiclePo> vehiclePos = vehicleMapper.listByConfigId(queryPo.getQueryCursor(), queryPo.getQueryValue(), 100);
            while (CollectionUtils.isNotEmpty(vehiclePos)) {
                Long fromId = vehiclePos.get(vehiclePos.size() - 1).getId();
                for (VehiclePo vehiclePo : vehiclePos) {
                    MqMessageDto messageDto = new MqMessageDto();
                    messageDto.setType(MessageType.TOTAL.getType());
                    messageDto.setUid(queryPo.getUid());
                    messageDto.setVin(vehiclePo.getVin());
                    mqSender.sendMqMsg(messageDto);
                }
                // 更新游标
                queryPo.setUpdateBy(Constant.SYSTEM);
                queryPo.setQueryCursor(fromId);
                aysnQueryMapper.updateById(queryPo);
                vehiclePos = vehicleMapper.listByConfigId(fromId, queryPo.getQueryValue(), 100);
            }
        }

        if (QueryType.TAG_ID.getType().equals(queryPo.getQueryType())) {
            List<VehicleTagPo> tagPos = vehTagMapper.listByTagId(queryPo.getQueryCursor(), queryPo.getQueryValue(), 100);
            while (CollectionUtils.isNotEmpty(tagPos)) {
                Long fromId = tagPos.get(tagPos.size() - 1).getId();
                for (VehicleTagPo tagPo : tagPos) {
                    MqMessageDto messageDto = new MqMessageDto();
                    List<Long> tagIds = new ArrayList<>();
                    tagIds.add(queryPo.getQueryValue());
                    messageDto.setType(MessageType.TOTAL.getType());
                    messageDto.setUid(queryPo.getUid());
                    messageDto.setVin(tagPo.getVin());
                    mqSender.sendMqMsg(messageDto);
                }
                // 更新游标
                queryPo.setUpdateBy(Constant.SYSTEM);
                queryPo.setQueryCursor(fromId);
                aysnQueryMapper.updateById(queryPo);
                tagPos = vehTagMapper.listByTagId(fromId, queryPo.getQueryValue(), 100);
            }
        }
    }
}
