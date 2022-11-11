package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author xuxiaochang1
 * @ClassName: FotaFirmwarePkgPo
 * @Description: 升级包信息
 * 包括升级包原始信息以及升级包发布信息 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-06-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_firmware_pkg")
@ApiModel(value = "FotaFirmwarePkgPo对象", description = "升级包信息 包括升级包原始信息以及升级包发布信息")
public class FotaFirmwarePkgPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "升级包类型:0-全量包,1-补丁包,2-差分包")
    private Integer pkgType;

    @ApiModelProperty(value = "原始文件上传记录ID")
    private Long uploadFileId;

    @ApiModelProperty(value = "构建完成后的文件上传记录ID")
    private Long buildUploadFileId;

    @ApiModelProperty(value = "测试报告文件上传记录ID")
    private Long reportUploadFileId;

    @ApiModelProperty(value = "升级包文件名,即升级包上传前自身的文件名，用于显示")
    private String pkgFileName;

    @ApiModelProperty(value = "原始包存储完整路径，包含文件名绝对路径原始包需要存储在分布式存储设备上")
    private String originalPkgFilePath;

    @ApiModelProperty(value = "原始包打包算法来源于软件表中的打包算法")
    private String originalPackageAlg;

    @ApiModelProperty(value = "原始包SHA码")
    private String originalPkgShaCode;

    @ApiModelProperty(value = "原始包大小单位字节")
    private Long originalPkgSize;

    @ApiModelProperty(value = "原始测试报告存储完整路径")
    private String originalReportFilePath;

    @ApiModelProperty(value = "差分包脚本，针对差分包类型有效")
    @TableField(exist = false)
    private String originalDifScriptUrl;
    
    @ApiModelProperty(value = "网关刷新脚本")
    @TableField(exist = false)
    private String flashScriptUrl;

    @ApiModelProperty(value = "包制作状态：0=未开始，1=制作中，2=制作成功，3=制作失败")
    private Integer buildPkgStatus;

    @ApiModelProperty(value = "包制作更新时间")
    private Date buildPkgTime;

    @ApiModelProperty(value = "包制作状态码")
    private Integer buildPkgCode;

    @ApiModelProperty(value = "发布包存储完整路径，包含文件名绝对路径原始包需要存储在分布式存储设备上")
    private String releasePkgFilePath;

    @ApiModelProperty(value = "发布包下载URL")
    private String releasePkgFileDownloadUrl;

    @ApiModelProperty(value = "发布包SHA码")
    private String releasePkgShaCode;

    @ApiModelProperty(value = "发布包加密算法来源于软件表中的加密算法")
    private String releasePkgEncryptAlg;

    @ApiModelProperty(value = "发布包加密密钥升级包加密密钥，由系统随机生成，一个包一个密钥")
    private String releasePkgEncryptSecret;

    @ApiModelProperty(value = "发布包签名算法来源于软件表中的签名算法")
    private String releasePkgSignAlg;

    @ApiModelProperty(value = "升级包签名全量升级包的签名信息，采用服务端私钥签名而成")
    private String releasePkgSign;


    @ApiModelProperty(value = "发布包大小单位字节")
    private Long releasePkgFileSize;

    @ApiModelProperty(value = "升级包碎片数据结构：[N][Index][L][data][L][data][L][data]…… N: 一个字节，代表有多少个碎片	Index: 表示数据在包中的开始位置	L:表示该数据的长度	Data:为具体数据，长度为L上述数据需要经过Base64编码转换为字符格式。")
    private String releasePkgChipInfo;

    @ApiModelProperty(value = "发布包状态:0表示无效1表示有效")
    private String releasePkgStatus;

    @ApiModelProperty(value = "发布包状态描述")
    private String releasePkgStatusMsg;

    @ApiModelProperty(value = "发布包CDN下载地址")
    private String releasePkgCdnDownloadUrl;

    @ApiModelProperty(value = "发布包CDN对象ID")
    private String releasePkgCdnObjId;

    @ApiModelProperty(value = "发布包CDN发布时间")
    private Date releasePkgCdnTime;

    @ApiModelProperty(value = "预估刷写时间")
    private Long estimateFlashTime;

    @ApiModelProperty(value = "数据记录版本号，用于控制并发操作")
    private Integer version;
    
    @ApiModelProperty(value = "0=未开始，1=加密中，2=加密成功，3=加密失败")
    private Integer encryptPkgStatus;
    
    @ApiModelProperty(value = "加密包文件上传id")
    private Long encryptUploadFileId;
}
