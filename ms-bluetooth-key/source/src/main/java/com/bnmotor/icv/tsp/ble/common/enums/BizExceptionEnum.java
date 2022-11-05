package com.bnmotor.icv.tsp.ble.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: anno
 * @Description: 描述类的作用
 * @author: liuyiwei1
 * @date: 2020/7/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum BizExceptionEnum implements BaseEnum {
    BLE_KEY_CREATE_VEH_REQ_ILLEGAL("S0100", "蓝牙钥匙申请时车辆验证失败"),
    BLE_DEVICE_UNREGISTER("S0102","检测到你授权的车辆目前还没注册，请确保车辆注册后再授权"),
    BLE_VERY_PWD_ERROR("S0106","验证服务密码时发生错误"),
    BLE_VERY_TOKEN_ERROR("S0108","验证用户服务Token时发生错误"),
    BLE_IS_ONLINE_ERROR("S0107","查询车辆是否在线时发生错误"),
    BLE_KEY_AUTH_SAVE_REDIS_ERROR("S0150","授权信息缓存到redis出错"),
    BLE_KEY_CREATE_SEND_TBOX_ERR("S0101", "蓝牙钥匙申请, 发送车辆T-BOX失败"),
    BLE_KEY_AUTH_SAVE_ERROR("S0150","授权信息过程出错"),
    BLE_KEY_QUERY_DEVICE_sn_ERR("S0103", "根据车辆vin获取设备SN没数据"),
    BLE_KEY_QUERY_VEH_FAILURE("S0200", "车主蓝牙钥匙无效"),
    BLE_KEY_QUERY_VEH_OWNER_EXIST("S0201", "当前设备上已经存在有效的蓝牙钥匙， 请从管理中删除该蓝牙钥匙"),
    BLE_KEY_REPLACE_REQ_ILLEGAL("S0301", "蓝牙钥匙更新请求非法"),
    BLE_KEY_PIN_CODE_NOT_EXIST("S0304", "车辆还没向平台注册,请先通过车辆向平台注册"),
    BLE_KEY_REPLACE_VEH_OWNER_INVALID("S0302", "蓝牙钥匙更新时， 注销原蓝牙钥匙失败"),
    BLE_KEY_REPLACE_VEH_REF_NOT_EXISTED("S0303", "蓝牙钥匙更新时， 无法关联服务权限"),
    BLE_KEY_DELETE_WITH_PUSH_MSG_ERROR("S0401", "蓝牙钥匙注销时， 写推送服务数据出错"),
    BLE_KEY_DELETE_WITH_PUSH_PROCESS_ERROR("S0402", "蓝牙钥匙注销时， 注销流程出错"),
    BLE_KEY_QUERY_WITH_PUSH_MSG_ERROR("S0403", "从车端查询某个蓝牙钥匙时， 写推送服务数据出错"),
    BLE_KEY_MODIFY_WITH_PUSH_MSG_ERROR("S0501", "蓝牙钥匙修改时， 写推送服务数据出错"),
    BLE_KEY_MODIFY_WITH_PUSH_PROCESS_ERROR("S0502", "蓝牙钥匙修改时， 修改流程出错"),
    BLE_KEY_MODIFY_WITH_CHECK_PROCESS_ERROR("S0503", "蓝牙钥匙修改时， 根据输入的参数查询记录为空"),
    BLE_KEY_LIMIT_NUM_CHECK_PROCESS_ERROR("S0504", "蓝牙钥匙有效数量已经到达10把限制,请勿过多申请"),
    BLE_KEY_USERNAME_NUM_CHECK_PROCESS_ERROR("S0505", "你的车下该用户名已经存在,请换个用户名授权"),
    BLE_KEY_USERNAME_UPDATE_PROCESS_ERROR("S0506", "根据输入的参数检测更新钥匙不合法"),
    BLE_KEY_DEVICENAME_UPDATE_PROCESS_ERROR("S0507", "根据输入的参数没更新失败"),
    BLE_KEY_EXPIRE_TIME_IS_VALID("S0508", "检测到要注册的蓝牙钥匙还在有效期内，本次删除无效"),
    BLE_KEY_USER_AUTH_IS_EMPTY("S0509", "用户授权数据为空，请选择用户授权记录再试"),
    ;

    private String code;
    private String desc;


    BizExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public String getValue() {
        return code;
    }
}
