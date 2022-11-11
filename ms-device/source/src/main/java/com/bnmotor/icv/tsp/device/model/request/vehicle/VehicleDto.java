package com.bnmotor.icv.tsp.device.model.request.vehicle;

import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: VehicleDto
 * @Description: 车辆设备信息新增请求DTO
 * @author: qiqi1
 * @date: 2020/8/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class VehicleDto implements Serializable {
    private static final long serialVersionUID = 3356815490149109147L;
    /**
     * 车架号
     */
    @NotEmpty(message = "车架号不能为空！")
    @ApiModelProperty("车架号")
    private String vin;
    /**
     * 车型名称
     */
    @NotEmpty(message = "车型名称不能为空！")
    @ApiModelProperty("车型名称")
    private String vehModelName;
    /**
     * 年款名称
     */
    @NotEmpty(message = "年款名称不能为空！")
    @ApiModelProperty("年款名称")
    private String yearStyleName;
    /**
     * 配置名称
     */
    @NotEmpty(message = "配置名称不能为空！")
    @ApiModelProperty("配置名称")
    private String configName;
    /**
     * 颜色名称
     */
    @ApiModelProperty("颜色名称")
    private String colorName;

    /**
     * 发动机号
     */
    @NotEmpty(message = "发动机号不能为空！")
    @ApiModelProperty("发动机号")
    private String engineNo;
    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batchNo;
    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    private String drivingLicPlate;

    /**
     * 生产商
     */
    @ApiModelProperty("生产商")
    private String manufacturer;

    /**
     * 生产日期
     */
    @ApiModelProperty("生产日期：格式：yyyy/MM/dd")
    private String productTime;

    /**
     * 下线日期
     */
    @ApiModelProperty("下线日期：格式：yyyy/MM/dd")
    private String downlineDate;

    /**
     * 合格证日期
     */
    @ApiModelProperty("合格证日期：格式：yyyy/MM/dd")
    private String certificateDate;
    /**
     * 出厂日期
     */
    @ApiModelProperty("出厂日期：格式：yyyy/MM/dd")
    private String outFactoryTime;
    /**
     * 发布日期
     */
    @ApiModelProperty("发布日期：格式：yyyy/MM/dd")
    private String publishDate;

    @ApiModelProperty("车辆的设备信息列表")
    private List<VehDeviceDto> deviceInfoList;
}
