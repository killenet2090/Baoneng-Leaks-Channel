package com.bnmotor.icv.tsp.device.common.enums.sms;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
* @ClassName: MsgFromTypeEnum
* @Description: 消息来源类型枚举
* @author: huangyun1
* @date: 2020/8/4
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public enum MsgFromTypeEnum implements BaseEnum<Integer> {
   /**
    * 管理平台
    */
   MANAGER_PLAT_MSG(0, "管理平台"),
   /**
    * 远控信息
    */
   REMOTE_CONTROL_MSG(1, "远控信息"),
   /**
    * 蓝牙钥匙
    */
   BLUETOOTH_MSG(2, "蓝牙钥匙"),
   /**
    * ota升级信息
    */
   OTA_MSG(3, "ota升级信息"),
   /**
    * 车辆设置
    */
   VEHICLE_SETTING(5, "车辆设置"),
   ;

   /**
    * 编码
    */
   private final Integer code;

   private final String desc;

   MsgFromTypeEnum(Integer code, String desc) {
       this.code = code;
       this.desc = desc;
   }


   @Override
   public String getDescription() {
       return this.desc;
   }

   @Override
   public Integer getValue() {
       return this.code;
   }
}
