package com.bnmotor.icv.tsp.ble.model.request.feign;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "消息通知对象", description = "消息通知对象")
public class BleNotification {
    /**
     * 车辆ID
     */
    private String deviceId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 车牌号
     */
    private String licensePlateNumber;
    /**
     * 模板类别
     */
    private Long categoryId;
    /**
     * 模板ID
     */
    private Long templateId;
    /**
     * 用户ID
     */
    private Long   userId;
    /**
     * 极光推送ID
     */
    private String pushId;
    /**
     * 蓝牙钥匙ID
     */
    private Long   bleId;
    /**
     * 车牌
     */
    private String brand;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 命令
     */
    private Integer cmd;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 失效时间
     */
    private String expireDate;
}
