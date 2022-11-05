package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "消息中心请求对象", description = "消息中心请求对象")
public class MessageCenterDto {
    /**
     * 消息中心ID，用于业务上分批发送同一个消息时，携带该参数。 业务上分批发送消息时，第一次发送不需要携带，并且第一次发送或返回消息中心的id给到业务，后续发送相同消息时携带该参数。
     */
  private  String id;
    /**
     * 消息模板的ID标识，消息中心提供
     */
  private  String templateId;
    /**
     * 消息分类的ID标识，消息中心提供
     */
  private  String categoryId;
    /**
     * 消息标题: 如果传了就已传递为准，如果没传就已消息中心为准。TODO 将来可能移除该字段
     */
  private  String title;
    /**
     * 消息模板需要的参数：JSON格式
     */
  private  String content;
    /**
     * 消息的内容类型：1. 文本消息；2.图文消息；3. 链接消息
     */
  private  Integer contentType;
    /**
     * 消息中图片的url
     */
  private  String image;
    /**
     * 链接消息对应的链接地址
     */
  private  String url;
    /**
     * 消息的发送者， 传递用户ID
     */
  private  String sender;
    /**
     * 目标类型。1：用户，2：设备
     */
  private  Integer targetType;
    /**
     * 是否立即发送，目前除短信验证码意外，其他都不需要立即发送
     */
  private  Boolean immediately;
    /**
     * 预约消息的cron表达式
     */
  private  String cron;
    /**
     * 消息发送的有效期：yyyy-MM-dd HH:mm:ss
     */
  private  String effectiveDeadline;
    /**
     * 推送消息，送到到目的地后，需要消息中心回执给业务的接口
     */
  private  String returnReceiptCallback;

  /**
   * 消息跳转参数
   */
  private  String actionParams;

  private List<TargetPlatforms> targetPlatforms;

}
