package com.bnmotor.icv.tsp.ota.model.req.device;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FotaCarInfoDto
 * @Description: 车辆同步信息对象
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value="车辆对象信息同步对象", description="车辆对象信息同步对象")
public class FotaCarInfoDto implements Serializable {
    private static final long serialVersionUID = -3853261099460928391L;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "对象ID升级对象的唯一识别ID")
    @NotEmpty(message = "vin码不能为空")
    private String vin;

    @ApiModelProperty(value = "组织机构Id")
    private Long orgId;

    @ApiModelProperty(value = "品牌")
    @NotEmpty(message = "品牌不能为空")
    private String brand;

    @ApiModelProperty(value = "品牌代码")
    private String brandCode;

    @ApiModelProperty(value = "车系")
    @NotEmpty(message = "车系不能为空")
    private String series;

    @ApiModelProperty(value = "车系代码")
    private String seriesCode;

    @ApiModelProperty(value = "车型")
    @NotEmpty(message = "车型不能为空")
    private String model;

    @ApiModelProperty(value = "车型代码")
    private String modelCode;

    @ApiModelProperty(value = "年款")
    @NotEmpty(message = "年款不能为空")
    private String year;

    @ApiModelProperty(value = "年款代码")
    private String yearCode;

    @ApiModelProperty(value = "配置")
    @NotEmpty(message = "配置不能为空")
    private String conf;

    @ApiModelProperty(value = "配置代码")
    private String confCode;


    @ApiModelProperty(value = "车辆状态： 1-已创建， 2-已销售，3-已报废")
    private Integer vehStatus;

    @ApiModelProperty(value = "工作模式：0 - 工厂模式；1 - 展车模式；2 - 销售模式；3 - 用户模式；4 - 报废模式")
    private Integer vehLifecircle;

    @ApiModelProperty(value = "车牌号")
    private String licenceNo;

    @ApiModelProperty(value = "生产日期")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date productionDate;

    @ApiModelProperty(value = "下线日期")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date downlineDate;

    @ApiModelProperty(value = "生产批次")
    private String prodBatchNo;

    @ApiModelProperty(value = "当前区域")
    private String currentArea;

    @ApiModelProperty(value = "销售区域")
    private String saleArea;

    @ApiModelProperty(value = "原始版本")
    private String initVersion;

    @ApiModelProperty(value = "零件列表", required = true)
    private List<FotaCarDeviceItemInfoDto> vehDevices;

    @ApiModelProperty(value = "零件id列表", required = true)
    private List<String> deviceIds;

    /**
     * 操作类型：1=新增，2=更新，3=删除
     */
    private Integer action;
}
