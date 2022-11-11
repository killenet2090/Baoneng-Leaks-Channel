package com.bnmotor.icv.tsp.device.model.response.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: GetICCIDStateVo
 * @Description: 查询认证状态返回对象
 * @author: huangyun1
 * @date: 2020/12/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "查询认证状态返回对象", description = "查询认证状态返回对象")
public class GetICCIDStateVo {

    @ApiModelProperty(value = "认证状态")
    private Integer certificationStatus;

    /**
     * 剩余时间
     */
    @ApiModelProperty(value = "剩余时间", name = "remainTime")
    private Long remainTime;

    /**
     * 唇语数字
     */
    @ApiModelProperty(value = "唇语数字", name = "digits")
    private String digits;

    /**
     * 业务流水号
     */
    @ApiModelProperty(value = "业务流水号", name = "transId")
    private String transId;
}
