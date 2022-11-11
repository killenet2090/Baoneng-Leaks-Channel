package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: Vehicle
 * @Description: 车辆数据
 * @author: zhangwei2
 * @date: 2020/11/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class Vehicle {
    /**
     * 车型配置关联关系id
     */
    private Long orgId;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 下线日期
     */
    private String downlineDate;
    /**
     * 车牌号
     */
    private String licenceNo;

    /**
     * 生产批次号
     */
    private String prodBatchNo;
    /**
     * 生产日期
     */
    private String productionDate;
    /**
     * 销售日期
     */
    private String saleArea;

    /**
     * 车辆生命周期
     */
    private Integer vehLifecircle;
    /**
     * 车辆状态
     */
    private Integer vehStatus;

    /**
     * 车辆绑定的设备
     */
    private List<VehicleDevice> vehDevices;

    /**
     * 默认 0-无意义
     */
    private Integer actionSubType = 0;

    public static Vehicle transform(VehiclePo vehiclePo) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vehiclePo.getVin());
        vehicle.setLicenceNo(vehiclePo.getDrivingLicPlate());
        if (vehiclePo.getDownlineDate() != null) {
            vehicle.setDownlineDate(vehiclePo.getDownlineDate().format(Constant.FORMATTER));
        }
        vehicle.setProdBatchNo(vehiclePo.getBatchNo());
        if (vehiclePo.getProductTime() != null) {
            vehicle.setProductionDate(vehiclePo.getProductTime().format(Constant.FORMATTER));
        }
        vehicle.setVehLifecircle(vehiclePo.getVehLifecircle());
        vehicle.setVehStatus(vehiclePo.getVehStatus());
        vehicle.setOrgId(vehiclePo.getOrgId());

        return vehicle;
    }

    public static DataSysnMessage<Vehicle> decorateMsg(Integer actionType, Integer businessType, Integer dataType, Vehicle vehicle) {
        DataSysnMessage<Vehicle> message = new DataSysnMessage();
        message.setType(dataType);
        message.setAction(actionType);
        message.setBusinessType(businessType);
        message.setData(vehicle);
        return message;
    }
}
