package com.bnmotor.icv.tsp.device.model.response.tag;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: VehicleTagVo
 * @Description: 车辆标签列表VO
 * @author: qiqi1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class VehicleTagVo implements Serializable {
    private static final long serialVersionUID = -3660965484662677300L;

    @ApiModelProperty("车辆标签ID")
    private Long id;

    @ApiModelProperty("车架号编码")
    private String vin;

    @ApiModelProperty("标签ID")
    private Long tagId;

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

}
