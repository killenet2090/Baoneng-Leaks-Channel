package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeDbService;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: DeviceTreeNodePo
 * @Description: 设备分类树
该信息从车辆数据库中同步过来 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class DeviceTreeNodeServiceImpl implements IDeviceTreeNodeService {
    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    /*@Override
    public List<DeviceTreeNodePo> listChildren(Long parentId) {
        return deviceTreeNodeDbService.listChildren(parentId);
    }*/

    @Override
    public List<DeviceTreeNodePo> listAll() {
        List<DeviceTreeNodePo> allDeviceTreeNodePos =  deviceTreeNodeDbService.listAll();
        if(CollectionUtils.isEmpty(allDeviceTreeNodePos)){
            log.warn("alldeviceTreeNodePos.size=0, please check database");
            return null;
        }
        log.info("alldeviceTreeNodePos.size={}", allDeviceTreeNodePos.size());

        List<DeviceTreeNodePo> roots = allDeviceTreeNodePos.stream().filter( item -> StringUtils.isEmpty(item.getParentId()) && StringUtils.isEmpty(item.getRootNodeId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(roots)){
            log.warn("root deviceTreeNode error, please check database");
            return null;
        }

        //loop do
        for(DeviceTreeNodePo deviceTreeNodePo : roots){
            //此处需要排除root节点
            wrapDeviceTreeNodeStr(deviceTreeNodePo);
            List<DeviceTreeNodePo> rootDeviceTreeNodePos = allDeviceTreeNodePos.stream().filter(item -> deviceTreeNodePo.getId().equals(item.getRootNodeId())).collect(Collectors.toList());
            wrapDeviceTreeNode(deviceTreeNodePo, rootDeviceTreeNodePos);
        }

        return MyCollectionUtil.safeList(roots);
    }

    /*@Override
    public List<DeviceTreeNodePo> listRoot() {
        return deviceTreeNodeDbService.listRoot();
    }

    @Override
    public List<DeviceTreeNodePo> listByTreeLevel(int treeLevel) {
        return deviceTreeNodeDbService.listByTreeLevel(treeLevel);
    }

    @Override
    public DeviceTreeNodePo getByNodeCodePath(String nodeCodePath) {
        return deviceTreeNodeDbService.getByNodeCodePath(nodeCodePath);
    }

    @Override
    public DeviceTreeNodePo getByNodeNamePath(String nodeNamePath) {
        return deviceTreeNodeDbService.getByNodeNamePath(nodeNamePath);
    }

    @Override
    public DeviceTreeNodePo getByTopName(String topName) {
        return deviceTreeNodeDbService.getByTopName(topName);
    }*/

    /**
     * 递归包装结果集
     * @param parentDeviceTreeNodePo
     * @param rootDeviceTreeNodePos
     */
    private void wrapDeviceTreeNode(DeviceTreeNodePo parentDeviceTreeNodePo, List<DeviceTreeNodePo> rootDeviceTreeNodePos/*, int maxTreeLevel*/){
        //如果到达最大的树深度，不需要继续计算下级子节点
        /*if(maxTreeLevel<= 0 *//*|| parentDeviceTreeNodePo.getTreeLevel() >= maxTreeLevel*//*){
            return;
        }*/
        List<DeviceTreeNodePo> childDeviceTreeNodePos = rootDeviceTreeNodePos.stream().filter(item -> item.getParentId().equals(parentDeviceTreeNodePo.getId())).collect(Collectors.toList());
        if(MyCollectionUtil.isNotEmpty(childDeviceTreeNodePos)){
            childDeviceTreeNodePos.forEach(item -> wrapDeviceTreeNodeStr(item));
            parentDeviceTreeNodePo.setChildrenDeviceTreeNodeDos(childDeviceTreeNodePos);

            for(DeviceTreeNodePo nodePo :childDeviceTreeNodePos){
                wrapDeviceTreeNode(nodePo, rootDeviceTreeNodePos);
            }
        }
    }

    /**
     * 包装到对象相关id属性返回到前端
     * @param deviceTreeNodePo
     */
    private void wrapDeviceTreeNodeStr(final DeviceTreeNodePo deviceTreeNodePo){
        if(Objects.nonNull(deviceTreeNodePo)){
            deviceTreeNodePo.setIdStr(Long.toString(deviceTreeNodePo.getId()));
            deviceTreeNodePo.setParentIdStr(Objects.nonNull(deviceTreeNodePo.getParentId()) ? Long.toString(deviceTreeNodePo.getParentId()) : null);
            deviceTreeNodePo.setRootNodeIdStr(Objects.nonNull(deviceTreeNodePo.getRootNodeId()) ? Long.toString(deviceTreeNodePo.getRootNodeId()) : null);
        }
    }
}
