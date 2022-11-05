package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyVo;

import java.util.List;

/**
 * @ClassName: BleAuthService
 * @Description: 蓝牙钥匙授权接口
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleAuthMapService extends IService<BleAuthServicePo> {
    /**
     * 修改蓝牙钥匙授权信息
     * @param bleKeyServicePoList
     * @return
     */
    BleKeyModifyVo reviseBleKeyAuthInfo(List<BleKeyServicePo> bleKeyServicePoList);

    /**
     * 添加蓝牙钥匙授权权限信息
     * @param bleAuthServicePo
     * @return
     */

    int addBleKeyAuthMapInfo(BleAuthServicePo bleAuthServicePo);

    /**
     * 添加蓝牙钥匙授权权限信息
     * @param bleAuthServicePoList
     * @return
     */
    int addBleKeyAuthMapInfo(List<BleAuthServicePo> bleAuthServicePoList);

    /**
     * 更新蓝牙钥匙授权权限信息
     * @param bleAuthServicePo
     * @return
     */
    int updateBleKeyAuthMapInfo(BleAuthServicePo bleAuthServicePo);

    /**
     * 删除蓝牙钥匙授权权限信息
     * @param bleAuthServicePoList
     * @return
     */
    int delBleKeyAuthMapInfo(List<BleAuthPo> bleAuthServicePoList);

}
