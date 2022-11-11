package com.bnmotor.icv.tsp.operation.veh.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhoulong1
 * @ClassName: VehicleDetailVo
 * @Description: 车辆详情返回对象
 * @since: 2020/7/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleDetailVo {
    /**
     * 车辆vin
     */
    private String vin;
    /**
     * 车辆生产厂商
     */
    private String manufacturer;
    /**
     * 车辆型号
     */
    private String vehModel;
    /**
     * 车辆类型
     */
    private String vehType;
    /**
     * 车牌号
     */
    private String drivingLicPlate;
    /**
     * 车辆颜色
     */
    private String color;
    /**
     * 发动机号
     */
    private String engineNo;
    /**
     * 下线日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date downlineDate;
    /**
     * 出厂日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date outFactoryTime;
    /**
     * 设备绑定信息
     */
    private List<DeviceBindVo> binds;
    /**
     * 设备信息
     */
    private List<VehicleDeviceVo> devices;
    /**
     * 车辆标签
     */
    private List<String> labels;
    /**
     * 备注
     */
    private String remark;

    /**
     * 车主信息
     */
    private MgtUserVo userVo;
    /**
     * 绑定级MNO信息
     */
    private List<VehicleBindVo> vehicleBindVos;
    /**
     * 销售信息
     */
    private VehicleSalesVo vehicleSalesVo;
    /**
     * 车辆上牌信息
     */
    private VehicleLicenseVo vehicleLicenseVo;
    /**
     * 车辆保修信息
     */
    private VehicleWarranty vehicleWarranty;

}