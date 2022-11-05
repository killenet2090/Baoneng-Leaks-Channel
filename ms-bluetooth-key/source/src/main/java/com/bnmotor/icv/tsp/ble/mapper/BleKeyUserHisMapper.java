package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyHisPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: UserBleKey
* @Description: 用户蓝牙钥匙
记录用户申请到的蓝牙钥匙 dao层
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface BleKeyUserHisMapper extends BaseMapper<UserBleKeyHisPo> {
    /**
     * 根据蓝牙实体类查询蓝牙信息
     * @param userBleKeyPo
     * @return
     */
    UserBleKeyPo queryBleKeyInfo(UserBleKeyPo userBleKeyPo);

    /**
     * 根据条件查询多把蓝牙钥匙
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyHisPo> queryDeviceAllBles(UserBleKeyPo userBleKeyPo);
}
