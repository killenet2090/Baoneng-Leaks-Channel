package com.bnmotor.icv.tsp.operation.veh.model.response;

import lombok.Data;

import java.util.Date;

/**
 * @author zhoulong1
 * @ClassName: VehicleLicenseVo
 * @Description: 车辆上牌信息
 * @since: 2020/7/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleLicenseVo {
    /**
     * 车管所名称
     */
    private String vehicleAdministrativeOffice="龙华车管所";
    /**
     * 上牌地址
     */
    private String registrationAddress="深圳";
    /**
     * 注册详细地址
     */
    private String registrationDetailAdress="深圳市龙华区宝能科技园";
    /**
     * 上牌日期
     */
    private Date registrationDate;
   /**
     * 车牌号
     */
    private String drivingLicPlate="粤B890983";
    /**
     * 车牌颜色
     */
    private String drivingLicColor="red";
    /**
     * 车辆照片
     */
    private String vehiclePhotoUrl="sus/sjklr/t.html";

}
