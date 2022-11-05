package com.bnmotor.icv.tsp.ble.service;

import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyQueryDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUidKeyDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyQueryVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleUidKey;
import com.bnmotor.icv.tsp.ble.model.response.ble.UserBleKeyVo;

import java.util.List;

/**
 * @ClassName: BleKeyQueryService
 * @Description: 蓝牙钥匙查询服务接口定义
 * @author: liuyiwei
 * @date: 2020/7/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface BleKeyQueryService {

    /**
     * 根据车辆设备ID和车主用户ID查询车主蓝牙钥匙记录
     *
     * @param userId
     * @param projectId
     * @param queryDto
     * @return
     */
    List<BleKeyQueryVo> queryByVehOwner(String userId, String projectId, BleKeyQueryDto queryDto);

    /**
     * 从TBOX端查询车辆下有哪些钥匙
     * @param userId
     * @param projectId
     * @param querydto
     * @return
     */
    BleKeyQueryDto queryCheckByVehOwner(String userId, String projectId, BleKeyQueryDto querydto);

    /**
     * 从TBOX端查询指定钥匙
     * @param userId
     * @param projectId
     * @param querydto
     * @return
     */
    BleKeyDto queryCheckSpeciByVehOwner(String userId, String projectId, BleKeyDto querydto);

    /**
     * 从车端查询所有蓝牙钥匙
     * @param userId
     * @param projectId
     * @param deviceId
     * @return
     */
    String queryCheckAllByVehOwner(String userId, String projectId, String deviceId);

    /**
     * 根据车辆设备ID和车主用户ID查询车主蓝牙钥匙记录
     * @param bleUidKeyDto
     * @return
     */
    List<BleUidKey> queryBleUids(BleUidKeyDto bleUidKeyDto);

    /**
     * 检查过期的蓝牙钥匙数据
     * @param projectId
     * @param deviceId
     * @return
     */
    void checkExpireHisData(String projectId, String deviceId);

    /**
     *清除过期的蓝牙钥匙数据
     * @param elem
     */
    void cleanExpireHisData(BleAuthPo elem);

    /**
     * 清楚车主申请未过期的钥匙
     * @param projectId
     * @param deviceId
     * @param uid
     */
    void checkOwnerExpireHisData(String projectId, String deviceId,String uid);

}
