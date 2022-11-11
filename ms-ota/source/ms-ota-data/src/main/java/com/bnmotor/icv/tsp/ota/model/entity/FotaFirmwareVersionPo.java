package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: FotaFirmwareVersionDo
 * @Description: 软件版本,即软件坂本树上的一个节点
定义软件所生成的各个不同的版本 实体类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_firmware_version")
@ApiModel(value="FotaFirmwareVersionDo对象", description="软件版本,即软件坂本树上的一个节点定义软件所生成的各个不同的版本")
public class FotaFirmwareVersionPo extends BasePo {

    private static final long serialVersionUID = 5654770864112261110L;
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "ID字符串类型")
    @TableField(exist=false)
    private String idStr;

    @ApiModelProperty(value = "项目ID", example = "guanzhi001", required = true)
    private String projectId;

    @ApiModelProperty(value = "固件ID该版本对应的软件", example = "10000", required = true)
    private Long firmwareId;

    @ApiModelProperty(value = "固件ID该版本对应的软件字符串类型", example = "firmwareId001")
    @TableField(exist=false)
    private String firmwareIdStr;

    @ApiModelProperty(value = "固件名称", example = "firmwareVersionName001")
    @TableField(exist=false)
    private String firmwareName;

    @ApiModelProperty(value = "固件代码", example = "firmwareVersionNo001")
    @TableField(exist=false)
    private String firmwareCode;

    @ApiModelProperty(value = "固件版本名称", example = "firmwareVersionName001")
    private String firmwareVersionName;

    @ApiModelProperty(value = "固件版本号", example = "firmwareVersionNo001", required = true)
    private String firmwareVersionNo;

    @ApiModelProperty(value = "版本号数字码用于版本大小比较暂时由页面输入", example = "5")
    private Long versionDigitalNo;

    @ApiModelProperty(value = "父版本ID")
    private Long parentVersionId;

    @ApiModelProperty(value = "适用的固件版本集合(分隔符是分号)固件版本号;固件版本号", example = "firmwareVersionId001;firmwareVersionId002;", required = true)
    private String appliedFirmwareVersion;

    @ApiModelProperty(value = "适用的硬件版本集合(分隔符是分号)硬件版本号;硬件版本号", example = "boot001;boot002;", required = true)
    private String appliedHardwareVersion;

    @ApiModelProperty(value = "发布说明", example = "反正我发布了，用不用由你")
    private String releaseNotes;

    @ApiModelProperty(value = "全量包ID")
    private Long fullPkgId;

    @ApiModelProperty(value = "是否已上传了升级包:0=未上传，1=已上传")
    @TableField(exist=false)
    private Integer pkgUpload;

    @ApiModelProperty(value = "状态：0-新建，1-待审核，2-审核通过，3-审核不通过，4-已发布", example = "2")
    private Integer status;

    @ApiModelProperty(value = "是否强制全量升级：0-否，1-是。如果是的话，版本检查总是返回全量包, 必须采用全量升级", example = "1", required = true)
    private Integer isForceFullUpdate;

    @ApiModelProperty(value = "发布用户id", example = "sunwukong")
    private String releaseUser;

    @ApiModelProperty(value = "发布日期", example = "2020-06-22 12:00:00")
    private Date releaseDt;

    @ApiModelProperty(value = "审核用户id", example = "sunwukong")
    private String approvalUser;

    @ApiModelProperty(value = "审核日期", example = "2020-06-12 12:00:00")
    private Date approvalDt;

    @ApiModelProperty(value = "审核意见")
    private String approvalComment;

    @ApiModelProperty(value = "数据更新版本", example = "2")
    private Integer version;

    @ApiModelProperty(value = "依赖版本信息列表")
    @TableField(exist=false)
    private List<FotaFirmwareVersionDependencePo> fotaFirmwareVersionDependenceDoList;

    @ApiModelProperty(value = "零件名称(用于前端页面显示)", example = "震动器")
    @TableField(exist = false)
    private String componentCode;

    @ApiModelProperty(value = "零件代码(用于前端页面显示)", example = "TUP")
    @TableField(exist = false)
    private String componentName;

    @ApiModelProperty(value = "固件的升级模式：0=全量,1=补丁,2=差分", example = "1")
    @TableField(exist = false)
    private Integer packageModel;
}
