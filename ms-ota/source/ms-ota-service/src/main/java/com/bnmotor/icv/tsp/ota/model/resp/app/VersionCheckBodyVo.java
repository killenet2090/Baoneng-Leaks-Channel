package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VersionCheckBodyVO
 * @Description： 新版本检查对应结果消息体
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class VersionCheckBodyVo extends BaseAppBodyVo{

    @ApiModelProperty(value = "版本检查结果：1=存在新版本（需要异步等待新版本检查相关信息）,2=无新版本（不需异步等待）", example = "1", required = true)
    private Integer checkResult;

    @ApiModelProperty(value = "当前升级状态:0=需要进行版本检查，1=下载中，2=下载成功，未安装，3=下载完成，校验失败, 4=下载成功，预约安装，5=安装中", example = "", required = true)
    private Integer status;

    @ApiModelProperty(value = "当前升级状态:附加信息。status=4（下载成功，预约安装）时，预约安装时间取字段名：installedTime。status=5（安装中）时，当前安装总数据量=installedTotalNum，当前安装为第几个=installedCurrentIndex", example = "", required = true)
    private Object aditionalInfo;

    @ApiModelProperty(value = "当前版本号", example = "V1.0.0", required = true)
    private String currentVersion;

    @ApiModelProperty(value = "TBox端第一次请求获取新版本时，由服务器端生成", example = "10086", required = true)
    private Long transId;

    @ApiModelProperty(value = "云端任务Id", example = "10086", required = true)
    private Long taskId;

    @ApiModelProperty(value = "目标新版本（存在新版本）", example = "V2.0.0", required = true)
    private String targetVersion;

    @ApiModelProperty(value = "新版本提示（存在新版本）", example = "这是一个新版本，刷完后可能会gameover", required = true)
    private String newVersionTips;

    @ApiModelProperty(value = "下载提示（存在新版本）", example = "越下载越嗨皮", required = true)
    private String downloadTips;

    @ApiModelProperty(value = "免责申明（存在新版本）", example = "我可不承担任何可能成为砖头的锅", required = true)
    private String disclaimer;

    @ApiModelProperty(value = "任务提示（存在新版本）", example = "这是一个升级任务提示", required = true)
    private String taskTips;

    @ApiModelProperty(value = "升级包大小：单位字节", example = "1000", required = true)
    private Long fullPkgFileSize;

    @ApiModelProperty(value = "预估升级升级时间", example = "1000", required = true)
    private Long intalledRemainTime;
}
