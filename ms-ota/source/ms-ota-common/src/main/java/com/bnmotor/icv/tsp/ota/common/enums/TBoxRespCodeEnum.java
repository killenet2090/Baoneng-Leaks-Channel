package com.bnmotor.icv.tsp.ota.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: TBoxRespCodeEnum
 * @Description:    OTA项目响应枚举类型
 * @author: xuxiaochang1
 * @date: 2020/6/13 17:53
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum TBoxRespCodeEnum implements BaseEnum<Integer> {
    //定义的面向APP的异常错误类型码
    APP_RESP_CODE_TBOX_SLEEPED_4DOWNLOAD(101,"车辆已经休眠,将在下次启动后自动下载"),
    APP_RESP_CODE_TBOX_FAIL_4DOWNLOAD(102, "系统繁忙请重新下载或稍后重试"),
    APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD(103, "车辆存储空间不足，请联系客服处理"),
    APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD(104, "升级包校验失败"),
    APP_RESP_CODE_TBOX_COMPLETED_FAIL_4INSTALLED(105, "安装失败，但是版本回滚失败，可以拨打客户"),
    APP_RESP_CODE_TBOX_COMPLETED_FAIL_WITH_ROLLBACK_SUCCESS_4INSTALLED(106, "安装失败，但是版本回滚成功,可再次升级"),


    //参数校验错误码
    OTA_RESP_CODE_PARAM_ERROR_VIN(1001, "vin码校验异常"),
    OTA_RESP_CODE_PARAM_ERROR_BUSINESS_ID(1002, "businessId异常"),
    OTA_RESP_CODE_PARAM_ERROR_TRANS_ID(1003, "transId异常"),
    OTA_RESP_CODE_PARAM_ERROR_TASK_ID(1004, "taskId异常"),
    OTA_RESP_CODE_PARAM_ERROR_REQ_ID(1005, "reqId异常"),
    OTA_RESP_CODE_PARAM_ERROR_CONF_VERSION(1005, "固件配置清单版本号参数异常"),
    OTA_RESP_CODE_PARAM_ERROR_SOURCE_TYPE(1006, "请求来源类型不能为空"),


    //业务错误码
    OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION(1101, "没有新版本"),
    OTA_RESP_CODE_BUSINESS_ERROR_CONF_VERSION_NOT_MATCH(1102, "固件配置清单版本号与云端不匹配"),
    OTA_RESP_CODE_BUSINESS_ERROR_PLAN_INVALID(1103, "升级任务失效"),
    OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_NOT_EXIST(1104, "车辆不存在"),
    OTA_RESP_CODE_BUSINESS_ERROR_PLAN_NOT_EXIST(1105, "升级任务不存在"),
    OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_WITH_NO_PLAN(1106, "车辆没有绑定到升级任务"),
    OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_WITH_NO_FIRMWARE_LIST(1107, "车辆未配置固件清单"),

    OTA_RESP_CODE_TBOX_PARAM_ERROR_CURRENT_INSTALLED_INDEX(1201, "当前升级安装包序号异常"),
    OTA_RESP_CODE_TBOX_PARAM_ERROR_INSTALLED_TOTAL_PKG_NUM(1202, "升级包总数异常"),
    /*OTA_RESP_CODE_BUSINESS_ERROR_TBOX_ECU_LIST(1106, "车辆上报Ecu列表参数异常"),*/
    /*OTA_RESP_CODE_BUSINESS_ERROR_PLAN_FIRMWARE_LIST(1107, "升级任务固件清单异常"),
    OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_PLAN_FIRMWARE_NOT_MATCH(1108, "车辆在与对应升级任务固件清单不匹配"),*/
    /*OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_CURRENT_VERSION_NOT_MATCH(1109, "车辆Ecu当前版本号异常"),
    OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_TARGET_VERSION_NOT_MATCH(1110, "车辆Ecu目标版本号异常"),
    OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_VERSION_PATH_NOT_FIND(1111, "Ecu升级路径不存在"),*/
    /*OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_UPGRAD_PKG_NOT_FIND(1112, "Ecu升级包异常"),*/

    SYS_ERROR(2101, "系统处理异常"),


    SUCCESS(200, "成功"),
    ;

    @Getter
    private Integer code;

    @Getter
    private String desc;

    /**
     *
     * @param code
     * @param desc
     */
    private TBoxRespCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取状态码描述
     * @return
     */
    public static String getDescriptionList(){
        StringBuilder sb = new StringBuilder();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        EnumSet.allOf(TBoxRespCodeEnum.class).stream().forEach( item -> sb.append(atomicInteger.incrementAndGet() +"状态码:"+item.getCode()+",描述="+item.getDesc() + "\r\n"));
        return sb.toString();
    }

    /**
     * 是否为下载失败
     * @param code
     * @return
     */
    public static boolean downloadFail(Integer code){
        if(Objects.isNull(code)){
            return false;
        }
        return APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getCode().equals(code) ||
                APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getCode().equals(code);
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
