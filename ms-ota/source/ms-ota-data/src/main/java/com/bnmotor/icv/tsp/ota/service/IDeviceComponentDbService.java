package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;

import java.util.List;

/**
 * @ClassName: DeviceComponentPo
 * @Description: OTA升级硬件设备信息库 服务类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IDeviceComponentDbService extends IService<DeviceComponentPo> {
    /**
     * 获取零件树下的零件节点列表
     * @param treeNodeId
     * @return
     */
    List<DeviceComponentPo> getDeviceComponentPosByTreeNodeId(long treeNodeId);

    /**
     * 获取零件信息
     * @param componentCode
     * @param componentModel
     * @return
     */
    DeviceComponentPo findOne(String componentCode, String componentModel);
}
