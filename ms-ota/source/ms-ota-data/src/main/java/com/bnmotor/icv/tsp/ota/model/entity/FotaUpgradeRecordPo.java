package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: FotaUpgradeRecordPo
 * @Description: OTA升级记录指的是升级对象针对每一个升级软件的一次升级记录
升级记录记录的是升级计划和任务的实际执行情况
 实体类
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_upgrade_record")
@ApiModel(value="FotaUpgradeRecordPo对象", description="OTA升级记录指的是升级对象针对每一个升级软件的一次升级记录 升级记录记录的是升级计划和任务的实际执行情况")
public class FotaUpgradeRecordPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "升级任务明细ID该字段，仅针对计划升级才需要保存任务id")
    private Long otaTaskDetailId;

    @ApiModelProperty(value = "升级对象类型支持多类型升级对象的升级，目前只有车辆升级0 - 车辆升级")
    private Integer objectType;

    @ApiModelProperty(value = "对象ID升级对象的唯一识别ID")
    private String objectId;

    @ApiModelProperty(value = "升级对象固件ID指向升级对象固件清单")
    private Long fotaObjectFirmwareId;

    @ApiModelProperty(value = "升级固件ID")
    private Long firmwareId;

    @ApiModelProperty(value = "升级包ID")
    private Long upgradePkgId;

    @ApiModelProperty(value = "升级前版本ID")
    private Long beforeUpgradeVersionId;

    @ApiModelProperty(value = "升级前版本号")
    private String beforeUpgradeVersion;

    @ApiModelProperty(value = "升级后版本ID")
    private Long afterUpgradeVersionId;

    @ApiModelProperty(value = "升级后版本号")
    private String upgradedVersion;

    @ApiModelProperty(value = "整个OTA开始时间从收到通知开始或从版本检查更新开始")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date finishTime;

    @ApiModelProperty(value = "升级状态0 - 失败1 - 成功2 - 升级中")
    private Integer status;

    @ApiModelProperty(value = "当前所处升级过程代码")
    private String curUpgradeProcCode;

    @ApiModelProperty(value = "备注")
    private String remarks;

    private Integer version;

}
