package com.bnmotor.icv.tsp.device.model.request.feign;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: JpushContentDetail
 * @Description: 极光推送内容明细
 * @author: huangyun1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class JpushComDetailDto {
    /**
     * 成功标识
     */
    public static final Integer SUCCESS_FLAG = 0;
    /**
     * 失败标识
     */
    public static final Integer FAIL_FLAG = 1;
    /**
     * 车辆识别号
     */
    @ApiModelProperty(value = "车辆识别号", name = "vin", required = true, example = "T000000001")
    private String vin;

    /**
     * 状态值:0-成功;1-失败
     */
    @ApiModelProperty(value = "状态值", name = "status", required = true, example = "0")
    private Integer status;

    /**
     * 子状态值:只有status=0时才返回 1-成功；2-设置成功，待下次启动后生效
     */
    @ApiModelProperty(value = "子状态值", name = "subStatus", required = true, example = "1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer subStatus;

    /**
     * 操作类型:1-设置临客;2-取消临客;3-车机激活
     */
    @ApiModelProperty(value = "1-设置临客;2-取消临客;3-车机激活", name = "opType", required = true, example = "1")
    private Integer opType;

    @ApiModelProperty(value = "认证状态")
    private Integer certificationStatus;
}
