package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Objects;

/**
 * @ClassName: AppEnums
 * @Description:    APP客户端请求枚举类集合
 * @author: xuxiaochang1
 * @date: 2020/9/4 15:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class AppEnums {
    private AppEnums(){}

    /**
     * 版本检查结果枚举
     */
    public enum CheckVersionResultEnum {
        /**
         * 存在新版本，需要异步等待TBOX完成检查请求
         */
        NEW_VERSION(1, "存在新版本，需要异步等待TBOX完成检查请求"),

        /**
         * 不存在新版本
         */
        NO_VERSION(2, "不存在新版本");
        @Getter
        private int type;
        @Getter
        private String desc;

        CheckVersionResultEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举值
         * @param type
         * @return
         */
        public static AppEnums.CheckVersionResultEnum getByType(Integer type){
            return EnumSet.allOf(AppEnums.CheckVersionResultEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * 安装类型
     */
    public enum InstalledType4AppEnum {
        INSTALLED_IMI(1, "立即安装"),
        INSTALLED_BOOKED(2, "预约安装");
        @Getter
        private int type;
        @Getter
        private String desc;

        InstalledType4AppEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举值
         * @param type
         * @return
         */
        public static AppEnums.InstalledType4AppEnum getByType(Integer type){
            return EnumSet.allOf(AppEnums.InstalledType4AppEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * 客户端数据推送类型
     */
    public enum AppReqTypeEnum {
        NEW_VERSION(1, "新版本检查"),
        REMOTED_DOWNLOAD(2, "远程下载"),
        REMOTED_INSTALLED(3, "远程安装"),
        CANCEL_INSTALLED_BOOKED(4, "取消预约安装"),
        ;
        @Getter
        private int type;
        @Getter
        private String desc;

        AppReqTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举值
         * @param type
         * @return
         */
        public static AppEnums.AppReqTypeEnum getByType(Integer type){
            return EnumSet.allOf(AppEnums.AppReqTypeEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * 客户端数据推送类型
     */
    public enum AppResponseTypeEnum {
        DOWNLOAD_VERIFIED_REQ(1, "远程下载确认请求(对应新版本检查结果响应)"),
        INSTALLED_VERIFIED_REQ(2, "远程安装确认请求"),
        DOWNLOAD_VERIFIED_RESPONSE(3, "远程下载结果响应"),
        INSTALLED_VERIFIED_RESPONSE(4, "远程安装结果响应"),
        UPGRADE_TERMINATE_RESPONSE(5, "升级安装终止响应"),
        ;
        @Getter
        private int type;
        @Getter
        private String desc;

        AppResponseTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举值
         * @param type
         * @return
         */
        public static AppEnums.AppResponseTypeEnum getByType(Integer type){
            return EnumSet.allOf(AppEnums.AppResponseTypeEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * 操作客户端来源来源
     */
    public enum ClientSourceTypeEnum {
        FROM_HU(1, "来自Hu"),
        FROM_TBOX(2, "来自TBOX"),
        FROM_APP(3, "来自APP"),
        FROM_OTA(4, "来自OTA"),
        ;
        @Getter
        private int type;
        @Getter
        private String desc;

        ClientSourceTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举值
         * @param type
         * @return
         */
        public static ClientSourceTypeEnum getByType(Integer type){
            return EnumSet.allOf(ClientSourceTypeEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }


    /**
     * 下载进度阶段枚举类型
     */
    public enum DownloadProcessType4AppEnum {
        DOWNLOAD_VERIFY_RESULT(1, "下载确认结果(是否开始下载)"),
        DOWNLOAD_FINISHED_SUCCESS(2, "下载成功（下载包校验成功）"),
        DOWNLOAD_FINISHED_FAIL(3, "下载失败（下载包校验失败）"),
        DOWNLOAD_PROGRESS(4, "下载中进度"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        DownloadProcessType4AppEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举类型
         * @param type
         * @return
         */
        public static AppEnums.DownloadProcessType4AppEnum getByType(Integer type){
            return EnumSet.allOf(AppEnums.DownloadProcessType4AppEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }


    /**
     * 下载进度阶段枚举类型
     */
    public enum InstalledProcessType4AppEnum{
        /**
         * 安装阶段：1=立即安装确认指令下发确认结果，2=预约安装确认指令下发确认结果，3=前置条件检查异常，4=取消安装（来自Hu）, 5=安装进度信息，6=安装结果
         */
        INSTALLED_VERIFY_RESULT(1, "立即安装确认指令下发确认结果"),
        INSTALLED_BOOKED_VERIFY_RESULT(2, "预约安装确认指令下发确认结果"),
        INSTALLED_PRECHECKED_FAIL(3, "前置条件检查异常"),
        INSTALLED_CANCEL(4, "取消安装"),
        INSTALLED_PROCESSING(5, "安装进度信息"),
        INSTALLED_RESULT(6, "安装结果"),
        INSTALLED_START(7, "安装开始"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        InstalledProcessType4AppEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        /**
         * 获取枚举类型
         * @param type
         * @return
         */
        public static AppEnums.InstalledProcessType4AppEnum getByType(Integer type){
            return EnumSet.allOf(AppEnums.InstalledProcessType4AppEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }
    }

    /**
     * 字典枚举类型
     */
    public enum CheckVersionStatusType4AppEnum {
        /**
         * FLASH_SCRIPT
         */
        WAIT_CHECK(0, "需要检测"),
        DOWNLOADING(1, "下载中"),
        DOWNLOAD_COMPLETE_SUCCESS(2, "下载成功，未安装"),
        DOWNLOAD_COMPLETE_FAIL(3, "下载完成，校验失败"),
        INSTALLED_VERIFY_WITH_BOOKED_TIME(4, "下载成功，预约安装"),
        INSTALLING(5, "安装中"),
        INSTALLED_FAIL(6, "安装失败"),
        ;

        @Getter
        private int type;
        @Getter
        private String desc;

        CheckVersionStatusType4AppEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    /**
     * OTA系统在消息中心定义的类型
     */
    public enum MessageCenterMsgTypeEnum {
        /**
         * 下载中断提醒：
         * 您的车辆${licensePlateNumber}软件下载中断，请留意。
         * template_id：1358258716972929026
         * category_id：1303211739319786324
         *
         * 预约升级开始提醒：
         * 您的车辆${licensePlateNumber}已于${appointmentTime}开始升级。
         * category_id：1303211739319786325
         * template_id：1358258716972929027
         */


        NEW_VERSION(1303211738509786361L, 1303211738512732565L, "新版本通知", "您的车辆{}有可用的软件更新"),
        DOWNLOAD_COMPLETE_SUCCESS(1303211738509786362L, 1303211738512732566L, "下载完成通知", "您的车辆{}软件下载完成，可立即安装或预约安装"),
        DOWNLOAD_COMPLETE_FAIL_WITHOUT_ENOUGH_SPACE(1303211738509786363L, 1303211738512732567L, "下载失败通知", "您的车辆{}软件下载失败，请重新下载或者联系客服处理"),
        DOWNLOAD_COMPLETE_FAIL(1303211738509786363L, 1303211738512732568L, "下载失败通知", "您的车辆{}软件下载失败，请重新下载或者联系客服处理"),
        DOWNLOAD_WAIT(1303211739319786324L, 1358258716972929026L, "下载中断通知", "您的车辆{}软件下载中断，请留意"),
        INSTALLED_BOOKED_FAIL(1303211738509786364L, 1303211738512732569L, "预约安装失败", "您的车辆{}软件预约安装失败，请重新预约"),
        INSTALLED_COMLETE_FAIL(1303211738609786365L, 1303211738512732571L, "升级失败通知", "您的车辆{}升级失败，可以尝试重新升级"),
        INSTALLED_FAIL(1303211738609786365L, 1303211738512732572L, "升级失败通知", "您的车辆{}升级失败，请勿使用车辆，请尽快联系客服处理"),
        INSTALLED_COMLETE_SUCCESS(1303211738609786366L, 1303211738512732573L, "升级成功通知", "您的车辆{}软件已经升级完成，全新体验等您体验"),
        INSTALLED_BOOKED_START(1303211739319786325L, 1358258716972929027L, "预约升级开始通知", "您的车辆{}已于{}开始升级"),
        ;

        @Getter
        private long type;

        @Getter
        private long templateId;
        @Getter
        private String desc;

        @Getter
        private String hint;

        MessageCenterMsgTypeEnum(long type, long templateId, String desc, String hint) {
            this.type = type;
            this.templateId = templateId;
            this.desc = desc;
            this.hint = hint;
        }

        /**
         * 获取枚举类型
         * @param type
         * @return
         */
        public static AppEnums.MessageCenterMsgTypeEnum getByType(Long type){
            return EnumSet.allOf(AppEnums.MessageCenterMsgTypeEnum.class).stream().filter(item -> item.type==type).findFirst().orElse(null);
        }

        /**
         *
         * 推送到APP的消息形式
         * 21. APP-通知栏
         * 22. APP-弹窗
         * 23. APP-站内信
         *
         * @param messageCenterMsgTypeEnum
         * @return
         */
        public static String messageCenterMsgType2Way(MessageCenterMsgTypeEnum messageCenterMsgTypeEnum){
            if(Objects.isNull(messageCenterMsgTypeEnum)){
                throw new IllegalArgumentException("消息通知类型异常");
            }
            if(messageCenterMsgTypeEnum.equals(MessageCenterMsgTypeEnum.INSTALLED_COMLETE_SUCCESS)){
                return "23";
            }else{
                return "21";
            }
        }
    }
}
