package com.bnmotor.icv.tsp.ota.model.tbox.upgrade;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UpgradResultDetail
 * @Description:  升级结果详情
 * @author: xuxiaochang1
 * @date: 2020/7/9 17:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class UpgradeResultDetail /*extends BaseMongoEntity*/ {

    private static final long serialVersionUID = 2612857267257978138L;

    @ApiModelProperty(value = "固件Id", example = "1234567", required = true)
    private Long firmwareId;

    @ApiModelProperty(value = "当前系统最终运行版本", example = "V1.10", required = true)
    private String firmwareVersionNo;

    /**
     *  开始时间
     */
    @ApiModelProperty(value = "开始时间", example = "1000L", dataType = "Long")
    private Long startTime;

    /**
     *  结束时间
     */
    @ApiModelProperty(value = "结束时间", example = "1000L", dataType = "Long")
    private Long endTime;

    /**
     *  安装最终失败时间
     */
    @ApiModelProperty(value = "最终失败时间", example = "1000L", dataType = "Long")
    private Long lastFailTime;

    @ApiModelProperty(value = "最终失败原因", example = "顶顶顶顶", required = true)
    private String lastFailReason;

    @ApiModelProperty(value = "重试次数", example = "3", required = true)
    private Integer retryNum;
    /**
     *  最终重试时间
     */
    @ApiModelProperty(value = "最终重试时间", example = "1000L", dataType = "Long")
    private Long retryTime;

    @ApiModelProperty(value = "1", example = "0",notes = "ecu固件升级结果：1=未完成,2=已完成", required = true)
    private Integer status;
}
