package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: TboxUpgradStatusVo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/8/19 14:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class TboxUpgradStatusVo {
    @ApiModelProperty(value = "TBOX升级状态：-1=无新版本(同步)，0=等待发起新版本检查(异步)，1=等待新版本确认(异步)，11=待下载(同步)，12=下载开始(同步)，13=下载中(同步)，14=下载中止(同步)，15=下载等待(同步)，16=TBOX执行下载完成(同步)，17=TBOX执行下载完成，验证成功(同步)，18=TBOX执行下载完成,验证失败(同步),31=待升级，32=开始升级（同步），33=前置条件检查失败（同步），34=HU端触发预升级过程（同步），35=HU端取消升级（同步），36=升级中（同步），37=升级完成成功（同步），38=升级未完成（同步），39=升级失败（同步）", example = "1", required = true)
    private Integer status;

    @ApiModelProperty(value = "TBOX升级状态描述", example = "", required = true)
    private String desc;
}
