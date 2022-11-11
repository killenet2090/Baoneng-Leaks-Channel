package com.bnmotor.icv.tsp.vehstatus.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName: NotifyEventDto
 * @Description: 通知事件参数实体
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "通知事件参数实体对象", description = "充电通知事件参数实体对象")
public class NotifyEventDto {
    /**
     * redis对应key
     */
    private String redisKey;
    /**
     * 车辆vin
     */
    private String vin;
    /**
     * 平台时间
     */
    private Long platformTime;
    /**
     * 存入redis的map对象
     */
    private Map<Object, Object> saveRedisMap;
}
