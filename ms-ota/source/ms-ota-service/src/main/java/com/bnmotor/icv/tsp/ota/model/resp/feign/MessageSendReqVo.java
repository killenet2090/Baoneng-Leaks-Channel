package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: MessageSendReqVo
 * @Description: 发送消息请求对象
 * @author: zhangjianghua1
 * @date: 2020/9/7
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class MessageSendReqVo {

    /**
     * 消息接入ID
     */
    private String id;
    /**
     * 消息模板_ID
     */
    private Long templateId;

    /**
     * 消息分类
     */
    private Long categoryId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容。
     如果消息模板id不为空，则消息内容为参数内容
     如果消息模板id为空，则存放直接消息内容
     */
    private String content;

    /**
     * 消息内容类型
     1. 文本消息
     2. 图文消息
     3. 链接消息
     4. 语音消息
     */
    private Integer contentType;

    /**
     * 消息图片
     */
    private String image;

    /**
     * H5 地址
     */
    private String url;

    /**
     * 推送者
     */
    private String sender;

    /**
     * 发送目标类型:
     1. 用户
     2. 设备
     */
    private Integer targetType;

    /**
     * 消息状态
     1. 未发送
     2. 发送中
     3. 终止发送
     4. 发送完成
     5. 已过期
     */
    private Integer status;

    /**
     * 开始发送时间
     */
    private LocalDateTime sendStartTime;

    /**
     * 发送完成时间
     */
    private LocalDateTime sendEndTime;

    /**
     * 是否立即发送
     */
    private Boolean immediately;

    private String cron;

    /**
     * 消息发送有效截止时间
     */
    private LocalDateTime effectiveDeadline;

    /**
     * 送达回执回调
     */
    private String returnReceiptCallback;

    /**
     * 跳转参数
     */
    private String actionParams;

    private String action;

    /**
     * 推送目标平台
     */
    private List<MessageTargetPlatformReqVo> targetPlatforms;
}
