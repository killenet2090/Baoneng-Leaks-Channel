package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleCaPinPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleCaPinDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleDevicePinDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDevicePinVo;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: BleAuthService
 * @Description: 蓝牙钥匙证书pin码接口
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleCaPinService  extends IService<BleCaPinPo> {

    /**
     * 生成pin码信息
     * @param bleCaPinDto
     * @param userId
     * @return
     */
    BleCaPinPo generateCaPinInfo(BleCaPinDto bleCaPinDto, String userId);

    /**
     * 生成pin码信息
     * @param tspBleCaPinPo
     * @param bleDevicePinDto
     * @param userId
     * @return
     */
    List<BleCaPinPo> generateDevicePinInfo(Optional<List<BleCaPinPo>> tspBleCaPinPo,BleDevicePinDto bleDevicePinDto, String userId);

    /**
     * 查询pin码
     * @param bleCaPinDto
     * @return
     */
   BleCaPinPo queryPinInfo(BleCaPinDto bleCaPinDto);

    /**
     * 查询批量pin码
     * @param bleDevicePinDto
     * @return
     */
   List<BleCaPinPo> queryDevicePinInfo(BleDevicePinDto bleDevicePinDto);

    /**
     * 根据条件查询pin码
     * @param deviceId
     * @return
     */
    List<BleCaPinPo> blekeyCaPinQuery(String deviceId);

    /**
     * 增加pin码
     * @param bleCaPinPo
     * @return
     */
    Integer addCaPinInfo(BleCaPinPo  bleCaPinPo);

    /**
     * 删除pin码
     * @param bleCaPinDto
     * @return
     */
    int delCaPinInfo(BleCaPinDto bleCaPinDto);

    /**
     * 更新pin码
     * @param bleCaPinPo
     * @return
     */
    BleCaPinPo updateCaPinInfo(BleCaPinPo bleCaPinPo);

    /**
     * 更新车下的所有pin码
     * @param bleCaPinPoList
     * @return
     */
    Integer updateDevicePinInfo(List<BleCaPinPo> bleCaPinPoList);

    /**
     * 返回msc和pin码连接信息
     * @param bleCaPinPo
     * @return
     */
    BleDevicePinVo queryBleDevicePins(BleCaPinPo bleCaPinPo);

}
