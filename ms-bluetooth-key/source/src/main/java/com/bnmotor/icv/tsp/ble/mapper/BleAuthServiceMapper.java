package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthServicePo;

import java.util.HashMap;
import java.util.List;


/**
 * @ClassName: BleAuthServiceMapper
 * @Description: 蓝牙钥匙授权与权限服务的映射mapper文件
 * @author: shuqi1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleAuthServiceMapper extends BaseMapper<BleAuthServicePo> {
   /**
    * 根据项目ID、授权ID删除授权信息
    * @param hashMap
    * @return
    */
   List queryAuthServiceById(HashMap hashMap);

   /**
    * 增加蓝牙钥匙授权
    * @param bleAuthServicePo
    * @return
    */
   List<BleAuthServicePo> queryBleAuthService(BleAuthServicePo bleAuthServicePo);
   /**
    * 增加蓝牙钥匙授权
    * @param bleAuthServicePo
    * @return
    */
   int addBleAuthService(BleAuthServicePo bleAuthServicePo);

   /**
    * 更新授权信息
    * @param bleAuthServicePo
    * @return
    */
   int updateBleAuthService(BleAuthServicePo bleAuthServicePo);

   /**
    * 删除授权信息
    * @param bleAuthPo
    * @return
    */
   int delBleAuthService(BleAuthPo bleAuthPo);

   /**
    * 根据serviceID来删除
    * @param bleAuthServicePo
    * @return
    */
   int delBleAuthServiceData(BleAuthServicePo bleAuthServicePo);

   /**
    * 删除多条授权信息
    * @param bleAuthServicePoList
    * @return
    */
   int delBleAuthServiceList(List<BleAuthPo> bleAuthServicePoList);




}
