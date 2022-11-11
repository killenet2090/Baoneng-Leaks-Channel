package com.bnmotor.icv.tsp.ota.common.enums;

import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>
 *  通用枚举类，防止枚举类过多.
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
public final class Enums {
    /**
     * <pre>
     * 系统返回状态码
     * </pre>
     */
    public enum Status {
        /**
         * 操作成功
         */
        Success(200),
        /**
         * 未授权
         */
        Unauthorized(401),
        /**
         * 系统异常
         */
        Error(500),
        /**
         * 业务失败
         */
        BizFailure(700),
        /**
         * 参数校验不通过
         */
        ValidFailure(800),
        ;
        /**
         * 枚举值
         */
        private final int value;

        /**
         * <pre>
         *  私有构造函数
         * </pre>
         *
         * @param value 值
         */
        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 数据逻辑删除状态枚举
     */
    public enum DelFlagEnum {
        /**
         * 保留
         */
        RESERVED(0),

        /**
         * 删除
         */
        DEL(1),
        ;

        @Getter
        private int flag;

        DelFlagEnum(int flag) {
            this.flag = flag;
        }
    }

    /**
     * Tbox过来一般状态枚举
     */
    public enum CommmonTBoxStatusTypeEnum {
        /**
         * 成功
         */
        SUCCESS(1),

        /**
         * 失败
         */
        FAIL(2),
        ;

        @Getter
        private int status;

        CommmonTBoxStatusTypeEnum(int status) {
            this.status = status;
        }
    }

    /**
     * 上传文件类型枚举
     */
    public enum UploadFileTypeEnum {
        /**
         * 升级包
         */
        PKG(0, CommonConstant.BUCKET_OTA_GROUP_PKG),

        /**
         * 测试报告
         */
        REPORT(1, CommonConstant.BUCKET_OTA_GROUP_REPORT),

        /**
         * 脚本文件类型
         */
        SCRIPT(2, CommonConstant.BUCKET_OTA_GROUP_SCRIPT),

        /**
         * 脚本文件类型
         */
        UPGRADELOG(3, CommonConstant.BUCKET_OTA_GROUP_UPGRADELOG),
        ;

        @Getter
        private Integer type;

        @Getter
        private String bucketName;

        UploadFileTypeEnum(Integer type, String bucketName) {
            this.type = type;
            this.bucketName = bucketName;
        }

        /**
         * @param type
         * @return
         */
        public static UploadFileTypeEnum getByType(Integer type) {
            return EnumSet.allOf(UploadFileTypeEnum.class).stream().filter(item -> item.type.equals(type)).findFirst().orElse(null);
        }
    }

    /**
     * 固件升级模式枚举
     */
    public enum FirmwareUpgradeModeTypeEnum {
        /**
         * 全量
         */
        WHOLE(0, "全量"),

        /**
         * 补丁
         */
        PATCH(1, "补丁"),

