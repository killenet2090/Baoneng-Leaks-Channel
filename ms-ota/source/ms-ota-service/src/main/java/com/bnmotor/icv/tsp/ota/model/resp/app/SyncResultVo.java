package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: SyncResultVo
 * @Description:  同步响应结果
 * @author: xuxiaochang1
 * @date: 2020/9/1 17:05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class SyncResultVo {
    @ApiModelProperty(value = "请求OTA云端是否成功:1=云端接收请求成功（需要异步等待推送消息结果）,2=已经进入到升级后续阶段，不需要重复操作(同步接口), 3=云端接收请求成功（任务失效）", example = "1", required = true)
    private Integer success;

    @ApiModelProperty(value = "提示信息", example = "", required = true)
    private String msg;

    private Object content;

    /*public static SyncResultVo success(){
        return SyncResultVo.builder().success(1).build();
    }

    public static SyncResultVo fail(){
        return SyncResultVo.builder().success(2).build();
    }*/
}
