package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: DevicePo
 * @Description: 设备基本信息 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_device")
public class DevicePo extends BasePo {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 项目代码
     */
    private String projectId;
    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 零件型号
     */
    private String deviceModel;

    /**
     * 设备ID当前设备唯一识别号
     */
    private String deviceId;

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
     * 批次号
     */
    private String batchNo;

    /**
     * 激活状态1 成功、 2 失败、 3 进行中
     */
    private Integer enrollStatus;

    /**
     * 激活时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date enrollTime;

    /**
     * iccid
     */
    private String iccid;

    /**
     * 生产序列号
     */
    private String productSn;

    /**
     * 生产时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime productTime;

    /**
     * 出厂时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime outFactoryTime;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 版本号
     */
    private Integer version;
}
