package com.bnmotor.icv.tsp.ota.model.req.app;

import com.bnmotor.icv.adam.sdk.ota.domain.DownloadProcessDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: DownloadProcessPushMockDo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/9/14 15:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class DownloadProcessPushMockDo extends PushMockDto{

    @ApiModelProperty(value = "下载阶段：1=TBOX端对远程下载操作的确认结果，2=TBOX端下载成功，3=TBOX端下载失败， 4=下载中", example = "1", required = true)
    private Integer downloadProcessType;

    @ApiModelProperty(value = "下载阶段：102=其他异常，103=空间不足，104=校验失败", example = "1", required = true)
    private Integer failType;

    @ApiModelProperty(value = "是否立即下载：1=立即下载，2=延迟下载", example = "1", required = true)
    private Integer immediateDownload;

}
