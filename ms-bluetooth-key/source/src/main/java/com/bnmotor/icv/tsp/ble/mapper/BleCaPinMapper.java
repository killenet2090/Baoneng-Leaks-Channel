package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleCaPinPo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDevicePinVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: BleCaPinMapper
 * @Description: 证书pin码业务用mybatis访问数据库的mapper文件
 * @author: shuqi1
 * @date: 2020/6/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface BleCaPinMapper extends BaseMapper<BleCaPinPo> {
    /**
     * 添加ping码数据
     * @param bleCaPinPo
     * @return
     */
    Integer addBlePin(BleCaPinPo bleCaPinPo);

    /**
     * 添加
     * @param bleCaPinPo
     * @return
     */
    Integer ackAddBlePin(BleCaPinPo bleCaPinPo);

    /**
     * 根据Capin对象，删除pin数据
     * @param bleCaPinPo
     * @return
     */
    Integer deleteBlePin(BleCaPinPo bleCaPinPo);

    /**
     * 根据设备ID查询蓝牙钥匙
     * @param bleCaPinPo
     * @return
     */
    List<BleCaPinPo> queryBlePins(BleCaPinPo bleCaPinPo);

    /**
     * 根据设备ID、用户类型查询指定pin码
     * @param projectId
     * @param bleDeviceId
     * @param userTypeId
     * @return
     */
    BleCaPinPo queryBlePin(Long projectId,Long bleDeviceId,Integer userTypeId);

    /**
     * 更新pin码
     * @param bleCaPinPo
     * @return
     */
    int updateBlePin(BleCaPinPo bleCaPinPo);

    /**
     * 返回msc和pin码连接信息
     * @param bleCaPinPo
     * @return
     */
    BleDevicePinVo queryBleDevicePins(BleCaPinPo bleCaPinPo);

}
