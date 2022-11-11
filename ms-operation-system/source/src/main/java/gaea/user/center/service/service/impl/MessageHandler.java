package gaea.user.center.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.kafka.domain.KafkaMessage;
import com.bnmotor.icv.adam.kafka.handler.IMessageHandler;
import gaea.user.center.service.service.IUserCarService;
import gaea.user.center.service.service.IUserVehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class MessageHandler implements IMessageHandler {
    @Autowired
    IUserCarService userCarService;

    @Autowired
    IUserVehicleService userVehicleService;

    /**
     * 消费Kafka消息，并根据响应体保存用户-车辆关系
     * @param kafkaMessage
     * @throws AdamException
     */
    @Override
    public void onMessage(KafkaMessage kafkaMessage) throws AdamException {
        String messageValue = kafkaMessage.getValue();
        if(!StringUtils.isEmpty(messageValue)){
            JSONObject jsonObject = JSONObject.parseObject(messageValue);
            Long userId = null;
            if(null != jsonObject.get("uid") && !jsonObject.get("uid").equals("")){
                userId = Long.valueOf(jsonObject.getString("uid"));
            }
            Integer type = Integer.valueOf(StringUtils.isEmpty(jsonObject.get("type")) ? "" : jsonObject.getString("type"));
            String vin = StringUtils.isEmpty(jsonObject.get("vin")) ? "" : jsonObject.getString("vin");
            Long config = jsonObject.getLong("configId") == null ? null : jsonObject.getLong("configId");
            JSONArray tagIdList= JSONArray.parseArray(StringUtils.isEmpty(jsonObject.get("tagIds")) ? null : jsonObject.getString("tagIds"));
            //userCarService.insertUserCarBatchList(userId,type,vin,config,tagIdList);
            userVehicleService.insertUserVehicleBatchList(userId,type,vin,config,tagIdList);
            log.info("================消息消费完毕==============");
        }
    }
}
