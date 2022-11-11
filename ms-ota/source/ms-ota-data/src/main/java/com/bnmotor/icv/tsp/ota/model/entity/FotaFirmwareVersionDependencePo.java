package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaFirmwareVersionDependenceDo
 * @Description: 软件版本依赖 实体类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_firmware_version_dependence")
@ApiModel(value="FotaFirmwareVersionDependenceDo对象", description="软件版本依赖")
public class FotaFirmwareVersionDependencePo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "projectID")
    private String projectId;

    @ApiModelProperty(value = "ID字符串")
    @TableField(exist = false)
    private String idStr;

    @ApiModelProperty(value = "固件ID")
    private Long firmwareId;

    @ApiModelProperty(value = "固件ID字符串")
    @TableField(exist = false)
    private String firmwareIdStr;

    @ApiModelProperty(value = "固件版本ID")
    private Long firmwareVersionId;

    @ApiModelProperty(value = "固件版本ID字符串")
    @TableField(exist = false)
    private String firmwareVersionIdStr;

    @ApiModelProperty(value = "依赖固件ID")
    private Long dependFirmwareId;

    @ApiModelProperty(value = "依赖固件ID字符串")
    @TableField(exist = false)
    private String dependFirmwareIdStr;

    @ApiModelProperty(value = "依赖固件版本ID")
    private Long dependFirmwareVersionId;

    @ApiModelProperty(value = "依赖固件版本ID")
    @TableField(exist = false)
    private String dependFirmwareVersionIdStr;

    @ApiModelProperty(value = "升级顺序1,2,...从小到大依次升级")
    private Integer upgradeSequence;

    @ApiModelProperty(value = "依赖错误处理机制当该依赖升级出错后该怎么处理:0-停止后续升级，已升级的保持;1-忽略当前依赖错误，继续下一个依赖，但是终止当前升级,2-终止后续升级，并回滚所有已发送的升级")
    private String dependErrorHandMechanism;

    @ApiModelProperty(value = "依赖版本对应的固件名称")
    @TableField(exist = false)
    private String dependFirmwareName;

    @ApiModelProperty(value = "依赖版本对应固件号")
    @TableField(exist = false)
    private String dependFirmwareCode;

    @ApiModelProperty(value = "依赖版本对应的版本号")
    @TableField(exist = false)
    private String dependFirmwareVersionNo;

    @ApiModelProperty(value = "依赖版本对应的固件零件代码")
    @TableField(exist = false)
    private String dependComponentCode;

    @ApiModelProperty(value = "依赖版本对应的固件零件名称")
    @TableField(exist = false)
    private String dependComponentName;

    @ApiModelProperty(value = "数据版本，用于并发控制")
    private Integer version;
}
