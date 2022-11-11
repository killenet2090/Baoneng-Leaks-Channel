package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.DeviceFirmwareListPo;
import com.bnmotor.icv.tsp.ota.mapper.DeviceFirmwareListMapper;
import com.bnmotor.icv.tsp.ota.service.IDeviceFirmwareListDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName: DeviceFirmwareListPo
 * @Description: OTA升级硬件与固件关联清单 服务实现类
 * @author xuxiaochang1
 * @since 2020-11-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class DeviceFirmwareListDbServiceImpl extends ServiceImpl<DeviceFirmwareListMapper, DeviceFirmwareListPo> implements IDeviceFirmwareListDbService {

}
