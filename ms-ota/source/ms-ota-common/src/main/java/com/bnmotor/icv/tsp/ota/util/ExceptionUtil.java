package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.TBoxRespCodeEnum;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @ClassName: ExceptionUtil
 * @Description:   异常处理工具类，用于抛出业务异常
 * @author: xuxiaochang1
 * @date: 2020/6/13 17:57
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class ExceptionUtil {

    private ExceptionUtil(){}

    private static final Map<OTARespCodeEnum, TBoxRespCodeEnum> respCodeMaps = Maps.newHashMap();

    static {
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.SYS_ERROR);

       /* OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_NOT_EXIST(1101, "车辆不存在"),
                OTA_RESP_CODE_BUSINESS_ERROR_PLAN_NOT_EXIST(1102, "升级任务不存在"),
                OTA_RESP_CODE_BUSINESS_ERROR_PLAN_INVALID(1103, "升级任务失效"),
                OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_WITH_NO_PLAN(1104, "车辆没有绑定到升级任务"),
                OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_WITH_NO_FIRMWARE_LIST(1105, "车辆未配置固件清单"),
                OTA_RESP_CODE_BUSINESS_ERROR_TBOX_ECU_LIST(1106, "车辆上报Ecu列表参数异常"),
                OTA_RESP_CODE_BUSINESS_ERROR_PLAN_FIRMWARE_LIST(1107, "升级任务固件清单异常"),
                OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_PLAN_FIRMWARE_NOT_MATCH(1108, "车辆在与对应升级任务固件清单不匹配"),
                OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_CURRENT_VERSION_NOT_MATCH(1109, "车辆Ecu当前版本号异常"),
                OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_TARGET_VERSION_NOT_MATCH(1110, "车辆Ecu目标版本号异常"),
                OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_VERSION_PATH_NOT_FIND(1111, "Ecu升级路径不存在"),
                OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_UPGRAD_PKG_NOT_FIND(1112, "Ecu升级包异常"),*/


                /*respCodeMaps.put(OTARespCodeEnum.FOTA_PLAN_OBJ_NOT_EXIST, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_NOT_EXIST);
        respCodeMaps.put(OTARespCodeEnum.FOTA_PLAN_NOT_EXIST, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_PLAN_NOT_EXIST);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_PLAN_INVALID);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_WITH_NO_PLAN);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_WITH_NO_FIRMWARE_LIST);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_TBOX_ECU_LIST);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_PLAN_FIRMWARE_LIST);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_PLAN_FIRMWARE_NOT_MATCH);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_CURRENT_VERSION_NOT_MATCH);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_TARGET_VERSION_NOT_MATCH);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_VERSION_PATH_NOT_FIND);
        respCodeMaps.put(OTARespCodeEnum.SYSTEM_ERROR, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_ECU_UPGRAD_PKG_NOT_FIND);*/
    }

    /**
     * 构建异常
     * @param otaRespCodeEnum
     * @return
     */
    public static AdamException buildAdamException(OTARespCodeEnum otaRespCodeEnum){
        return new AdamException(otaRespCodeEnum.getCode(), otaRespCodeEnum.getValue());
    }

    /**
     * 构建异常
     * @param otaRespCodeEnum
     * @param msg
     * @return
     */
    public static AdamException buildAdamException(OTARespCodeEnum otaRespCodeEnum, String msg){
        return new AdamException(otaRespCodeEnum.getCode(), msg);
    }

    /**
     *
     * @param tboxRespCodeEnum
     * @return
     */
    public static TboxAdamException buildTboxAdamException(TBoxRespCodeEnum tboxRespCodeEnum){
        return new TboxAdamException(tboxRespCodeEnum.getCode(), tboxRespCodeEnum.getDesc());
    }

    /**
     * 构建异常
     * @param otaRespCodeEnum
     * @param t
     * @return
     */
    public static AdamException buildAdamException(OTARespCodeEnum otaRespCodeEnum, Throwable t){
        return new AdamException(otaRespCodeEnum.getValue(), t);
    }

    /**
     *
     * @param tboxRespCodeEnum
     * @param t
     * @return
     */
    public static TboxAdamException buildTboxAdamException(TBoxRespCodeEnum tboxRespCodeEnum, Throwable t){
        return new TboxAdamException(tboxRespCodeEnum.getDesc(), t);
    }

    /*public static AdamException buildAdamException(BusinessStatusEnum businessStatusEnum){
        return new AdamException(businessStatusEnum.getCode(),businessStatusEnum.getDescription());
    }*/
}