        /**
         * 差分
         */
        DIFF(2, "差分"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        FirmwareUpgradeModeTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 是否为全量升级路径类型
         *
         * @param type
         * @return
         */
        public static boolean isWholeType(int type) {
            return FirmwareUpgradeModeTypeEnum.WHOLE.getType() == type;
        }

        /**
         * @param type
         * @return
         */
        public static FirmwareUpgradeModeTypeEnum getByType(int type) {
            return EnumSet.allOf(FirmwareUpgradeModeTypeEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }
    }

    /**
     * 设备同步操作类型
     */
    public enum DeviceSyncActionTypeEnum {
        /**
         * 新增
         */
        ADD(1, "新增"),

        /**
         * 更新
         */
        UPDATE(2, "更新"),

        /**
         * 删除
         */
        DELETE(3, "删除"),
        ;

        @Getter
        private Integer type;
        @Getter
        private String desc;

        DeviceSyncActionTypeEnum(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }


        /**
         * @param type
         * @return
         */
        public static DeviceSyncActionTypeEnum getByType(Integer type) {
            return EnumSet.allOf(DeviceSyncActionTypeEnum.class).stream().filter(item -> item.type.equals(type)).findFirst().orElse(null);
        }
    }


    /**
     * 固件升级路径枚举
     */
    public enum FirmwareUpgradePkgTypeEnum {
        /**
         * 全量
         */
        WHOLE(0, "全量包", "full.zip", FirmwareUpgradeModeTypeEnum.WHOLE),

        /**
         * 补丁
         */
        PATCH(1, "补丁包", "patch.zip", FirmwareUpgradeModeTypeEnum.PATCH),

        /**
         * 差分
         */
        DIFF(2, "差分包", "delta.zip", FirmwareUpgradeModeTypeEnum.DIFF),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;
        @Getter
        private String fileName;
        @Getter
        private FirmwareUpgradeModeTypeEnum firmwareUpgradeModeTypeEnum;

        FirmwareUpgradePkgTypeEnum(int type, String desc, String fileName, FirmwareUpgradeModeTypeEnum firmwareUpgradeModeTypeEnum) {
            this.type = type;
            this.desc = desc;
            this.fileName = fileName;
            this.firmwareUpgradeModeTypeEnum = firmwareUpgradeModeTypeEnum;
        }
    }

    /**
     * 升级结果枚举
     */
    public enum UpgradeResultTypeEnum {
        UPGRADE_UNDEFALT(0, "-"),
        /**
         * 全量
         */
        UPGRADE_COMPLETED(1, "升级完成（成功）"),

        /**
         * 补丁
         */
        UPGRADE_UNCOMPLETED(2, "升级未完成（回滚成功）"),

        /**
         * 差分
         */
        UPGRADE_FAIL(3, "升级失败"),
        ;

        public static List<Map<String,Object>> typeDescList = new ArrayList<>();
        static {
            EnumSet<UpgradeResultTypeEnum> upgradeResultTypeEnums = EnumSet.allOf(UpgradeResultTypeEnum.class);
            for (UpgradeResultTypeEnum statusTypeEnum:upgradeResultTypeEnums ){
                Map<String,Object> typeDescMap = new HashMap();
                typeDescMap.put("type",statusTypeEnum.getType());
                typeDescMap.put("desc",statusTypeEnum.getDesc());
                typeDescList.add(typeDescMap);
            }
        }

        @Getter
        private int type;
        @Getter
        private String desc;

        UpgradeResultTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * @param type
         * @return
         */
        public static UpgradeResultTypeEnum getByType(int type) {
            return EnumSet.allOf(UpgradeResultTypeEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }
    }

    /**
     * 0或1枚举
     */
    public enum ZeroOrOneEnum {
        /**
         * 0
         */
        ZERO(0),

        /**
         * 1
         */
        ONE(1),
        ;

        @Getter
        private int value;

        ZeroOrOneEnum(int value) {
            this.value = value;
        }

        /**
         * 是否在枚举范围内
         *
         * @param isForceFullUpdate
         * @return
         */
        public static boolean contains(Integer isForceFullUpdate) {
            if (Objects.isNull(isForceFullUpdate)) {
                return false;
            }
            return EnumSet.allOf(ZeroOrOneEnum.class).stream().filter(item -> item.value == isForceFullUpdate).findFirst().isPresent();
        }
    }

    /**
     * 端类型
     */
    /*public enum ClientSourceTypeEnum {
        *//**
         * 0
         *//*
        HU(0, "车机端"),

        *//**
         * 1
         *//*
        APP(1, "APP端"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        ClientSourceTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        *//**
         * 是否在枚举范围内
         *
         * @param type
         * @return
         *//*
        public static boolean contains(Integer type) {
            if (Objects.isNull(type)) {
                return false;
            }
            return EnumSet.allOf(ClientSourceTypeEnum.class).stream().filter(item -> item.type == type).findFirst().isPresent();
        }
    }*/

    /**
     * TBOX端下载确认结果处理理性枚举
     */
    public enum DownloadResultVerifyTypeEnum {
        /**
         * 0
         */
        IMMEDIATE(1, "立即下载"),

        /**
         * 1
         */
        DELAY(2, "延迟下载"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        DownloadResultVerifyTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 是否在枚举范围内
         *
         * @param type
         * @return
         */
        public static boolean contains(Integer type) {
            if (Objects.isNull(type)) {
                return false;
            }
            return EnumSet.allOf(DownloadResultVerifyTypeEnum.class).stream().filter(item -> item.type == type).findFirst().isPresent();
        }
    }

    /**
     * 固件版本发布状态枚举
     */
    public enum FirmwareVersionStatusEnum {

        /**
         * 新建
         */
        NEW(0, "新建"),

        /**
         * 待审核
         */
        WAIT_APPROVAL(1, "待审核"),

        /**
         * 审核通过
         */
        APPROVAL_PASSED(2, "审核通过"),

        /**
         * 审核不通过
         */
        APPROVAL_UNPASSED(3, "审核不通过"),

        /**
         * 已发布
         */
        PUBLISHED(4, "已发布"),
        ;

        @Getter
        private int status;
        @Getter
        private String desc;

        FirmwareVersionStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        /**
         * 是否在枚举范围内
         *
         * @param status
         * @return
         */
        public static boolean contains(int status) {
            if (Objects.isNull(status)) {
                return false;
            }
            return EnumSet.allOf(FirmwareVersionStatusEnum.class).stream().filter(item -> item.status == status).findFirst().isPresent();
        }
    }

    /**
     * 字典枚举类型
     */
    public enum DictTypeEnum {
        /**
         * FLASH_SCRIPT
         */
        FLASH_SCRIPT("FLASH_SCRIPT", "刷写脚本"),
        ;

        @Getter
        private String type;
        @Getter
        private String desc;

        DictTypeEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    /**
     * 任务状态枚举类型
     */
    /*public enum PlanStatusTypeEnum {
        OUTLINE(-1, "草稿"),
        CREATED(0, "创建成功待审核"),
        SUBMIT(1, "已提交审核"),
        AUDITED(2, "已审核通过"),
        EXECUTING(3, "执行中(保留)"),
        INVALID(4, "已失效"),
        ;
        public static List<Map<String,Object>> typeDescList = new ArrayList<>();
        @Getter
        private int type;
        @Getter
        private String desc;

        private PlanStatusTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }


        public static boolean isValid(Integer type) {
            if (Objects.isNull(type)) {
                return false;
            }
            return AUDITED.getType() == type.intValue();
        }

        public static boolean inValid(Integer type) {
            if (Objects.isNull(type)) {
                return false;
            }
            return INVALID.getType() == type.intValue();
        }


        public static List<Integer> needUpgradeNotifyStatusList(){
            return Lists.newArrayList(PlanStatusTypeEnum.AUDITED.getType());
        }

        public static PlanStatusTypeEnum getByType(int type) {
            return EnumSet.allOf(PlanStatusTypeEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }

        static {
            EnumSet<PlanStatusTypeEnum> planStatusTypeEnums = EnumSet.allOf(PlanStatusTypeEnum.class);
            for (PlanStatusTypeEnum statusTypeEnum:planStatusTypeEnums ){
                Map<String,Object> typeDescMap = new HashMap();
                typeDescMap.put("type",statusTypeEnum.getType());
                typeDescMap.put("desc",statusTypeEnum.getDesc());
                typeDescList.add(typeDescMap);
            }
        }
    }*/

    /**
     * 升级计划（任务）任务状态
     */
    /**
     * 任务状态枚举类型
     */
    public enum PlanMonitoriedStatusTypeEnum {
        //「有效」（目前任务管理中已审核状态）、「已失效」（目前任务管理中已失效状态）；
        AUDITED(2, "有效"),
        INVALID(4, "已失效"),
        ;
        public static List<Map<String,Object>> typeDescList = new ArrayList<>();
        @Getter
        private int type;
        @Getter
        private String desc;

        PlanMonitoriedStatusTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * @param type
         * @return
         */
        public static PlanMonitoriedStatusTypeEnum getByType(int type) {
            return EnumSet.allOf(PlanMonitoriedStatusTypeEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }

        static {
            EnumSet<PlanMonitoriedStatusTypeEnum> planMonitoriedStatusTypeEnums = EnumSet.allOf(PlanMonitoriedStatusTypeEnum.class);
            for (PlanMonitoriedStatusTypeEnum planMonitoriedStatusTypeEnum:planMonitoriedStatusTypeEnums ){
                Map<String,Object> typeDescMap = new HashMap();
                typeDescMap.put("type",planMonitoriedStatusTypeEnum.getType());
                typeDescMap.put("desc",planMonitoriedStatusTypeEnum.getDesc());
                typeDescList.add(typeDescMap);
            }
        }
    }

    /**
     * 升级对象状态
     */
    public enum TaskObjMonitoriedStatusTypeEnum {

        /**
         * 【⻋辆当前状态】包括：
         *
         * 【未获取任务】，表示⻋辆为完成版本检测；status：0
         * 【待下载】，表示已经获取任务信息，可以启动下载过程   status：2
         * 【下载中】，表示正在下载升级包  status：3
         * 【下载完成】，表示升级包下载完成  （预期为4，）
         * 【待升级】，表示升级包已经下载成功，可以启动升级过程 status：4
         * 【升级中】，表示⻋辆具备升级条件，正式进⾏刷写；status：5
         * 【升级完成】，表示升级过程完成  status：6/7
         */
        NO_VERSION(-1, "未获取任务"),
        NO_VERSION_WITHCHECK(-2, "未获取任务"),

        //版本检查阶段状态
        CREATED(0, "未获取任务"),
        CHECK_VERIFY(1, "检查到任务"),
        //该状态定义保留:可以通过下载确认请求去同步该状态
        CHECK_ACK_VERIFY(2, "待下载"),

        /**
         * 【待下载】，表示已经获取任务信息，可以启动下载过程
         *  【下载中】，表示正在下载升级包
         *  【下载终⽌】，包括⽹络异常、数据异常等造成的升级包下载中断
         *  【下载等待】，表示建⽴通道连接，升级包下载前的状态
         *  【下载完成】，表示升级包下载完成，同时进⼊到安装包检验状态
         *  【下载成功】，表示升级包下载完成，安装包校验成功
         *  【下载失败】，表示升级包下载完成，安装包校验失败
         */

        //下载节点相关状态
        DOWNLOAD_ACK_WAIT(11, "待下载"),
        DOWNLOAD_ACK_BEGIN(12, "下载中"),
        DOWNLOAD_PROCESSING(13, "下载中"),
        DOWNLOAD_PROCESSING_STOP(14, "下载中"),
        DOWNLOAD_PROCESSING_WAIT(15, "下载中"),
        DOWNLOAD_COMPLETED(16, "下载完成(待验证)"),
        DOWNLOAD_COMPLETED_SUCCESS(17, "下载完成(验证成功)"),
        DOWNLOAD_COMPLETED_FAIL(18, "下载完成(验证失败)"),

        //升级节点相关状态
        INSTALLED_ACK_WAIT(31, "待升级"),
        INSTALLED_ACK_BEGIN(32, "开始升级"),
        INSTALLED_PRECHECK_FAIL(33, "升级前置条件检查失败，待升级"),
        INSTALLED_PREOPERATE_FROM_HU(34, "待升级"),    //实际情况可能会不存在
        INSTALLED_CANCEL(35, "待升级"),
        INSTALLED_PROCESSING(36, "升级中"),
        INSTALLED_COMPLETED_SUCCESS(37, "升级完成"),
        INSTALLED_COMPLETED_FAIL(38, "升级未完成"),
        INSTALLED_FAIL(39, "升级失败"),
        INSTALLED_PLAN_VALID(40, "升级任务失效"),

        //中间状态码预留
        //UPGRADE_SUCCESS(101, "升级安装成功"),
        //UPGRADE_FAIL(102, "升级安装失败"),

        STOP_NO_PLAN(111, "未获取任务终止"),
        STOP_NO_DOWNLOAD(112, "未下载终止"),
        STOP_NO_INSTALLED(113, "未执行任务终止"),
        ;
        public static List<Map<String,Object>> typeDescList = new ArrayList<>();

        @Getter
        private int type;
        @Getter
        private String desc;

        TaskObjMonitoriedStatusTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * @param type
         * @return
         */
        public static TaskObjMonitoriedStatusTypeEnum getByType(int type) {
            return EnumSet.allOf(TaskObjMonitoriedStatusTypeEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }

        static {
            EnumSet<TaskObjMonitoriedStatusTypeEnum> objStatusTypeEnums = EnumSet.allOf(TaskObjMonitoriedStatusTypeEnum.class);
            for (TaskObjMonitoriedStatusTypeEnum statusTypeEnum:objStatusTypeEnums ){
                Map<String,Object> typeDescMap = new HashMap();
                typeDescMap.put("type",statusTypeEnum.getType());
                typeDescMap.put("desc",statusTypeEnum.getDesc());
                typeDescList.add(typeDescMap);
            }
        }
    }

    /**
     * 升级对象状态
     */
    public enum TaskObjStatusTypeEnum {
        /**
         *
         * 考虑到后续状态值增加的情况下，需要预留一部分状态码给到不同标识阶段
         * 0-10状态码：版本检查
         * 11-29状态码：下载过程
         * 30-100状态码：安装过程
         * 100以上状态码：安装结果
         */
        NO_VERSION(-1, "不存在有效任务，不存在新版本"),
        NO_VERSION_WITHCHECK(-2, "任务存在，不存在新版本"),

        //版本检查阶段状态
        CREATED(0, "新创建(TBOX端已发起检查)"),
        CHECK_VERIFY(1, "TBOX检测到新版本"),
        //该状态定义保留:可以通过下载确认请求去同步该状态
        CHECK_ACK_VERIFY(2, "TBOX已获取新版本结果下发信息（并已与OTA云端确认同步）"),

        /**
         * 【待下载】，表示已经获取任务信息，可以启动下载过程
         *  【下载中】，表示正在下载升级包
         *  【下载终⽌】，包括⽹络异常、数据异常等造成的升级包下载中断
         *  【下载等待】，表示建⽴通道连接，升级包下载前的状态
         *  【下载完成】，表示升级包下载完成，同时进⼊到安装包检验状态
         *  【下载成功】，表示升级包下载完成，安装包校验成功
         *  【下载失败】，表示升级包下载完成，安装包校验失败
         */

        //下载节点相关状态
        DOWNLOAD_ACK_WAIT(11, "待下载(新版本信息已下发到TBOX)"),
        DOWNLOAD_ACK_BEGIN(12, "开始下载(并已与OTA云端确认同步)"),
        DOWNLOAD_PROCESSING(13, "升级下载中"),
        DOWNLOAD_PROCESSING_STOP(14, "升级下载中止"),
        DOWNLOAD_PROCESSING_WAIT(15, "升级下载等待"),
        DOWNLOAD_COMPLETED(16, "TBOX执行下载完成"),
        DOWNLOAD_COMPLETED_SUCCESS(17, "TBOX执行下载完成(验证成功)"),
        DOWNLOAD_COMPLETED_FAIL(18, "TBOX执行下载完成(验证失败)"),
        /*DOWNLOAD_PRE_CHECK_FAIL(19, "下载前置条件检查失败(内存空间不足)"),*/

        //升级节点相关状态
        INSTALLED_ACK_WAIT(31, "待升级(TBOX端升级包已经下载成功，可以启动升级过程)"),
        INSTALLED_ACK_BEGIN(32, "开始升级(TBOX已收到升级安装指令,并已与OTA云端确认同步)"),
        INSTALLED_PRECHECK_FAIL(33, "前置条件检查失败"),
        INSTALLED_PREOPERATE_FROM_HU(34, "Hu端触发升级"),    //实际情况可能会不存在
        INSTALLED_CANCEL(35, "取消升级"),
        INSTALLED_PROCESSING(36, "执⾏升级(表示⻋辆具备升级条件，正式进⼊升级环节)"),
        INSTALLED_COMPLETED_SUCCESS(37, "升级完成成功(表示升级过程完成，同时升级的结果为升级成功)"),
        INSTALLED_COMPLETED_FAIL(38, "升级未完成(表示升级过程完成，回滚成功)"),
        INSTALLED_FAIL(39, "升级失败(表示升级过程完成，存在回滚失败的情况)"),
        INSTALLED_PLAN_INVALID(40, "升级任务失效(表示升级过程未完成，升级任务已失效)"),

        //中间状态码预留
        //UPGRADE_SUCCESS(101, "升级安装成功"),
        //UPGRADE_FAIL(102, "升级安装失败"),

        STOP_NO_PLAN(111, "未获取任务终止"),
        STOP_NO_DOWNLOAD(112, "未下载终止"),
        STOP_NO_INSTALLED(113, "未执行任务终止"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        TaskObjStatusTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * @param type
         * @return
         */
        public static TaskObjStatusTypeEnum getByType(int type) {
            return EnumSet.allOf(TaskObjStatusTypeEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }

        /**
         * 判断当前升级过程是否已经处于下载阶段确认之后的阶段
         * @param type
         * @return
         */
        public static boolean afterDownloadVerfiyResult(int type) {
            return type > TaskObjStatusTypeEnum.DOWNLOAD_ACK_WAIT.getType();
        }

        /**
         * 判断当前升级过程是否已经处于升级阶段确认之后的阶段
         * @param type
         * @return
         */
        public static boolean afterInstalledVerfiyResult(int type) {
            return type > TaskObjStatusTypeEnum.INSTALLED_ACK_WAIT.getType();
        }

        /**
         * 是否处于检查未完成阶段
         * @param type
         * @return
         */
        public static boolean beforeCheckVerfiy(int type) {
            return type < TaskObjStatusTypeEnum.CHECK_ACK_VERIFY.getType() && type >= TaskObjStatusTypeEnum.CREATED.getType();
        }

        /**
         * 升级流程已结束
         * @param type
         * @return
         */
        public static boolean isInstalledFinished(int type) {
            return type == TaskObjStatusTypeEnum.INSTALLED_COMPLETED_SUCCESS.getType()
                    /*|| type == TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.getType()*/
                    || type == TaskObjStatusTypeEnum.INSTALLED_FAIL.getType();
        }

        /**
         * 升级流程已结束---不包括失败情况
         * @param type
         * @return
         */
        public static boolean isInstalledFinishedExcludeFail(int type) {
            return type == TaskObjStatusTypeEnum.INSTALLED_COMPLETED_SUCCESS.getType()
                    || type == TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.getType();
        }

        /**
         * 是否为升级完成状态
         * @param type
         * @return
         */
        public static boolean isInstalledFinished(TaskObjStatusTypeEnum type) {
            return isInstalledFinished(type.getType());
        }

        /**
         * 安装成功
         * @param type
         * @return
         */
        public static boolean isInstalledSuccess(TaskObjStatusTypeEnum type) {
            return isInstalledSuccess(type.getType());
        }

        /**
         * 安装已经成功
         * @param type
         * @return
         */
        public static boolean isInstalledSuccess(Integer type) {
            return type.equals(TaskObjStatusTypeEnum.INSTALLED_COMPLETED_SUCCESS.getType());
        }

        /**
         * 升级任务已失效
         * @param type
         * @return
         */
        public static boolean isInstalledPlanInvalid(Integer type) {
            return type.equals(TaskObjStatusTypeEnum.INSTALLED_PLAN_INVALID.getType());
        }

        /**
         * 是否为下载完成状态
         * @param type
         * @return
         */
        public static boolean isDownloadFinished(int type) {
            return type == TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED.getType()
                    || type == TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_SUCCESS.getType()
                    || type == TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_FAIL.getType();
        }


        /**
         * 是否为下载完成
         * @param type
         * @return
         */
        public static boolean isDownloadFinished(TaskObjStatusTypeEnum type) {
            return isDownloadFinished(type.getType());
        }
    }

    /**
     * PlanTaskDetailStatus
     */
    public enum PlanTaskDetailStatusEnum {
        UPGRADE_NO(0, "-"),
        UPGRADE_SUCCESS(1, "完成"),
        UPGRADE_FAIL(2, "失败"),
        UPGRADE_UMCOMPLETED(3, "升级未完成");
        public static List<Map<String,Object>> typeDescList = new ArrayList<>();
        @Getter
        private int type;
        @Getter
        private String desc;

        PlanTaskDetailStatusEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取升级状态
         * @param type
         * @return
         */
        public static PlanTaskDetailStatusEnum getByType(Integer type){
            return EnumSet.allOf(PlanTaskDetailStatusEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }

        static {
            typeDescList = EnumSet.allOf(PlanTaskDetailStatusEnum.class).stream().map(item -> {
                Map<String,Object> typeDescMap = Maps.newHashMap();
                typeDescMap.put("type", item.getType());
                typeDescMap.put("desc", item.getDesc());
                return typeDescMap;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 统计方式枚举类型 ，1日报 2周报 3月报
     */
    public enum StatisticalMethodEnum {
        DAILY_REPORT(1, "日报"),
        WEEKLY_REPORT(2, "周报"),
        MONTHLY_REPORT(3, "月报");
        @Getter
        private int type;
        @Getter
        private String desc;

        StatisticalMethodEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取车辆升级任务状态
         * @param type
         * @return
         */
        public static StatisticalMethodEnum getByType(Integer type){
            return EnumSet.allOf(StatisticalMethodEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * 下载确认来源枚举
     */
    public enum DownloadVerifySourceEnum {
        FROM_HU(1, "HU操作确认"),
        FROM_TBOX(2, "TBOX自动下载"),
        FROM_APP(3, "APP操作确认");
        @Getter
        private int type;
        @Getter
        private String desc;

        DownloadVerifySourceEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取车辆升级任务状态
         * @param type
         * @return
         */
        public static DownloadVerifySourceEnum getByType(Integer type){
            return EnumSet.allOf(DownloadVerifySourceEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * 安装确认来源枚举
     */
    public enum InstalledVerifySourceEnum {
        FROM_HU(1, "HU操作确认"),
        FROM_APP(2, "APP操作确认");
        @Getter
        private int type;
        @Getter
        private String desc;

        InstalledVerifySourceEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    /**
     *
     */
    private static Map<DownloadProcessTypeEnum, TaskObjStatusTypeEnum> downloadProcess2TaskObjStatusTypeEnumMap = Maps.newHashMap();
    private static Map<InstalledProcessTypeEnum, TaskObjStatusTypeEnum> installedProcess2TaskObjStatusTypeEnum = Maps.newHashMap();
    private static Map<UpgradeResultTypeEnum, TaskObjStatusTypeEnum> installedCompleted2TaskObjStatusTypeEnum = Maps.newHashMap();
    static {
        downloadProcess2TaskObjStatusTypeEnumMap.put(DownloadProcessTypeEnum.DOWNLOADING, TaskObjStatusTypeEnum.DOWNLOAD_PROCESSING);
        downloadProcess2TaskObjStatusTypeEnumMap.put(DownloadProcessTypeEnum.DOWNLOAD_STOP, TaskObjStatusTypeEnum.DOWNLOAD_PROCESSING_STOP);
        downloadProcess2TaskObjStatusTypeEnumMap.put(DownloadProcessTypeEnum.DOWNLOAD_WAIT, TaskObjStatusTypeEnum.DOWNLOAD_PROCESSING_WAIT);
        downloadProcess2TaskObjStatusTypeEnumMap.put(DownloadProcessTypeEnum.DOWNLOAD_FINISHED, TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED);
        downloadProcess2TaskObjStatusTypeEnumMap.put(DownloadProcessTypeEnum.DOWNLOAD_FINISHED_SUCCESS, TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_SUCCESS);
        downloadProcess2TaskObjStatusTypeEnumMap.put(DownloadProcessTypeEnum.DOWNLOAD_FINISHED_FAIL, TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_FAIL);
        /*downloadProcess2TaskObjStatusTypeEnumMap.put(DownloadProcessTypeEnum.DOWNLOAD_PRE_CHECK_DISK_FREE_NOT_ENOUGH, TaskObjStatusTypeEnum.DOWNLOAD_PRE_CHECK_FAIL);*/


        installedProcess2TaskObjStatusTypeEnum.put(InstalledProcessTypeEnum.INSTALLED_CANCEL, TaskObjStatusTypeEnum.INSTALLED_CANCEL);
        installedProcess2TaskObjStatusTypeEnum.put(InstalledProcessTypeEnum.INSTALLED_PRECHECK_FAIL, TaskObjStatusTypeEnum.INSTALLED_PRECHECK_FAIL);
        installedProcess2TaskObjStatusTypeEnum.put(InstalledProcessTypeEnum.INSTALLED_PROCESSING, TaskObjStatusTypeEnum.INSTALLED_PROCESSING);installedProcess2TaskObjStatusTypeEnum.put(InstalledProcessTypeEnum.INSTALLED_CANCEL, TaskObjStatusTypeEnum.INSTALLED_CANCEL);
        installedProcess2TaskObjStatusTypeEnum.put(InstalledProcessTypeEnum.INSTALLED_START, TaskObjStatusTypeEnum.INSTALLED_PROCESSING);installedProcess2TaskObjStatusTypeEnum.put(InstalledProcessTypeEnum.INSTALLED_CANCEL, TaskObjStatusTypeEnum.INSTALLED_CANCEL);

        installedCompleted2TaskObjStatusTypeEnum.put(UpgradeResultTypeEnum.UPGRADE_COMPLETED, TaskObjStatusTypeEnum.INSTALLED_COMPLETED_SUCCESS);
        installedCompleted2TaskObjStatusTypeEnum.put(UpgradeResultTypeEnum.UPGRADE_UNCOMPLETED, TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL);
        installedCompleted2TaskObjStatusTypeEnum.put(UpgradeResultTypeEnum.UPGRADE_FAIL, TaskObjStatusTypeEnum.INSTALLED_FAIL);
    }

    /**
     *
     * @param type
     * @return
     */
    public static TaskObjStatusTypeEnum getTaskObjStatusTypeEnum(DownloadProcessTypeEnum type){
        return downloadProcess2TaskObjStatusTypeEnumMap.get(type);
    }

    /**
     *
     * @param type
     * @return
     */
    public static TaskObjStatusTypeEnum getTaskObjStatusTypeEnum(InstalledProcessTypeEnum type){
        return installedProcess2TaskObjStatusTypeEnum.get(type);
    }

    /**
     *
     * @param type
     * @return
     */
    public static TaskObjStatusTypeEnum getTaskObjStatusTypeEnum(UpgradeResultTypeEnum type){
        return installedCompleted2TaskObjStatusTypeEnum.get(type);
    }

    /**
     * 下载进度阶段枚举类型
     */
    public enum DownloadProcessTypeEnum{
        /*
        1=下载中（实时进度上报），2=下载中止，3=下载中等待，4=下载完成，5=下载成功（下载包校验成功），6=下载失败（下载包校验失败）
         */
        DOWNLOADING(1, "下载中（实时进度上报）"),
        DOWNLOAD_STOP(2, "下载中止"),
        DOWNLOAD_WAIT(3, "下载中等待"),
        DOWNLOAD_FINISHED(4, "下载完成"),
        DOWNLOAD_FINISHED_SUCCESS(5, "下载成功（下载包校验成功）"),
        DOWNLOAD_FINISHED_FAIL(6, "下载失败（下载包校验失败）"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        DownloadProcessTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举类型
         * @param type
         * @return
         */
        public static DownloadProcessTypeEnum getByType(Integer type){
            return EnumSet.allOf(DownloadProcessTypeEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }

        /**
         * 是否为完成态
         * @param type
         * @return
         */
        public static boolean isFinished(Integer type){
            return DOWNLOAD_FINISHED_SUCCESS.getType()==type || DOWNLOAD_FINISHED_FAIL.getType() == type;
        }

        /**
         * 是否失败
         * @param type
         * @return
         */
        public static boolean isFailed(DownloadProcessTypeEnum type){
            return DOWNLOAD_FINISHED_FAIL.getType() == type.getType();
        }

        /**
         * 是否为完成态
         * @param type
         * @return
         */
        public static boolean isFinished(DownloadProcessTypeEnum type){
            return isFinished(type.getType());
        }

        /**
         * 是否为下载中
         * @param type
         * @return
         */
        public static boolean isDownloading(DownloadProcessTypeEnum type){
            return DOWNLOADING.getType() == type.getType();
        }

        /**
         * 下载等待
         * @param type
         * @return
         */
        public static boolean isDownloadWait(DownloadProcessTypeEnum type){
            return DOWNLOAD_WAIT.getType() == type.getType();
        }
    }

    /**
     * 下载进度阶段枚举类型
     */
    public enum InstalledProcessTypeEnum{
        INSTALLED_PRECHECK_FAIL(1, "前置条件检查失败"),
        INSTALLED_PROCESSING(2, "执⾏升级(表示⻋辆具备升级条件，正式进⼊升级环节)"),
        INSTALLED_CANCEL(3, "取消升级"),
        INSTALLED_START(4, "开始升级"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        InstalledProcessTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举类型
         * @param type
         * @return
         */
        public static InstalledProcessTypeEnum getByType(Integer type) {
            return EnumSet.allOf(InstalledProcessTypeEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }
    }

    /**
     * 升级包构建类型
     */
    public enum BuildEnum {
        TYPE_DELTA(2, "delta"),
        TYPE_FULL(0, "full"),
        ;
        @Getter
        private int type;
        @Getter
        private String name;

        BuildEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public static BuildEnum getByType(int type) {
            return EnumSet.allOf(BuildEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }
    }

    /**
     * 升级包制作状态枚举
     */
    public enum BuildStatusEnum {
        //  包制作状态：0=未开始，1=制作中，2=制作成功，3=制作失败
        TYPE_FAIL(3, "制作失败"),
        TYPE_FINISH(2, "制作成功"),
        TYPE_BUILDING(1, "制作中"),
        TYPE_WAIT(0, "未开始"),
        ;

        @Getter
        private int type;
        @Getter
        private String name;

        BuildStatusEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public static BuildStatusEnum getByType(int type) {
            return EnumSet.allOf(BuildStatusEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }

    }
    
    public enum EncryptStatusEnum {
        //  包制作状态：0=未加密，1=加密中，2=加密成功，3=加密失败
        TYPE_FAIL(3, "加密失败"),
        TYPE_FINISH(2, "加密成功"),
        TYPE_BUILDING(1, "加密中"),
        TYPE_WAIT(0, "未加密"),
        ;

        @Getter
        private int type;
        @Getter
        private String name;

        EncryptStatusEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public static EncryptStatusEnum getByType(int type) {
            return EnumSet.allOf(EncryptStatusEnum.class).stream().filter(item -> item.type == type).findFirst().orElse(null);
        }

    }

    /**
     * @author fmq
     * @date 2020.08.26
     */
    public enum  CodeEnum {
        BUILD_SUCCESS(10000,"成功接收差分请求"),
        SOURCE_URL_IS_NULL(10001,"源版本sourceVersionUrl为空"),
        TARGET_URL_IS_NULL(10002,"目标版本targetVersionUrl为空"),
        SOURCE_HASH_IS_NULL(10003,"源版本Hash为空"),
        TARGET_HASH_IS_NULL(10004,"目标版本Hash为空"),
        TYPE_IS_NULL(10005,"type为空"),
        PACKAGE_NAME_IS_NULL(10006,"packageName为空"),
        TYPE_ILLEGAL(10007,"type不合法"),
        PACKAGE_ERROR(10008,"参数异常"),

        CREATE_SUCCESS(20000,"升级包制作成功"),
        PACKAGE_CREATING(20001,"升级包正在制作中"),
        PACKAGE_FAIL(20002,"升级包制作失败"),
        TASK_ID_IS_NULL(20003,"taskId为空"),
        TASK_ID_NOT_EXIST(20004,"taskId不存在"),
        FILE_HASH_NOT_SAME(20005,"文件hash与对象存储文件hash不一致"),
        FILE_FAIL_DOWN(20006,"版本包下载失败"),
        SOURCE_ANALYZE_FAIL(20017,"源软件包解析异常"),
        TARGET_ANALYZE_FAIL(20018,"目标软件包解析异常"),
        SCRIPT_FAIL(20009,"调用mkotapackage工具制作差分/整包失败"),
        PACKAGE_INFO_FAIL(20010,"调用mkotapackage工具打包用户数据（例如打包mcu版本）失败"),
        SOURCE_UNZIP_FAIL(20011,"用户源版本解压失败"),
        TARGET_UNZIP_FAIL(20012,"用户目标版本解压失败"),
        SOURCE_AND_TARGET_SAME(20013,"用户源版本与目标版本无差异"),

        ;
        private int code;
        private String comment;
        CodeEnum(int code,String comment){
            this.code = code;
            this.comment = comment;
        }

        public int getCode() {
            return code;
        }

        public String getComment() {
            return comment;
        }

        /**
         * 获取枚举值
         * @param code
         * @return
         */
        public static CodeEnum getByCode(Integer code){
            if(Objects.isNull(code)){
                return null;
            }
            return EnumSet.allOf(CodeEnum.class).stream().filter(item -> item.code == code.intValue()).findFirst().orElse(null);
        }
    }


    /**
     * 同步类型枚举
     */
    public enum DeviceSyncTypeEnum {
        CAR_INFO_ALL(1, 1, "车辆信息同步"),
        //CAR_INFO_INCR(1, 0, "车辆信息增量同步"),

        COMPONNET_INFO_ALL(2, 1, "车型零件信息全量同步"),
        //COMPONNET_INFO_INCR(2, 0, "车型零件信息增量同步"),

        TAG_INFO_ALL(3, 1, "标签信息全量同步"),
        //TAG_INFO_INCR(3, 0, "标签信息增量同步"),
        ;
        @Getter
        private Integer type;
        @Getter
        private Integer businessType;
        @Getter
        private String desc;

        DeviceSyncTypeEnum(Integer type,Integer businessType, String desc) {
            this.type = type;
            this.businessType = businessType;
            this.desc = desc;
        }

        /**
         *
         * @param type
         * @return
         */
        public static DeviceSyncTypeEnum getByType(Integer type) {
            return EnumSet.allOf(DeviceSyncTypeEnum.class).stream().filter(item -> item.type.equals(type) /*&& item.businessType.equals(businessType)*/).findFirst().orElse(null);
        }
    }

    /**
     * ota类型枚举
     */
    public enum OtaUpgradeMessageTypeEnum {
        NEW_VERSION(1, "OTA升级新版本消息"),

        OTHER(2, "OTA升级其他消息"),
        ;
        @Getter
        private Integer type;
        @Getter
        private String desc;

        OtaUpgradeMessageTypeEnum(Integer type,String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         *
         * @param type
         * @return
         */
        public static OtaUpgradeMessageTypeEnum getByType(Integer type) {
            return EnumSet.allOf(OtaUpgradeMessageTypeEnum.class).stream().filter(item -> item.type.equals(type)).findFirst().orElse(null);
        }
    }

    /**
     * 升级过程中发送消息到其他TOPIC消息的应用类型
     */
    public enum SendMsg2OtherTypeEnum{
        FOR_UPGRADE_PROCESS(1, "升级进度消息"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        SendMsg2OtherTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举类型
         * @param type
         * @return
         */
        public static SendMsg2OtherTypeEnum getByType(Integer type){
            return EnumSet.allOf(SendMsg2OtherTypeEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * ota类型枚举
     */
    public enum OtaUpgradeModeTypeEnum {
        FACTORY_MODE(1, "OTA升级新版本消息"),

        INTERACTIVE_MODE(2, "OTA升级其他消息"),

        DOWNLOAD_SILENCE_MODE(3, "静默下载"),
        ;
        @Getter
        private Integer type;
        @Getter
        private String desc;

        OtaUpgradeModeTypeEnum(Integer type,String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         *
         * @param type
         * @return
         */
        public static OtaUpgradeModeTypeEnum getByType(Integer type) {
            return EnumSet.allOf(OtaUpgradeModeTypeEnum.class).stream().filter(item -> item.type.equals(type)).findFirst().orElse(null);
        }
    }

    /**
     * ota类型枚举
     */
    public enum OtaUpgradePhaseTypeEnum {
        NEW_VERSION(1, "升级阶段"),

        DOWNLOAD(2, "下载阶段"),

        INSTALL(3, "安装阶段"),
        ;
        @Getter
        private Integer type;
        @Getter
        private String desc;

        OtaUpgradePhaseTypeEnum(Integer type,String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         *
         * @param type
         * @return
         */
        public static OtaUpgradePhaseTypeEnum getByType(Integer type) {
            return EnumSet.allOf(OtaUpgradePhaseTypeEnum.class).stream().filter(item -> item.type.equals(type)).findFirst().orElse(null);
        }
    }


    /**
     * 最终的安装状态
     */
    public enum InstalledCompletedStatusTypeEnum {
        INSTALLED_SUCCESS(1, "升级成功"),
        INSTALLED_UNCOMPLETED(2, "升级未完成"),
        INSTALLED_FAILD(3, "升级失败"),
        INSTALLED_TERMINATED(4, "升级终止"),
        ;

        @Getter
        private Integer type;
        @Getter
        private String desc;

        InstalledCompletedStatusTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 是否在枚举范围内
         *
         * @param type
         * @return
         */
        public static boolean contains(Integer type) {
            if (Objects.isNull(type)) {
                return false;
            }
            return EnumSet.allOf(InstalledCompletedStatusTypeEnum.class).stream().filter(item -> item.type == type).findFirst().isPresent();
        }
    }
}
