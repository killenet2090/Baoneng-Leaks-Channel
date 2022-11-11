package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: BaseAppBodyVo
 * @Description： 响应给APP的对象基类
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class BaseAppBodyVo {
    @ApiModelProperty(value = "升级事务Id", example = "123456789", required = true)
    private Long transId;

    @ApiModelProperty(value = "升级任务Id", example = "123456789", required = true)
    private Long taskId;

    @ApiModelProperty(value = "推送结果码", example = "200", required = true)
    private Integer respCode;

    @ApiModelProperty(value = "异常信息", example = "123456789", required = true)
    private String msg;
}
