package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckInfoPo;

/**
 * @ClassName: TspBleAckInfo
 * @Description:  服务类
 * @author shuqi1
 * @since 2020-09-16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface BleAckInfoService extends IService<BleAckInfoPo> {
      /**
     * 移动tbox上报历史数据
     * @return
     */
    int moveAckInfoData();


}
