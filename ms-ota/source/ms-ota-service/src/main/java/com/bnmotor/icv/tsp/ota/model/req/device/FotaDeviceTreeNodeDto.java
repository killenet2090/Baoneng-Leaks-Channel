package com.bnmotor.icv.tsp.ota.model.req.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName: FotaDeviceTreeNodeDto
 * @Description: 设备树节点信息
 * @author: xuxiaochang1
 * @date: 2020/10/27 10:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ApiModel(value="车辆设备树节点同步信息对象", description="车辆设备树节点同步信息对象")
public class FotaDeviceTreeNodeDto implements Serializable {
    private static final long serialVersionUID = 4600580160910285958L;

    @ApiModelProperty(value = "项目id,用于定义树的归属", example = "guanzhi001")
    private String projectId;

    @ApiModelProperty(value = "品牌")
    @NotEmpty(message = "品牌不能为空")
    private String brand;

    @ApiModelProperty(value = "品牌代码")
    private String brandCode;

    @ApiModelProperty(value = "车系")
    /*@NotEmpty(message = "车系不能为空")*/
    private String series;

    @ApiModelProperty(value = "车系代码")
    private String seriesCode;

    @ApiModelProperty(value = "车型")
    /*@NotEmpty(message = "车型不能为空")*/
    private String model;

    @ApiModelProperty(value = "车系代码")
    private String modelCode;

    @ApiModelProperty(value = "年款")
    /*@NotEmpty(message = "年款不能为空")*/
    private String year;

    @ApiModelProperty(value = "年款代码")
    private String yearCode;

    @ApiModelProperty(value = "配置")
/*    @NotEmpty(message = "配置不能为空")*/
    private String conf;

    @ApiModelProperty(value = "配置代码")
    private String confCode;

    @ApiModelProperty(value = "节点的层次:0=品牌,1=车系,2=车型,3=年款,4=配置")
    private Integer treeLevel;
}
