package com.bnmotor.icv.tsp.vehstatus.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @ClassName: MessageDto
 * @Description: 消息实体
 * @author: huangyun1
 * @date: 2020/12/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "消息实体对象", description = "消息实体对象")
public class MessageVo {
    @ApiModelProperty(value = "业务id", name = "businessId", required = false, example = "1001")
    private Long businessId;

    @ApiModelProperty(value = "业务类型", name = "businessType", required = true, example = "1")
    private Integer businessType;

    @ApiModelProperty(value = "时间戳", name = "timestamp", required = true, example = "1606721606000")
    private Long timestamp;

    @ApiModelProperty(value = "平台时间", name = "platformTime", required = true, example = "1606721606000")
    private Long platformTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

    @ApiModelProperty(value = "vin号", name = "vin", required = true, example = "T09988888888")
    private String vin;

    @ApiModelProperty(value = "消息版本", name = "version", required = true, example = "1")
    private Integer version = 1;

    @ApiModelProperty(value = "消息实体", name = "jsonPayload", required = false, example = "1")
    private String jsonPayload;
}
