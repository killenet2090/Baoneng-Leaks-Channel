package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.resp.DeviceComponentVo;

import java.util.List;

/**
 * @ClassName: DeviceComponentPo
 * @Description: OTA升级硬件设备信息库 服务类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IDeviceComponentService{
    /**
     * 获取零件树下的零件节点列表
     * @param treeNodeId
     * @return
     */
    List<DeviceComponentVo> getDeviceComponentVosByTreeNodeId(long treeNodeId);

    /**
     * 获取零件树下的零件节点列表
     * @param treeNodeId
     * @return
     */
    //List<DeviceComponentPo> getDeviceComponentPosByTreeNodeId(long treeNodeId);
}
