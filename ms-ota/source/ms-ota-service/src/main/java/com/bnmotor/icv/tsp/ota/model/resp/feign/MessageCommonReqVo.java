package com.bnmotor.icv.tsp.ota.model.resp.feign;

import com.bnmotor.icv.adam.core.utils.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @ClassName: MessageCommonReqVo
 * @Description: 发送消息请求公共对象
 * @author: zhangjianghua1
 * @date: 2020/9/7
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class MessageCommonReqVo {
    /**
     * API输入参数签名结果
     */
    @NotBlank(message = "参数【sign】不能为空")
    private String sign;
    /**
     * 时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2015-01-01 12:00:00。淘宝API服务端允许客户端请求最大时间误差为10分钟
     */
    @NotBlank(message = "-参数【timestamp】不能为空")
    @Pattern(regexp = DateUtil.TIME_MASK_DEFAULT, message = "参数【timestamp】格式不正确，正确格式：yyyy-MM-dd HH:mm:ss")
    private String timestamp;
    /**
     * 响应格式。默认为json格式，可选值：xml，json。
     */
    private String format="json";
}
