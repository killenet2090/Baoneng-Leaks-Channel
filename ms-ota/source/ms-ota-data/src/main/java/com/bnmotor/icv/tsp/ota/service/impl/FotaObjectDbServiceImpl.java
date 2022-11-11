package com.bnmotor.icv.tsp.ota.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaObjectMapper;
import com.bnmotor.icv.tsp.ota.model.compose.RegionInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaObjectServiceImpl
 * @Description: OTA升级对象指需要升级的一个完整对象，在车联网中指一辆车通常拿车的vin作为升级的ID服务实现类
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaObjectDbServiceImpl extends ServiceImpl<FotaObjectMapper, FotaObjectPo> implements IFotaObjectDbService {
    
	@Override
    public IPage<FotaObjectPo> listPageByTreeNodeId(long treeNodeId, int offset, int pageSize) {
        MyAssertUtil.isTrue(treeNodeId >0 , OTARespCodeEnum.DATA_NOT_FOUND);
        Page page = new Page(offset, pageSize);
        QueryWrapper<FotaObjectPo> queryWrapper = new QueryWrapper<FotaObjectPo>();
        queryWrapper.eq("tree_node_id", treeNodeId);
        queryWrapper.orderByDesc("create_time");
        IPage<FotaObjectPo> fotaPlanPos = getBaseMapper().selectPage(page, queryWrapper);
        return fotaPlanPos;
    }

    @Override
    public List<FotaObjectPo> listAllByTreeNodeId(long treeNodeId) {
        MyAssertUtil.isTrue(treeNodeId >0 , OTARespCodeEnum.DATA_NOT_FOUND);
        QueryWrapper<FotaObjectPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tree_node_id", treeNodeId);
        queryWrapper.orderByDesc("create_time");
        List<FotaObjectPo> fotaPlanPos = list(queryWrapper);
        return fotaPlanPos;
    }

    @Override
    public FotaObjectPo findByObjectId(String objectId) {
        MyAssertUtil.isTrue(!StringUtils.isEmpty(objectId), OTARespCodeEnum.DATA_NOT_FOUND);
        QueryWrapper<FotaObjectPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_id", objectId);
        return getOne(queryWrapper);
    }

    @Override
    public FotaObjectPo findByVin(String vin) {
        return findByObjectId(vin);
    }
    
    @Override
    public List<RegionInfo> listRegion(Long treeNodeId) {
        List<RegionInfo> fotaPlanPos = ((FotaObjectMapper)getBaseMapper()).listRegion(treeNodeId);
        return fotaPlanPos;
    }

	@Override
	public List<FotaObjectPo> listByVins(List<String> vins) {
		QueryWrapper<FotaObjectPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("object_id", vins);
		return list(queryWrapper);
	}

	@Override
	public List<FotaObjectPo> listByRegionCodeAndTreeNodeId(List<String> regionCodes, Long treeNodeId) {
		QueryWrapper<FotaObjectPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("current_code", regionCodes);
		queryWrapper.eq("tree_node_id", treeNodeId);
		return list(queryWrapper);
	}

	/**
	 * 
	 * @param treeNodeId
	 * @param targetObjectIds
	 * @param page
	 * @return
	 */
	@Override
	public Page<FotaObjectPo> selectVehicleWithTreeNodeIdPage(Long treeNodeId, List<Long> targetObjectIds, Page page) {
		QueryWrapper<FotaObjectPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("tree_node_id", treeNodeId);
		if (CollectionUtils.isNotEmpty(targetObjectIds)) {
			queryWrapper.in("id", targetObjectIds);
		}
		return getBaseMapper().selectPage(page, queryWrapper);
	}
	
	@Override
	public List<FotaObjectPo> selectVehicleWithTreeNodeId(Long treeNodeId, List<Long> targetObjectIds) {
		QueryWrapper<FotaObjectPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("tree_node_id", treeNodeId);
		if (CollectionUtils.isNotEmpty(targetObjectIds)) {
			queryWrapper.in("id", targetObjectIds);
		}
		return getBaseMapper().selectList(queryWrapper);
	}

}