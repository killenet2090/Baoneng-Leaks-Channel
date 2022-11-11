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

import java.time.LocalDateTime;

/**
 * @ClassName: FotaVersionCheckVerifyPo
 * @Description: OTA升级版本结果确认表 实体类
 * @author xxc
 * @since 2020-07-15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_version_check_verify")
@ApiModel(value="FotaVersionCheckVerifyPo对象", description="OTA升级版本结果确认表")
public class FotaVersionCheckVerifyPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "升级对象Id")
    private Long objectId;

    @ApiModelProperty(value = "升级任务对象Id")
    private Long otaPlanObjectId;

    @ApiModelProperty(value = "升级任务Id")
    private Long otaPlanId;

    @ApiModelProperty(value = "客户端请求记录Id")
    private Long checkReqId;

    @ApiModelProperty(value = "车辆Id,默认为vin码，冗余字段")
    private String vin;

    @ApiModelProperty(value = "来源类型:0=客户端请求类型，保留")
    private Integer sourceType;

    @ApiModelProperty(value = "升级确认状态:1=已确认，0=未确认")
    private Integer checkAckVerifyStatus;

    @ApiModelProperty(value = "升级确认请求时间(等于升级确认请求时间)")
    private LocalDateTime checkAckVerifyTime;

    @ApiModelProperty(value = "升级确认请求时间")
    private LocalDateTime upgradeReqTime;

    @ApiModelProperty(value = "升级确认消息状态:0=放弃升级，1=确认升级")
    private Integer upgradeVerifyStatus;

    @ApiModelProperty(value = "升级确认来源类型：0=车机，1=App")
    private Integer upgradeVerifySource;

    @ApiModelProperty(value = "升级确认（是否立即开启下载）：1=立即，2=延迟")
    private Integer upgradeVerifyImmediate;

    @ApiModelProperty(value = "升级确认消息时间")
    private LocalDateTime upgradeVerifyTime;

    @ApiModelProperty(value = "升级确认消息下发状态:0=未下发，1=已下发")
    private Integer upgradeVerifyPush;

    @ApiModelProperty(value = "下载百分比")
    private Integer downloadPercentRate;

    @ApiModelProperty(value = "下载总包大小")
    private Long downloadFullSize;

    @ApiModelProperty(value = "下载完成大小")
    private Long downloadFinishedSize;

    @ApiModelProperty(value = "下载花费时间:时间为s")
    private Integer downloadSpendTime;

    @ApiModelProperty(value = "预估剩余时间:时间为s")
    private Integer downloadRemainTime;

    @ApiModelProperty(value = "最后一次下载上报时间")
    private LocalDateTime downloadReportTime;

    @ApiModelProperty(value = "下载完成状态（保留）")
    private Integer downloadStatus;

    @ApiModelProperty(value = "升级确认请求时间")
    private LocalDateTime installedReqTime;

    @ApiModelProperty(value = "升级安装消息确认状态:0=未确认，1=已确认")
    private Integer installedAckVerifyStatus;

    @ApiModelProperty(value = "升级安装消息确认时间")
    private LocalDateTime installedAckVerifyTime;

    @ApiModelProperty(value = "升级安装消息确认状态:0=放弃安装，1=确认安装")
    private Integer installedVerifyStatus;

    @ApiModelProperty(value = "升级安装消息确认来源：0=车机，1=App")
    private Integer installedVerifySource;

    @ApiModelProperty(value = "升级安装消息确认时间")
    private LocalDateTime installedVerifyTime;

    @ApiModelProperty(value = "升级安装预约时间")
    private LocalDateTime installedVerifyBookedTime;

    @ApiModelProperty(value = "升级安装确认消息状态:0=未下发，1=已下发")
    private Integer installedVerifyPush;

    @ApiModelProperty(value = "安装完成百分比")
    private Integer installedPercentRate;

    @ApiModelProperty(value = "安装包总个数")
    private Integer installedTotalNum;

    @ApiModelProperty(value = "安装包当前第几个")
    private Integer installedCurrentIndex;

    @ApiModelProperty(value = "安装花费时间:时间为s")
    private Integer installedSpendTime;

    @ApiModelProperty(value = "安装预估剩余时间:时间为s")
    private Integer installedRemainTime;

    @ApiModelProperty(value = "当前正在安装的固件Id")
    private Long installedFirmwareId;

    @ApiModelProperty(value = "最后一次安装上报时间")
    private LocalDateTime installedReportTime;

    @ApiModelProperty(value = "安装完成状态（保留）")
    private Integer installedStatus;

    @ApiModelProperty(value = "安装结果：0=失败，1=成功")
    private Integer installedCompleteStatus;

    @ApiModelProperty(value = "安装结果上报时间")
    private LocalDateTime installedCompleteTime;

    @ApiModelProperty(value = "安装开始时间")
    private LocalDateTime installedStartTime;

    @ApiModelProperty(value = "安装结束时间")
    private LocalDateTime installedEndTime;

    @ApiModelProperty(value = "数据并发控制字段")
    private Integer version;
    
    @ApiModelProperty(value = "tbox升级日志存储信息")
    private String upgradeLog;

}