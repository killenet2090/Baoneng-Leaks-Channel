package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.DeviceTreeNodeMapper;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
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
public class DeviceTreeNodeDbServiceImpl extends ServiceImpl<DeviceTreeNodeMapper, DeviceTreeNodePo> implements IDeviceTreeNodeDbService {

    @Override
    public List<DeviceTreeNodePo> listChildren(Long parentId) {
        QueryWrapper<DeviceTreeNodePo> queryWrapper = new QueryWrapper<>();
        if(Objects.nonNull(parentId)) {
            queryWrapper.eq("parent_id", parentId);
        }else {
            queryWrapper.isNull("parent_id");
        }
        List<DeviceTreeNodePo> deviceTreeNodePos = this.list(queryWrapper);
        if(!CollectionUtils.isEmpty(deviceTreeNodePos)){
            for (DeviceTreeNodePo deviceTreeNodePo : deviceTreeNodePos) {
                wrapDeviceTreeNodeStr(deviceTreeNodePo);
            }
        }
        return MyCollectionUtil.safeList(deviceTreeNodePos);
    }

    @Override
    public List<DeviceTreeNodePo> listAll() {
        return list();
    }

    /*@Override
    public List<DeviceTreeNodePo> listAll() {
        Map<String, Object> paramMap = Maps.newHashMap();
        List<DeviceTreeNodePo> allDeviceTreeNodePos =  baseMapper.selectByMap(paramMap);
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
            List<DeviceTreeNodePo> rootdeviceTreeNodePos = allDeviceTreeNodePos.stream().filter(item -> deviceTreeNodePo.getId().equals(item.getRootNodeId())).collect(Collectors.toList());
            wrapDeviceTreeNode(deviceTreeNodePo, rootdeviceTreeNodePos*//*, 1 == includeLeafInt? DeviceTreeNodeLevelEnum.ECU.getLevel() : DeviceTreeNodeLevelEnum.CONF.getLevel()*//*);
        }

        return MyCollectionUtil.safeList(roots);
    }*/

    @Override
    public List<DeviceTreeNodePo> listRoot(/*String projectId*/) {
        QueryWrapper queryWrapper = new QueryWrapper();
        /*queryWrapper.eq("project_id", projectId);*/
        queryWrapper.isNull("parent_id");
        queryWrapper.isNull("root_node_id");
        return MyCollectionUtil.safeList(list(queryWrapper));
    }

    @Override
    public List<DeviceTreeNodePo> listByTreeLevel(int treeLevel) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("tree_level", treeLevel);
        return MyCollectionUtil.safeList(list(queryWrapper));
    }

    @Override
    public DeviceTreeNodePo getByNodeCodePath(String nodeCodePath) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("node_code_path", nodeCodePath);
        return getOne(queryWrapper);
    }

    @Override
    public DeviceTreeNodePo getByNodeNamePath(String nodeNamePath) {
        if(StringUtils.isEmpty(nodeNamePath)){
            log.warn("nodeNamePath is null, please check it!");
            return null;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("node_name_path", nodeNamePath);
        return getOne(queryWrapper);
    }

    @Override
    public DeviceTreeNodePo getByTopName(String topName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("node_name", topName);
        queryWrapper.isNull("parent_id");
        queryWrapper.isNull("root_node_id");
        List<DeviceTreeNodePo> deviceTreeNodePos = list(queryWrapper);
        return MyCollectionUtil.isNotEmpty(deviceTreeNodePos) ? deviceTreeNodePos.get(0) : null;
    }

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
                wrapDeviceTreeNode(nodePo, rootDeviceTreeNodePos/*, maxTreeLevel*/);
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
