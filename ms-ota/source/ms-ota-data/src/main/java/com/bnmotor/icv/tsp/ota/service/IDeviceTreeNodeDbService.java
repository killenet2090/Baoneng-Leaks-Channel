package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;

import java.util.List;

/**
 * @ClassName: DeviceTreeNodePo
 * @Description: 设备分类树
该信息从车辆数据库中同步过来 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IDeviceTreeNodeDbService extends IService<DeviceTreeNodePo> {
    /**
     * 返回子节点
     * @param parentId
     * @return
     */
    List<DeviceTreeNodePo> listChildren(Long parentId);
    
    /**
     * 获取设备树
     * @return
     */
    List<DeviceTreeNodePo> listAll();

    /**
     * 获取第一级节点列表
     * @return
     */
    List<DeviceTreeNodePo> listRoot();

    /**
     * 获取某一项目下某一层级所有节点
     * @param treeLevel
     * @return
     */
    List<DeviceTreeNodePo> listByTreeLevel(int treeLevel);

    /**
     * 根据nodeCodePath查找树节点
     * @param nodeCodePath
     * @return
     */
    DeviceTreeNodePo getByNodeCodePath(String nodeCodePath);

    /**
     * 根据nodeNamePath查找树节点
     * @param nodeCodePath
     * @return
     */
    DeviceTreeNodePo getByNodeNamePath(String nodeCodePath);

    /**
     * 获取顶级节点数据
     * @param topName
     * @return
     */
    DeviceTreeNodePo getByTopName(String topName);
}
