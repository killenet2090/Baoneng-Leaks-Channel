package com.bnmotor.icv.tsp.device.common.enums.feign;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
* @ClassName: SmsFromTypeEnum
* @Description: 短信来源类型枚举
* @author: huangyun1
* @date: 2020/8/4
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public enum FromTypeEnum implements BaseEnum<Integer> {
   /**
    * 远控模块
    */
   REMOTE_CONTROL_MODEL(1, "远控模块"),
   /**
    * 蓝牙模块
    */
   BLUETOOTH_MODEL(2, "蓝牙模块"),
   /**
    * 用户模块
    */
   USER_MODEL(3, "用户模块"),
   /**
    * TSP后台模块
    */
   TSP_PLATFORM_MODEL(4, "TSP后台模块"),
   /**
    * 车辆设备模块
    */
   DEVICE_MODEL(5, "车辆设备模块");

   /**
    * 编码
    */
   private final Integer code;

   private final String desc;

   FromTypeEnum(Integer code, String desc) {
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
