package com.bnmotor.icv.tsp.ota.model.req.tbox;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: LogFileUploadDto
 * @Description:    过程和日志记录上传参数
 * @author: xuxiaochang1
 * @date: 2020/9/15 9:59
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class LogFileUploadDto {
    @ApiModelProperty(value = "升级事务Id", example = "0", dataType = "Long")
    private Long transId;
    @ApiModelProperty(value = "升级任务Id", example = "0", dataType = "Long")
    private Long taskId;
}
