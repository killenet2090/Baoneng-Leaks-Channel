package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DownloadProcessVO
 * @Description： 新版本检查对应结果
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class RemoteDownloadCallbackBodyVo extends BaseAppBodyVo{
    @ApiModelProperty(value = "下载阶段：1=TBOX端对远程下载操作的确认结果，2=TBOX端下载成功，3=TBOX端下载失败, 4=下载中", example = "1", required = true)
    private Integer downloadProcessType;
}
