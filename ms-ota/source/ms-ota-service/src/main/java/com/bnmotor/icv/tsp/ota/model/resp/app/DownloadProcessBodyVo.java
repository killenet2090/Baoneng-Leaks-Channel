package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: InstalledProcessBodyVO
 * @Description： 下载进度消息体
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class DownloadProcessBodyVo extends RemoteDownloadCallbackBodyVo{
    @ApiModelProperty(value = "包大小（单位为M）", example = "8", required = true)
    private Long downloadTotal;

    @ApiModelProperty(value = "已经下载的包大小", example = "123456789", required = true)
    private Long downloadFinished;

    @ApiModelProperty(value = "预计剩余下载时长:单位为秒", example = "10", required = true)
    private Long downloadRemainTime;

    @ApiModelProperty(value = "预计剩余下载时长:单位为秒", example = "10", required = true)
    private Integer downloadPercentRate;
}
