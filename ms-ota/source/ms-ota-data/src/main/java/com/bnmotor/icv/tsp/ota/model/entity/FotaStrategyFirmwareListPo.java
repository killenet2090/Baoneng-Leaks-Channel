package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaStrategyFirmwareListPo
 * @Description: OTA升级策略-升级ecu固件列表 实体类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_strategy_firmware_list")
public class FotaStrategyFirmwareListPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 策略ID
     */
    private Long otaStrategyId;

    /**
     * 关联的设备零件列表ID
     */
    private Long componentListId;

    /**
     * 软件ID
     */
    private Long firmwareId;

    /**
     * 升级包模式； 1 - 差分升级包， 0 - 全量升级包
     */
    private Integer upgradeMode;

    /**
     * 前置版本Id
     */
    private Long startVersionId;

    /**
     * 目标版本Id
     */
    private Long targetVersionId;

    /**
     * 升级顺序
     */
    private Integer orderNum;

    /**
     * 分组信息
     */
    private Integer groupSeq;

    /**
     * 分组信息
     */
    private Integer dbGroupSeq;

    /**
     * 数据并发控制版本
     */
    private Integer version;

}
