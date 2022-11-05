package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @ClassName: TspBleAckPush
* @Description: 记录下发数据类型和流水号，用于消息推送 dao层
 * @author shuqi1
 * @since 2020-08-13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface BleAckPushMapper extends BaseMapper<BleAckPushPo> {
    /**
     * 增加需要推送的消息
     * @param bleAckPushPo
     * @return
     */
    int addBleAckPush(BleAckPushPo bleAckPushPo);
    /**
     * 删除推送响应的数据
     * @param bleAckPushPo
     * @return
     */
    int deleteBleAckPushById(BleAckPushPo bleAckPushPo);

    /**
     *批量删除蓝牙钥匙
     * @param bleAckPushPoList
     * @return
     */
    int deleteBleAckPushList(List<BleAckPushPo> bleAckPushPoList);
    /**
     * 移动数据到历史表
     * @param bleAckPushPoList
     * @return
     */
    int moveBleAckPushServiceList(List<BleAckPushPo> bleAckPushPoList);

    /**
     * 更新ack  status
     * @param bleAckPushPo
     * @return
     */
    int updateBleAckPushId(BleAckPushPo bleAckPushPo);

    /**
     * 更新数据状态
     * @param bleAckPushPo
     * @return
     */
    int updateBleAckPushAckStatus(BleAckPushPo bleAckPushPo);

    /**
     * 更新所有时间已经超过配置的最大时长了的数据
     * @param bleAckPushPo
     * @return
     */
    int updateOverTimeBlePushStatus(BleAckPushPo bleAckPushPo);

    /**
     * 查询需要推送的
     * @param bleAckPushPo
     * @return
     */
    BleAckPushPo queryBleAckPush(BleAckPushPo bleAckPushPo);

    /**
     * 查询需要推送的
     * @param bleAckPushPo
     * @return
     */
    List<BleAckPushPo> queryBleAckPushs(BleAckPushPo bleAckPushPo);

    /**
     *
     * @param bleAckPushPo
     * @return
     */
    List<BleAckPushPo> queryOverLimitBleAckPush(BleAckPushPo bleAckPushPo);

}
