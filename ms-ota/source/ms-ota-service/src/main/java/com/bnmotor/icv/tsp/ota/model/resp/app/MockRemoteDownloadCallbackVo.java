package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DownloadProcessVO
 * @Description： 下载进度汇报
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class MockRemoteDownloadCallbackVo{
    @ApiModelProperty(value = "远程下载TBOX确认异步回调消息", example = "", required = false)
    private DownloadVerfiyResultVo downloadVerfiyResultVo;

    @ApiModelProperty(value = "远程下载最终结果异步回调消息", example = "", required = false)
    private DownloadResultVo downloadResultVo;
}
