package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qiqi1
 * @ClassName: VehicleDevicePo
 * @Description: 车辆绑定设备信息 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_device")
public class VehicleDevicePo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Long id;

    /**
     * 项目代码
     */
    private String projectId;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 设备ID当前设备唯一识别号
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 版本号
     */
    private Integer version;

}
