package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaObjectLabelMapper;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectLabelPo;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectLabelDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName: FotaObjectLabelPo
 * @Description: OTA升级对象指需要升级的一个完整对象， 在车联网中指一辆车 通常拿车的vin作为升级的ID 服务实现类
 * @author xuxiaochang1
 * @since 2020-11-27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaObjectLabelDbServiceImpl extends ServiceImpl<FotaObjectLabelMapper, FotaObjectLabelPo>
		implements IFotaObjectLabelDbService {

	@Override
	public List<LabelInfo> listLabels(List<Long> objectIds) {
		if (CollectionUtils.isNotEmpty(objectIds)) {
			return getBaseMapper().listLabels(objectIds);
		}
		return Lists.newArrayList();
	}
	
	@Override
	public List<LabelInfo> listFullLabels() {
		return getBaseMapper().listFullLabels();
	}

	@Override
	public List<FotaObjectLabelPo> selectByLables(List<String> labels) {
		if (CollectionUtils.isEmpty(labels)) {
			return Lists.newArrayList();
		}
		QueryWrapper<FotaObjectLabelPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("label_key", labels);
		List<FotaObjectLabelPo> fotaObjectLabelPos = list(queryWrapper);
		return fotaObjectLabelPos;
	}

	@Override
	public List<FotaObjectLabelPo> selectByObjectIds(List<Long> objectIds) {
		if (CollectionUtils.isEmpty(objectIds)) {
			return Lists.newArrayList();
		}
		QueryWrapper<FotaObjectLabelPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("object_id", objectIds);
		List<FotaObjectLabelPo> fotaObjectLabelPos = list(queryWrapper);
		return fotaObjectLabelPos;
	}

	@Override
	public List<FotaObjectLabelPo> selectLableByVin(List<String> vins) {
		if (CollectionUtils.isEmpty(vins)) {
			return Lists.newArrayList();
		}
		QueryWrapper<FotaObjectLabelPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("vin", vins);
		List<FotaObjectLabelPo> fotaObjectLabelPos = list(queryWrapper);
		return fotaObjectLabelPos;
	}

	@Override
	public List<FotaObjectLabelPo> list(String vin, String labelKey) {
		if (StringUtils.isEmpty(vin)) {
			return Lists.newArrayList();
		}
		QueryWrapper<FotaObjectLabelPo> queryWrapper = new QueryWrapper();
		queryWrapper.eq("vin", vin);
		queryWrapper.eq("label_key", labelKey);
		List<FotaObjectLabelPo> fotaObjectLabelPos = list(queryWrapper);
		log.info("list.size={}", MyCollectionUtil.size(fotaObjectLabelPos));
		return MyCollectionUtil.safeList(fotaObjectLabelPos);
	}

	@Override
	public void delByVinAndKeysPyhsical(String vin, List<String> labelKeys) {
		if(StringUtils.isEmpty(vin) || MyCollectionUtil.isEmpty(labelKeys)){
			log.warn("参数异常.vin={}, labelKeys={}", vin, labelKeys);
		}
		getBaseMapper().delByVinAndKeysPyhsical(vin, labelKeys);
	}

}