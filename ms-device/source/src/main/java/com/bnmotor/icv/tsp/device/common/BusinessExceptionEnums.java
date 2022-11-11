package com.bnmotor.icv.tsp.device.common;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @author zhangjianghua1
 * @ClassName: BusinessExceptionEnums
 * @Description: 业务异常码
 * @date 2020-06-19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BusinessExceptionEnums implements BaseEnum<String> {
    VEHICLE_EXIST("A1200", "车辆已存在"),
    VEHICLE_NOT_EXIST("A1201", "车辆不存在"),

    VEHICLE_MODEL_CONFIG_NOT_EXIST("A1202", "车型:{0},配置:{1},配置:{2}相关信息不存在"),
    STATUS_NOT_SUPPORT_BIND("A1203", "绑定失败，当前车辆状态为:{0}"),
    CUR_USER_HAS_BIND("A1204", "您当前已绑定该车辆"),
    LICENSE_VIN_NOT_IDENTICAL("A1205", "行驶证车辆识别号与申请信息不一致"),
    LICENSE_ENGINE_NO_NOT_IDENTICAL("A1206", "行驶证车辆发动机号与申请信息不一致"),
    INVOICE_PURCHASER_NOT_IDENTICAL("A1207", "行驶证所有人与购车记录中购买方名称不一致,需进入二手车绑定"),
    INVOICE_INVALID("A1208", "无效的机动车销售发票凭证"),
    INVOICE_PURCHASER_CODE_NOT_IDENTICAL("A1209", "机动车销售发票购买方身份证号码/组织机构代码与销售记录不一致,车辆未绑定,需进入二手车绑定"),
    INVOICE_DATE_INVALID("A1210", "无效的【发票日期】机动车销售发票凭证"),
    VEHICLE_HAS_BIND("A1211", "该车辆已被绑定，请先解绑"),
    VEHICLE_BIND_STATUS_CHANGED("A1212", "该车辆绑定状态已发生改变，请刷新重试"),
    VEHICLE_SALE_INFO_NOT_EXIST("A1213", "车辆销售信息不存在"),
    OCR_RECOGNIZED_VEH_INVOICE_ERROR("A1214", "识别机动车发票信息失败"),
    OCR_RECOGNIZED_VEH_LICENSE_ERROR("A1215", "识别行驶证信息失败"),
    VEHICLE_SALE_INFO_HAS_SYNCHRONIZATION("A1216", "车辆销售信息已同步，请勿重复同步"),
    AUTH_CODE_EXPIRED("A1217", "验证码已过期"),
    AUTH_CODE_ERROR("A1218", "验证码输入有误"),
    SEND_INTERVAL_FREQUENTLY("A1219", "验证码发送间隔不能小于{0}秒"),

    DEVICE_MODEL_RELATION_ERROR("A147", "设备与设备型号错误"),
    BINDED_NOT_EXIST("A148", "车辆零部件绑定关系不存"),
    VEHICLE_BINDED_SAME_DEVICE("A1249", "车辆已经绑定相同设备"),
    IMPORT_OVER_LIMIT("A1250", "导入数据操作最大限制"),
    IMPORT_DATA_NOT_EXIST("A1251", "导入数据不存在"),
    EXPORT_DATA_NOT_EXIST("A1252", "导出数据不存在"),
    MODEL_DEVIVE_HAS_BINDED("A1253", "该车辆已绑定同类型设备"),
    DEVICE_EXIST("A1254", "该设备已经存在"),
    DEVICE_BINDING_OTHER_VEH("A1255", "该设备已经绑定其他车辆"),
    DEVICE_NOT_EXIST("A1256", "该设备不存在"),
    DEVICE_MODEL_NOT_EXIST("A1257", "零件型号不存在"),
    DEVICE_NOT_BINDED("A1258", "该车辆没有绑定设备,不允许换件"),
    NOT_EXIST_DEVICE_AVALIABLE("A259", "不存在可用设备"),

    NO_VEHICLE_OWNER("A1260", "非车主用户"),
    QR_CODE_INVALID("A1261", "二维码已失效"),
    CALL_DEVICE_ONLINE_SER_ERROR("A1262", "调用车辆是在线服务发生异常"),
    VEHICLE_NOT_ONLINE("A1263", "车辆暂不在线"),
    VEHICLE_STATUS_NOT_SUPPORT("A1264", "车辆状态暂不支持该操作"),
    INVOICE_PURCHASER_CODE_NOT_IDENTICAL_BIND("A1265", "机动车销售发票购买方身份证号码/组织机构代码与销售记录不一致,车辆已绑定,需进入二手车绑定"),
    HU_STATUS_NOT_SUPPORT("A1266", "车机状态暂不支持该操作"),
    VEHICLE_HAS_NO_BIND("A1267", "车辆暂未绑定"),
    VEHICLE_HAS_ACTIVATE("A1268", "车机已激活，请勿重复操作"),

    GET_REGISTRATION_ID_ERROR("B0702", "极光推送registrationId为空"),
    ;

    private final String code;
    private final String description;

    BusinessExceptionEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return code;
    }
}
