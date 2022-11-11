package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: VehicleSettingRecordPo
 * @Description: 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_setting_record")
public class VehicleSettingRecordPo extends BasePo {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Long id;

    /**
     * 业务id
     */
    private Integer businessId;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户推送rid
     */
    private String registrationId;

    /**
     * 操作类型；1-设置临客模式；2-取消临客模式
     */
    private Integer opType;

    /**
     * 响应结果状态 10-等待响应 15-执行中 20-成功 30-失败 99-响应超时
     */
    private Integer respStatus;

    /**
     * 响应时间
     */
    private Date respTime;

    /**
     * 时长(ms)
     */
    private Integer spendTime;

    /**
     * 版本号
     */
    private Integer version;
}
