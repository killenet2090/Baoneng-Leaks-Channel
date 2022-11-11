package com.bnmotor.icv.tsp.device.common;

import java.time.format.DateTimeFormatter;

/**
 * @ClassName: Constant
 * @Description: 常量类
 * @author: zhangwei2
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface Constant {
    /**
     * 日期格式
     */
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 日期格式
     */
    DateTimeFormatter DATATIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");

    /**
     * uid常量
     */
    String UID = "uid";

    /**
     * 车机激活令牌名称qrCodeToken
     */
    String QR_CODE_TOKEN = "qrcodeKey";

    /**
     * uid常量
     */
    String USER_ID = "userId";
    /**
     * OSS Bucket
     */
    String OSS_BUCKET = "tsp-device";
    /**
     * OSS Group---行驶证
     */
    String OSS_GROUP_DRIVING_LICENSE = "drivingLicense";
    /**
     * OSS Group---机动车销售发票
     */
    String OSS_GROUP_VEHICLE_INVOICE = "vehicleInvoice";

    String VEHICLE = "vehicle";
    /**
     * 品牌
     */
    String BRAND = "brand";
    /**
     * 车型
     */
    String SERIES = "series";
    /**
     * 车型
     */
    String MODEL = "model";
    /**
     * 年款
     */
    String YEAL = "yealStyle";
    /**
     * 配置
     */
    String CONFIG = "config";

    String SYSTEM = "System";

    /**
     * redis 车机激活码 activate-qrcode常量
     */
    String CACHE_KEY_ACTIVATE_QRCODE_NAME = "activate-qrcode";
    /**
     * redis 零件型号缓存
     */
    String CACHE_KEY_DEVICE_MODEL_INFO = "device-model-info";
    /**
     * 车辆缓存key
     */
    String CACHE_KEY_VEHICLE = "vehicle";
    /**
     * reids sim卡信息缓存
     */
    String CACHE_SIM = "sim";
    /**
     * redis device信息缓存
     */
    String CACHE_DEVICE = "device";

    /**
     * &符常量
     */
    String AND_CHAR = "&";
    /**
     * =符常量
     */
    String EQUAL_CHAR = "=";
    /**
     * ?符常量
     */
    String ASK_CHAR = "?";
    /**
     * 车机激活二维码内容前缀
     */
    String QR_CODE_CONTENT_PREFIX = "bnqrcode:/";
    /**
     * vin常量
     */
    String VIN = "vin";
    /**
     * deviceId常量
     */
    String DEVICE_ID = "deviceId";
}
