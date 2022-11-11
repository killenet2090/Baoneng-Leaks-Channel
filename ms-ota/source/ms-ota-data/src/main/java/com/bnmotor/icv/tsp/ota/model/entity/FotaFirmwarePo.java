package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaFirmwarePo
 * @Description: OTA固件信息
定义各个零部件需要支持的OTA升级软件

OTA软升级需要用户的下载 实体类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_firmware")
@ApiModel(value="FotaFirmwarePo对象", description="OTA固件信息定义各个零部件需要支持的OTA升级软件 OTA软升级需要用户的下载")
public class FotaFirmwarePo extends BasePo {
    private static final long serialVersionUID = 2813446982743673156L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "ID字符串")
    @TableField(exist = false)
    private String idStr;

    @ApiModelProperty(value = "项目ID多租户，不同的租户具有不同的项目ID", example = "guanzhi001", required = true)
    private String projectId;

    @ApiModelProperty(value = "设备树对应的节点Id", example = "1000001")
    private Long treeNodeId;

    @TableField(exist = false)
    @ApiModelProperty(value = "设备树对应的节点Id", example = "1000001")
    private String treeNodeIdStr;

    /*@ApiModelProperty(value = "设备树对应的父节点Id", example = "1000001")
    @TableField(exist = false)
    private Long parentTreeNodeId;*/

    @ApiModelProperty(value = "零件代码区分不同零件的代号", required = true, example = "cCode1")
    private String componentCode;

    @ApiModelProperty(value = "零件型号")
    private String componentModel;

    @ApiModelProperty(value = "零件名称", required = true, example = "TCU")
    private String componentName;

    @ApiModelProperty(value = "升级渠道定义当前软解借助什么渠道升级:０-tbox，1-车机......", example = "0")
    private String otaChannel;

    @ApiModelProperty(value = "固件代码固件代码由OTA平台同终端提前约定", required = true, example = "fCode1")
    private String firmwareCode;

    @ApiModelProperty(value = "固件名称", required = true, example = "车机动力控制器")
    private String firmwareName;

    @ApiModelProperty(value = "描述软件的作用，目的等介绍说明", example = "这个固件并没什么卵用")
    private String description;

    @ApiModelProperty(value = "升级模式(区分于软件中的升级模式)：0-强制静默升级，1-非静默升级", example = "0", required = true)
    private Integer updateModel;

    @ApiModelProperty(value = "是否通知到手机：0-否，1-是", example = "1")
    @JsonIgnore
    private Integer notifiedToMobile;

    @ApiModelProperty(value = "升级模式(按升级包的形式来看)软件的性质决定了该软件适合采用怎么样的升级模式0 - 全量升级，该模式下，只能通过下载全量包升级1－补丁升级，该模式下，可以通过全量升级和补丁升级2－差分升级，该模式下，可以通过全量升级和差分包升级", required = true, example="0")
    private Integer packageModel;

    @ApiModelProperty(value = "升级包加密算法采用对称加密，对全量包，补丁包或差分包进行加密的算法空代表无需加密，其他为具体加密方法目前支撑：AES-CBC-128", required = false)
    @JsonIgnore
    private String encryptAlg;

    @ApiModelProperty(value = "打包算法指软件在发布为软件包时采用的打包算法后台再需要生成差分前，按此算法解包后，再生产差分空代表无需解包targzip", required = false)
    @JsonIgnore
    private String packageAlg;

    @ApiModelProperty(value = "APN通道期望该版本通过该通道升级：1-apn1，2-apn2", required = false)
    private Integer apnChannel;

    @ApiModelProperty(value = "升级条件表达式采用ognl表达式表达，终端需要将变量替换为值传入表达式中计算ognl表达式值：true/false, 以判断终端是否满足条件", required = false)
    @JsonIgnore
    private String otaCondExpress;

    @ApiModelProperty(value = "诊断ID代表一个ECU刷新通讯时用到的ID", example = "733", required = true)
    private String diagnose;
    
    @ApiModelProperty(value = "响应ID代表一个零件刷写时用到的ID（十进制）", example = "741", required = true)
    /*@TableField(exist = false)*/
    private String responseId;

    @ApiModelProperty(value = "刷写脚本URL", example = "", required = true)
    private String flashScriptUrl;

    @ApiModelProperty(value = "数据版本", example = "")
    @JsonIgnore
    private Integer version;

    @ApiModelProperty(value = "信息采集受控对象", example = "升级对象/单元由谁负责采集其基础、更新信息", required = false)
    private Integer infoCollCtrlObj;

    @ApiModelProperty(value = "下载受控对象", example = "升级对象/单元的软件包由哪个零件进行下载", required = false)
    private Integer dlCtrlObj;

    @ApiModelProperty(value = "信息采集受控对象", example = "升级对象/单元的软件包由哪个零件进行传输", required = false)
    private Integer instTxCtrlObj;

    @ApiModelProperty(value = "安装受控对象", example = "升级对象/单元的软件包由哪个零件负责安装", required = false)
    private Integer instCtrlObj;

    @ApiModelProperty(value = "安装约束（零件需要处于低压或高压条件下才可以安装）", example = "0 低压 1高压", required = false)
    private Integer instCondition;

    @ApiModelProperty(value = "总线类型", example = "1 can节点 2 以太网节点", required = false)
    private Integer busType;

    /*@ApiModelProperty(value = "业务流程脚本，指升级对象进行OTA升级涉及的业务功能流程所需要用到的脚本类型，如A或B或C:0=Default", example = "0", required = true)
    @TableField(exist = false)
    private Integer businessProcScript;

    @ApiModelProperty(value = "指SA算法的类型，由车厂提供总范围，如A或B或C:0=Default", example = "0", required = true)
    @TableField(exist = false)
    private Integer safeAlgorithm;*/

    @ApiModelProperty(value = "业务流程脚本", example = "升级对象信息采集时所需要用到的脚本类型", required = false)
    private Integer infoCollScriptType;

    @ApiModelProperty(value = "指SA算法的类型，由车厂提供总范围，如A或B或C", example = "", required = false)
    private Integer instAlgorithmType;

    @ApiModelProperty(value = "安装脚本地址", example = "升级对象刷写时所需使用的安装脚本地址", required = false)
    private String instScriptUrl;
}
