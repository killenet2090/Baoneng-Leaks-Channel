package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaComponentListMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaComponentListPo;
import com.bnmotor.icv.tsp.ota.service.IFotaComponentListDbService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FotaComponentListPo
 * @Description: OTA升级硬件设备信息关系表 服务实现类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaComponentListDbServiceImpl extends ServiceImpl<FotaComponentListMapper, FotaComponentListPo> implements IFotaComponentListDbService {

    @Override
    public List<FotaComponentListPo> listByTreeNodeIds(long treeNodeId) {
        QueryWrapper<FotaComponentListPo> queryWrapper = new QueryWrapper<FotaComponentListPo>();
        queryWrapper.eq("tree_node_id", treeNodeId);
        return list(queryWrapper);
    }

    @Override
    public FotaComponentListPo findOne(Long treeNodeId, Long deviceComponentId) {
        QueryWrapper<FotaComponentListPo> fotaComponentListPoQueryWrapper = new QueryWrapper<>();
        fotaComponentListPoQueryWrapper.eq("tree_node_id", treeNodeId);
        fotaComponentListPoQueryWrapper.eq("device_component_id", deviceComponentId);
        return getOne(fotaComponentListPoQueryWrapper);
    }
}
