package com.bnmotor.icv.tsp.ota.model.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: MessgeCenterMockDto
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/4 16:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class MessgeCenterMockDto {
    private String vin;

    @ApiModelProperty(value = "新版本通知：1303211738509786361, ", example = "1", required = true)
    private Long typeId;

    @ApiModelProperty(value = "如果为安装失败类型：1=安装失败，回滚成功,2=安装失败，回滚失败；如果为下载失败类型：1=下载失败，没有足够空间,2=下载校验包失败；", example = "1")
    private Integer failType;
}
