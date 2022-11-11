/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanFirmwareListMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanFirmwareListPo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 *  该表用于定义一个OTA升级计划中需要升级哪几个软件
	包含：
     1. 依赖的软件清单-默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Service
@Slf4j
public class FotaPlanFirmwareListDbServiceImpl extends ServiceImpl<FotaPlanFirmwareListMapper, FotaPlanFirmwareListPo> implements IFotaPlanFirmwareListDbService {
	@Override
	public List<FotaPlanFirmwareListPo> listByOtaPlanId(Long otaPlanId) {
		QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("plan_id", otaPlanId);
		return list(queryWrapper);
	}

	@Override
	public List<FotaPlanFirmwareListPo> getByPlanIdWithFirmwareIds(Long otaPlanId, List<Long> firmwareIds) {
		QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("plan_id", otaPlanId);
		queryWrapper.in("firmware_id", firmwareIds);
		return list(queryWrapper);
	}

	@Override
	public List<FotaPlanFirmwareListPo> list(Long firmwareId, Long firmwareVersionId) {
		QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<FotaPlanFirmwareListPo>();
		queryWrapper.eq("firmware_id", firmwareId);
		queryWrapper.eq("firmware_version_id", firmwareVersionId);
		List<FotaPlanFirmwareListPo> list = list(queryWrapper);
		log.info("list.size={}", MyCollectionUtil.size(list));
		return MyCollectionUtil.safeList(list);
	}
}
