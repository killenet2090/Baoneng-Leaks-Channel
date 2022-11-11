package com.bnmotor.icv.tsp.ota.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;
import lombok.Getter;

/**
 * @ClassName: OTARespCodeEnum
 * @Description:    OTA项目响应枚举类型
 * @author: xuxiaochang1
 * @date: 2020/6/13 17:53
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum OTARespCodeEnum implements BaseEnum<String> {

    FILE_VERIFY_ERROR("文件校验错误", "A0100"),
    FILE_UPLOAD_PARAM_ERROR("文件上传参数错误", "A0101"),
    FILE_UPLOAD_ERROR("文件上传错误", "A0102"),
    FILE_ID_PARAM_ERROR("文件Id参数异常", "A0103"),
    FILE_UPLOAD_RECORD_NULL_ERROR("文件记录异常", "A0104"),
    FILE_UPLOAD_EXIT_ERROR("上传文件已存在", "A0105"),
    FILE_UPLOAD_WITH_SAME_VERSION_ERROR("当前上传升级包与该固件之前版本文件重合，请检查", "A0106"),


    FIRMWARE_NEW_EXIST_ERROR("固件已存在", "A0200"),
    FIRMWARE_CODE_NEW_EXIST_ERROR("固件代码已存在", "A0201"),
    FIRMWARE_VERSION_NEW_EXIST_ERROR("固件版本已存在", "A0202"),
    FIRMWARE_VERSION_APPLIED_SELECT_ERROR("固件适用版本选择异常", "A0203"),
    FIRMWARE_NOT_EXIST_ERROR("固件不存在", "A0204"),

    //A0300-之类的定义与前端预定义错误码有冲突，暂时屏蔽
    FIRMWARE_UPDATE_ERROR("固件更新失败", "A0400"),
    FIRMWARE_WITHVERSION_NOTALLOWED_DEL("已经存在固件版本，不允许删除", "A0401"),
    FIRMWARE_DEL_ERROR("固件删除失敗", "A0402"),

    FIRMWARE_VERSION_NOT_EXIST_ERROR("固件版本不存在", "A0403"),
    FIRMWARE_VERSION_PRE_WITHOUT_PACKAGE_ERROR("存在之前的固件版本升级包未创建,请检查", "A0404"),

    FIRMWARE_VERSION_PKG_UPLOAD_FINISHED("固件版本升级包已存在", "A0500"),
    FIRMWARE_DEPENDENCE_VERSIONS_NOT_ALLOWED_BELONGTO_OTHER_CAR("固件依赖版本列表不属于同一款配置车，请检查", "A0501"),
    FIRMWARE_DEPENDENCE_VERSIONS_NOT_ALLOWED_BELONGTO_SAME("固件依赖版本列表不允许属于同一个固件，请检查", "A0502"),


    FIRMWARE_PKGS_WITH_STRATEGY("固件版本已经绑定了升级策略，不允许删除升级包", "A0503"),
    FIRMWARE_PKGS_NOT_EXISTS("固件版本升级包不存在", "A0504"),
    FIRMWARE_PKGS_UPLOAD_NOT_EXISTS("固件版本升级包未上传", "A0505"),

    /*
        ====================升级相关 start =====================================
     */
    FOTA_OBJECT_NOT_EXIST("升级对象不存在", "A0601"),
    FOTA_FIRMWARE_LIST_NOT_EXIST("升级对象固件清单为空", "A0602"),

    FOTA_OBJECT_IN_ANOTHORE_PLAN("升级对象已经存在另一个有效的升级任务内,不允许重复添加到新任务中", "A0701"),
    FOTA_FIRMWARE_NOT_MATCH("升级请求固件参数不合法", "A0603"),
    /*

        ====================升级相关 start =====================================
     */
    FOTA_PLAN_INVALID_OBJECT("无效升级车辆", "P0101"),
    
    FOTA_PLAN_INVALID_OBJECT_EXIST_IN_OTHER_PLAN("无效升级车辆", "P0102"),
    /*
     * ====================任务相关 end =====================================
     */

    FOTA_PLAN_OBJ_NOT_EXIST("升级车辆不存在", "A0701"),

    FOTA_PLAN_OBJ_LIST_NOT_EXIST("升级任务车辆不存在", "A0702"),

    FOTA_CHECK_VERIFY_NOT_EXIST("升级事务异常", "A0703"),

    FOTA_OBJECT_FRIMWARE_LIST_NOT_EXIST("升级任务固件清单信息异常", "A0704"),

    FOTA_PLAN_FRIMWARE_LIST_NOT_EXIST("升级任务固件信息异常", "A0705"),

    FOTA_PLAN_TASK_DETAIL_NOT_EXIST("升级任务详情信息异常", "A0706"),

    FOTA_PLAN_NOT_EXIST("升级任务不存在", "A0707"),

    FOTA_PLAN_INVALID_NOT_ALLOWED_ENABLED("升级任务已失效不允许开启", "A0707"),
    
    FOTA_PACKAGE_SIGN_FAILED("OTA文件签名失败" ,"A0708"),
    FOTA_PACKAGE_ENCRYPT_FAILED("OTA文件加密失败" ,"A0709"),
    FOTA_PACKAGE_DOWNLOAD_FAILED("OTA升级文件无法下载" ,"A0710"),
    FOTA_PACKAGE_UPLOAD_FILE_NO_EXISTS("OTA升级文件对应的文件上传记录不存在" ,"A0711"),

    FOTA_VERSION_CHECK_TRANSID_ERROR("升级事务Id参数异常", "A0712"),

    APP_REMOTE_DOWNLOAD_FAIL("远程下载操作失败", "A0801"),

    APP_REMOTE_INSTALLED_FAIL("远程安装操作失败", "A0802"),

    APP_CANCLE_INSTALLED_BOOKED_FAIL("取消预约安装操作失败", "A0803"),


    UP_BUSINESS_TYPE_INVALID("上行消息类型异常", "A0901"),

    UP_TBOX_PRAMAETER_BODY_ERROR("上行消息(主体)不能为空", "A0902"),
    UP_TBOX_PRAMAETER_HEADER_ERROR("上行消息(头)不能为空", "A0903"),

    //定义的面向APP的异常错误类型码
    /*APP_RESP_CODE_TBOX_SLEEPED_4DOWNLOAD("车辆已经休眠,将在下次启动后自动下载", "A1001"),
    APP_RESP_CODE_TBOX_FAIL_4DOWNLOAD("系统繁忙请重新下载或稍后重试", "A1002"),
    APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD("车辆存储空间不足，请联系客服处理", "A1003"),
    APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD("升级包校验失败", "A1004"),
    APP_RESP_CODE_TBOX_COMPLETED_FAIL_4INSTALLED("安装失败，但是版本回滚失败，可以拨打客户", "A1005"),
    APP_RESP_CODE_TBOX_COMPLETED_FAIL_WITH_ROLLBACK_SUCCESS_4INSTALLED("安装失败，但是版本回滚成功,可再次升级", "A1006"),*/
    /*
     *==========================================================================
     */


    FOTA_STRATEGY_NOT_ALLOWED_ERROR("策略绑定了任务,禁止被删除", "A1001"),


    SYSTEM_ERROR("系统异常", "B0100"),

    PARAMETER_VALIDATION_ERROR("参数校验失败", "B0200"),


    // 系统级别
    //SYSTEM_ERROR("系统繁忙，请联系管理员！", "5000"),

    //数据级别
    TOKEN_EXPIRED("登陆已过期或者token验证失败,请重新登陆！", "B0401"),
    NO_PERMISSION("无数据访问权限！", "B0403"),

    DATA_SAVE_FAIL("数据新增失败！", "B0402"),
    DATA_NOT_FOUND("数据未找到！", "B0404"),
    DATA_NOT_UPDATED("数据更新失败！", "B0405"),
    DATA_NOT_NULL("数据不能为空！", "B0406"),
    DATA_CANT_NOT_DELETE("数据不能被删除或数据正在被使用！", "B0407"),

    FILE_TYPE_NOT_SUPPORTED("不支持的上传文件类型！", "B0408"),
    BUILD_DATA_NOT_FINISH("升级包未完成制作/或升级包缺失！", "B0409"),


    GET_CACHED_SLICE_DATA_ERROR("文件分片上传记录数据未找到！", "B0410"),
    SET_CACHED_SLICE_DATA_ERROR("设置文件分片上传记录数据失败！", "B0411"),

    ;

    @Getter
    private String code;

    @Override
    public String getDescription(){
        return code;
    }

    //@Getter
    private String value;

    @Override
    public String getValue(){
        return value;
    }

    private OTARespCodeEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
