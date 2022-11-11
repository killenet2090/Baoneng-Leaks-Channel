package com.bnmotor.icv.tsp.ble.common;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: RespCode
 * @Description: Web异常码定义
 * @author: hao.xinyue
 * @date: 2020/3/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum RespCode implements BaseEnum<String> {
    // 用户输入
    USER_SMS_CEHCK_CODE_INPUT_ALREADY("输入短信授权码已使用或错误，请检查后重试", "S0132"),
    SER_SMS_SEND_ERROR("发送短信过程出现错误", "S0133"),
    DEL_BLE_MESSAGE_SEND_ERROR("注销蓝牙钥匙时推送消息发生异常", "S0134"),

    // 第三方服务
    OTHER_SERVICE_INVOKE_ERROR("调用第三方服务出错", "C0001"),
    OTHER_SERVICE_PUSHID_ERROR("检测到用户数据没绑定", "S0301"),
    OTHER_SERVICE_INVOKE_TIMEOUT("第三方系统执行超时", "C0100"),

    // 蓝牙钥匙系统
    AUTH_APPLY_ERROR("车主授权阶段加密签名出错", "S0001"),
    VEOWNER_APPLY_ERROR("车主申请失败", "S0002"),
    AUTH_DEVICE_CHECK_ERROR("车辆信息检测失败", "S0003"),
    AUTH_PHONENUMBER_CHECK_ERROR("手机号码格式不对", "S0004"),
    AUTH_COMPE_QUERY_ERROR("权限查询结果为NULL,请确认输入的参数", "S0005"),
    AUTH_DUPLICATE_ERROR("授权重复，请勿重复授权，请稍后再试！", "S0006"),
    AUTH_CONFIRM_DUPLICATE_ERROR("该移动设备下已经存在蓝牙钥匙，请更换移动设备进行确认！", "S0007"),
    AUTH_USERTYPE_IMPERMISSBLE("不能对车主身份授权，请重新选择身份！", "S0013"),
    AUTH_SELF_IMPERMISSBLE("不能对自己进行授权，请重新选择身份授权！", "S0026"),
    AUTH_DELAUTH_ERROR("离线激活失败！", "S0007"),
    AUTH_ACTIVE_OFFLINE_ERROR("蓝牙钥匙离线激活失败！", "S0025"),
    MODIFY_EXPIRETIME_ERROR("蓝牙钥匙更新有效期失败！", "S0024"),
    DATETIME_FORMATE_ERROR("时间格式错误！", "S0008"),
    MODIFY_RIGHT_ERROR("蓝牙钥匙更新权限失败！", "S0009"),
    BLEKEY_NOT_EXIST("该车辆下蓝牙钥匙不存在或者还没激活！", "S0010"),
    CAPIN_WAS_EXIST("证书、pin码已经生成，请勿重复申请！", "S0011"),
    CAPIN_GENERATE_ERROR("证书、PIN码相关信息存库时发生异常！","S0012"),
    QUERY_RESULT_EMPTY("根据传入的参数查询的结果为空，请确认传入的参数！","S0014"),
    AUTH_DATETIME_CHCKEROOR("手机时间不正确，请重新调整后输入！", "S0015"),
    AUTH_NOT_ALLOW_MODIFY("该钥匙未激活，不允许修改！", "S0016"),
    AUTH_NOT_ALLOW_DELETE("该钥匙未激活，不允许删除！", "S0017"),
    AUTH_HAS_ALREAD_ACTIVE("车辆已经激活或则激活码已经过期，请勿再次激活！", "S0018"),
    AUTH_HAS_AUTH_RECORD("该账号在已经用该设备在该车辆上授权过蓝牙钥匙！", "S0019"),
    BLE_DEVICE_OFFLINE("车辆网络不佳，请稍后再试！", "S0020"),
    BLE_APPLY_FREQUENTLY("上次申请开通还未完成，请等待10秒后再试！", "S0027"),
    BLE_OVER_SPAN_LIMIT("当前移动设备已经是最新蓝牙钥匙，建议超过24小时再更新！", "S0021"),
    BLE_EXPIRE_TIME_ERROR("输入的有效期转换出错，请检查输入的有效期参数！", "S0022"),
    BLE_EXPIRE_PUSHID_ERROR("根据输入的手机号码，查询的极光推送信息为NULL！", "S0023"),
    BLE_ACTIVATE_PARSE_ERROR("再次激活蓝牙钥匙线上过程中出现异常，请按线下方式进行激活！", "S0530"),
    BLE_ACTIVATE_AUTH_EXPIRE("再次线上激活蓝牙钥匙线上有效期已过，请按线下方式进行激活！", "S0532"),
    BLE_STATUS_HAS_ACTIVATE("你的蓝牙钥匙已经处于激活状态，无需再次激活！", "S0533"),
    BLE_BLEKEY_NOT_FOUND("未找到要激活的蓝牙钥匙，请重新选择正确的参数！", "S0531"),
    // 未知错误
    UNKNOWN_ERROR("未知错误", "ZOOOO"),
    // 成功
    SUCCESS("成功", "00000");

    RespCode(String description, String code) {
        this.code = code;
        this.description = description;
    }

    private String description;

    private String code;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return code;
    }

}
