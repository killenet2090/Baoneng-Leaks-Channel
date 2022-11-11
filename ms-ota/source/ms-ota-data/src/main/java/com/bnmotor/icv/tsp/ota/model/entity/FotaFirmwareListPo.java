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
 * @ClassName: FotaFirmwareListPo
 * @Description: OTA升级固件清单
 实体类
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_firmware_list")
@ApiModel(value="FotaFirmwareListPo对象", description="OTA升级固件清单 ")
public class FotaFirmwareListPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目Id")
    private String projectId;

    @ApiModelProperty(value = "所属升级对象ID")
    private Long otaObjectId;

    @ApiModelProperty(value = "零件号对应设备的唯一识别号，比如pdsn，tbox id,iccid等")
    private String componentId;

    @ApiModelProperty(value = "零件批次号")
    private String batchNo;

    @ApiModelProperty(value = "零件序列号")
    private String serialNo;

    @ApiModelProperty(value = "零件硬件版本")
    private String hardwareVersion;

    @ApiModelProperty(value = "生产日期")
    private Date madeDate;

    @ApiModelProperty(value = "软件ID")
    private Long firmwareId;

    @ApiModelProperty(value = "初始化固件版本号")
    private String initFirmwareVersion;

    @ApiModelProperty(value = "当前运行软件版本")
    private String runningFirmwareVersion;

    @ApiModelProperty(value = "需要升级的软件版本号 双系统升级时，需要在此版本号基础上升级单升级时，该字段即为需要升级的软件版本号")
    private String needUpgradeFirmwareVersion;

    @ApiModelProperty(value = "最后升级时间")
    private Date upgradeTime;

    @ApiModelProperty(value = "数据版本")
    private Integer version;
}
