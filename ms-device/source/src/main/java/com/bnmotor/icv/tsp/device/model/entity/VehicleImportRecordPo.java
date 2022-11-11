package com.bnmotor.icv.tsp.device.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: VehicleImportRecordPo
 * @Description: 车辆信息 实体类
 * @author zhangwei2
 * @since 2020-11-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_import_record")
public class VehicleImportRecordPo extends BasePo{
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
     * 车型名称
     */
    private String vehModelName;

    /**
     * 年款名称
     */
    private String yearStyleName;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 颜色
     */
    private String colorName;

    /**
     * 发动机号
     */
    private String engineNo;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 生产商
     */
    private String manufacturer;

    /**
     * 生产时间
     */
    private LocalDateTime productTime;

    /**
     * 下线日期
     */
    private LocalDateTime downlineDate;

    /**
     * 合格证日期
     */
    private LocalDateTime certificateDate;

    /**
     * 出厂日期
     */
    private LocalDateTime outFactoryTime;

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
