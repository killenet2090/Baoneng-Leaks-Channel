package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: VehicleDeviceImportRecordPo
 * @Description: 设备基本信息 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_device_import_record")
public class VehicleDeviceImportRecordPo extends BasePo {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * 任务号
     */
    private String taskNo;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 零件号
     */
    private String deviceModel;

    /**
     * 设备ID，当前设备唯一识别号
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 硬件版本号
     */
    private String hardwareVersion;

    /**
     * 初始化软件版本号
     */
    private String softwareVersion;

    /**
     * 供应商硬件版本号
     */
    private String supplierHardwareVersion;

    /**
     * 供应商软件版本号
     */
    private String supplierSoftwareVersion;

    /**
     * boot版本号
     */
    private String bootVersion;

    /**
     * 生产序列号
     */
    private String productSn;

    /**
     * 生产时间
     */
    private LocalDateTime productTime;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 物联网卡号
     */
    private String iccid;

    /**
     * 供应商
     */
    private String supplierName;

    /**
     * 校验状态0-未校验；1-校验通过；2-校验未通过
     */
    private Integer checkStatus;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 版本号
     */
    private Integer version = 1;

}
