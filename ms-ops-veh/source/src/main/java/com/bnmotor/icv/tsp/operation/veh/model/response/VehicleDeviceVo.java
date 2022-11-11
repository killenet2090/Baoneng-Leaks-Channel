package com.bnmotor.icv.tsp.operation.veh.model.response;

import lombok.Data;

/**
 * @author zhoulong1
 * @ClassName: VehicleDeviceVo
 * @Description: 车辆设备信息返回对象
 * @since: 2020/7/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleDeviceVo {
    /**
     * 设备ID
     */
    private String id;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 软件版本号
     */
    private String sorfwareVersion;
    /**
     * 硬件版本号
     */
    private String hardVersion;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 生产序列号
     */
    private String productSn;
    /**
     * sim卡卡号
     */
    private String iccId;
}
