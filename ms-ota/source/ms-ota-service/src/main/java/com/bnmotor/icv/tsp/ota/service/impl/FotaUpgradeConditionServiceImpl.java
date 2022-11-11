package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.Po2VoMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaUpgradeConditionPo;
import com.bnmotor.icv.tsp.ota.model.resp.FotaUpgradeConditionVo;
import com.bnmotor.icv.tsp.ota.service.IFotaUpgradeConditionDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaUpgradeConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: FotaUpgradeConditionPo
 * @Description: 终端升级条件项目定义 服务实现类
 * @author xuxiaochang1
 * @since 2020-09-09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaUpgradeConditionServiceImpl implements IFotaUpgradeConditionService {
	@Autowired
	private IFotaUpgradeConditionDbService fotaUpgradeConditionDbService;

	@Override
	public List<FotaUpgradeConditionVo> queryAllCondtiion() {
		List<FotaUpgradeConditionPo> fotaUpgradeConditionPoList = fotaUpgradeConditionDbService.list();
		// 排序按cond_code排序
		fotaUpgradeConditionPoList = fotaUpgradeConditionPoList.stream()
				.sorted(Comparator.comparing(FotaUpgradeConditionPo::getCondCode)).collect(Collectors.toList());
		return fotaUpgradeConditionPoList.stream().map(Po2VoMapper.INSTANCE::fotaUpgradeConditionPo2FotaUpgradeConditionVo).collect(Collectors.toList());
	}
}
