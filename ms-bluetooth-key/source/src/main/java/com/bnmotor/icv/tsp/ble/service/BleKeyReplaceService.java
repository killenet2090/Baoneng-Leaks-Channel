package com.bnmotor.icv.tsp.ble.service;

import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleReplaceResultVo;

/**
 * @ClassName: BleKeyReplaceService
 * @Description: 描述类的作用
 * @author: liuyiwei
 * @date: 2020/7/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface BleKeyReplaceService {

    /**
     * 从数据查询，同时包含判断是否存在
     * @param userBleKeyPo
     * @return
     */
    UserBleKeyPo queryBleKeyInfo(UserBleKeyPo userBleKeyPo);

    /**
     * 判断蓝牙钥匙刷新时间是否已经超过3个月
     * @param userBleKeyPo
     * @return
     */
    Long querySubBleKeyRefresh(UserBleKeyPo userBleKeyPo);

    /**
     * 生成原蓝牙钥匙， 复制原蓝牙钥匙相关信息及关联权限； 失效原蓝牙钥匙
     *
     * @param dbUserBleKeyPo
     */
    BleReplaceResultVo updateBleKey(UserBleKeyPo dbUserBleKeyPo,String projectId,String userId);

    /**
     * 更新授权信息
     * @param tboxJson
     * @param kafkaJson
     */
    void SendAuthUpdate(String tboxJson,String kafkaJson);

}
