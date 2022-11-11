package com.bnmotor.icv.tsp.device.model.response.vehModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: VehConfigPicVo
 * @Description: 车辆配置图片
 * @author: zhangwei2
 * @date: 2020/11/16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehConfigPicVo {
    @ApiModelProperty(value = "年款名称")
    private String yearStyleName;

    @ApiModelProperty(value = "配置id")
    private String configId;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "总图片数目")
    private Integer totalPics;

    @ApiModelProperty(value = "图片列表")
    private List<PicVo> pics;
}
