package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName: TboxUpgradStatusV1Vo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/8/19 14:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class TboxUpgradStatusV1Vo {
    @ApiModelProperty(value = "TBOX升级状态：0=其他状态，需进行版本检查， 1=正在下载，1=下载完成（未安装），2=下载完成（已预约安装），3=安装中（要带上安装进度）", example = "1", required = true)
    private Integer status;

    @ApiModelProperty(value = "TBOX升级状态描述", example = "", required = true)
    private String desc;

    @ApiModelProperty(value = "TBOX升级状态附件信息", example = "", required = true)
    private Map<String, Object> additionalInfo;
}
