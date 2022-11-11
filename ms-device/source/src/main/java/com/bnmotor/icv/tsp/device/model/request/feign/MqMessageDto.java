package com.bnmotor.icv.tsp.device.model.request.feign;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: MqMessageDto
 * @Description: 消息发送对象
 * @author: zhangwei2
 * @date: 2020/10/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@JsonInclude
public class MqMessageDto {
    private Integer type;
    private Long uid;
    private String vin;
    private Long configId;
    private List<Long> tagIds;
}
