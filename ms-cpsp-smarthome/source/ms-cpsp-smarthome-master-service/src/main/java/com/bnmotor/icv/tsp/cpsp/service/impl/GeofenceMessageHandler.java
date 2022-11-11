package com.bnmotor.icv.tsp.cpsp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bnmotor.icv.adam.kafka.domain.KafkaMessage;
import com.bnmotor.icv.adam.kafka.handler.IMessageHandler;
import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;
import com.bnmotor.icv.tsp.cpsp.service.ISceneService;
import com.bnmotor.icv.tsp.cpsp.service.ISceneGeofenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: GeofenceMessageHandler
 * @Description: 消费围栏触发事件处理器
 * @author: jiangchangyuan1
 * @date: 2021/3/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class GeofenceMessageHandler implements IMessageHandler {

    @Autowired
    ISceneGeofenceService sceneGeofenceService;
    @Autowired
    ISceneService sceneService;

    @Override
    public void onMessage(KafkaMessage kafkaMessage) {
        String message = kafkaMessage.getValue();
        log.info("kafka消息内容为:{}",message);
        if(!StringUtils.isEmpty(message)){
            JSONObject jsonObject = JSONObject.parseObject(message);
            String vin = StringUtils.isEmpty(jsonObject.get("vin")) ? "" : jsonObject.getString("vin");
            String fenceId = StringUtils.isEmpty(jsonObject.get("fenceId")) ? "" : jsonObject.getString("fenceId");
            Integer eventType = jsonObject.getInteger("eventType") == null ? null : jsonObject.getInteger("eventType");
            //根据地理围栏ID查询出已绑定智能场景列表
            List<SceneGeofenceVo> sceneGeofenceVos = sceneGeofenceService.getSceneGeofenceList(fenceId,eventType);
            List<String> sceneIds = new ArrayList<>();
            for(SceneGeofenceVo vo : sceneGeofenceVos){
                sceneIds.add(vo.getSceneId());
            }
            //根据符合条件的智能场景ID列表执行智能场景
            sceneService.sceneExecution(sceneIds,vin);
        }
    }
}
