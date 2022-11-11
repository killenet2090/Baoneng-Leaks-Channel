package com.bnmotor.icv.tsp.device.model.request.vehDevice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * @ClassName: VehDeviceDto
 * @Description: 车辆信息-零部件信息-新增请求DTO
 * @author: qiqi1
 * @date: 2020/8/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class VehDeviceDto implements Serializable {
    private static final long serialVersionUID = -251117587652626531L;
    @ApiModelProperty("设备ID当前设备唯一识别号imsi")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备类型：1-TBOX ，2-xx")
    private Integer deviceType;

    @ApiModelProperty("零件型号")
    private String deviceModel;

    @ApiModelProperty("硬件版本号")
    private String hardwareVersion;

    @ApiModelProperty("初始化软件版本号")
    private String softwareVersion;

    @ApiModelProperty("绑定状态：0-未绑定；1-已绑定")
    private Integer bindStatus;

    @ApiModelProperty("设备批次号")
    private String batchNo;

    @ApiModelProperty("SIM电话号码")
    private String simPhone;

    @ApiModelProperty("网络运营商  1: 中国移动, 2: 中国联通, 3: 中国电信")
    private Integer networkOperator;

    @ApiModelProperty("移动设备识别码")
    private String imei;

    @ApiModelProperty("国际移动用户识别码")
    private String imsi;

    @ApiModelProperty("sim卡号")
    private String iccid;

    @ApiModelProperty("SIM卡状态：0-已开户， 1-已激活， 2-已封存， 3-已废弃， 4-重新激活")
    private Integer simStatus;

    @ApiModelProperty("激活状态：1-成功， 2-失败，3-进行中")
    private Integer enrollStatus;

    @ApiModelProperty("激活时间：格式 - yyyy/MM/dd")
    private String enrollTime;

    @ApiModelProperty("sim卡绑定时间：格式 - yyyy/MM/dd")
    private String simBindTime;

    @ApiModelProperty("生产序列号")
    private String productSn;

    @ApiModelProperty("生产时间：格式 - yyyy/MM/dd")
    private String productTime;

    @ApiModelProperty("出厂时间：格式 - yyyy/MM/dd")
    private String outFactoryTime;

    @ApiModelProperty("供应商名称")
    private String supplierName;
}
