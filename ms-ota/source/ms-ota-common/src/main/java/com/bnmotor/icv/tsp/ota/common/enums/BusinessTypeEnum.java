package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Objects;

/**
 * @ClassName: BusinessTypeEnum
 * @Description:  OTA消息枚举类型
 * @author: xuxiaochang1
 * @date: 2020/9/3 10:21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum BusinessTypeEnum {
    /**
     * 上行消息=版本检查
     */
    OTA_UP_VERSION_CHECK(1, "版本检查"),

    /**
     * 上行消息=升级(下载)确认请求
     */
    OTA_UP_DOWNLOAD_VERIFY_REQ(2, "升级(下载)确认请求"),

    /**
     * 上行消息=升级(下载)确认结果
     */
    OTA_UP_DOWNLOAD_VERIFY_RESULT(3, "升级(下载)确认结果"),

    /**
     * 上行消息=下载过程汇报
     */
    OTA_UP_DOWNLOAD_PROCESS(4, "下载过程汇报"),

    /**
     * 上行消息=升级(安装)确认请求
     */
    OTA_UP_INSTALLED_VERIFY_REQ(5, "升级(安装)确认请求"),

    /**
     * 上行消息=升级(安装)确认结果
     */
    OTA_UP_INSTALLED_VERIFY_RESULT(6, "升级(安装)确认结果"),

    /**
     * 上行消息=升级过程汇报
     */
    OTA_UP_INSTALLED_PROCESS(7, "升级过程汇报"),

    /**
     * 上行消息=升级结果汇报
     */
    OTA_UP_UPGRADE_RESULT(8, "升级结果汇报"),

    /**
     * 上行消息=ECU信息上报到OTA云端
     */
    OTA_UP_ECU_INFO_UPLOAD(9, "ECU信息上报到OTA云端"),

    /**
     * 上行消息=从OTA云端获取配置信息
     */
    OTA_UP_CONF_VERSION_FROM_OTA(10, "从OTA云端获取配置信息"),

    /**
     * 上行消息=判断任务有效性上行
     */
    OTA_UP_PLAN_IF_VALID(11, "判断任务有效性上行"),


    /**
     * 下行消息=版本检查响应
     */
    OTA_DOWN_VERSION_CHECK_RESP(101, "版本检查响应"),

    /**
     * 下行消息=升级(下载)确认请求
     */
    OTA_DOWN_DOWNLOAD_VERIFY_RESULT(102, "升级(下载)确认请求"),

    /**
     * 下行消息=新版本安装确认结果
     */
    OTA_DOWN_INSTALL_VERIFY_RESULT(103, "新版本安装确认结果"),

    /**
     * 下行消息=云端发布任务新版本通知
     */
    OTA_DOWN_VERSION_CHECK_FROM_OTA(104, "云端发布任务新版本通知"),

    /**
     * 下行消息=APP新版本检查请求
     */
    OTA_DOWN_VERSION_CHECK_FROM_APP(105, "APP新版本检查请求"),

    /**
     * 下行消息=云端固件升级配置清单下发
     */
    OTA_DOWN_CONF_VERSION_FROM_OTA(106, "远端固件升级配置清单下发"),

    /**
     * 下行消息=取消预约安装
     */
    OTA_DOWN_CANCEL_INSTALLED_BOOKED(107, "取消预约安装"),

    /**
     * 下行消息=判断任务有效性下行
     */
    OTA_DOWN_PLAN_IF_VALID(111, "判断任务有效性下行"),
    ;

    @Getter
    private int type;
    @Getter
    private String desc;

    /**
     * 私有构造函数
     * @param type
     * @param desc
     */
    BusinessTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 是否包含该类型枚举
     * @param type
     * @return
     */
    public static boolean contains(int type) {
        return Objects.nonNull(getByType(type));
    }

    /**
     * 根据类型获取枚举
     * @param type
     * @return
     */
    public static BusinessTypeEnum getByType(int type) {
        return EnumSet.allOf(BusinessTypeEnum.class).stream().filter((item) -> item.type == type).findFirst().orElse(null);
    }
}
