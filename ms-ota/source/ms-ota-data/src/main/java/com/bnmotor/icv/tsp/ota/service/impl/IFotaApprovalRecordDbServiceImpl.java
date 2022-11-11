package com.bnmotor.icv.tsp.ota.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaApprovalRecordMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaApprovalRecordPo;
import com.bnmotor.icv.tsp.ota.service.IFotaApprovalRecordDbService;

/**
 * @ClassName: IFotaApprovalRecordDbServiceImpl.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2021-3-24 17:11:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class IFotaApprovalRecordDbServiceImpl extends ServiceImpl<FotaApprovalRecordMapper, FotaApprovalRecordPo> implements IFotaApprovalRecordDbService {

	@Override
	public FotaApprovalRecordPo selectLatestApprovalRecord(Long primaryKey, Integer approvalType) {
		List<FotaApprovalRecordPo> records = selectList(primaryKey, approvalType);
		return records.stream().findFirst().orElse(null);
	}
	
	@Override
	public List<FotaApprovalRecordPo> selectList(Long primaryKey, Integer approvalType) {
		QueryWrapper<FotaApprovalRecordPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("approval_type", approvalType);
        queryWrapper.eq("ota_object_key", primaryKey);
        queryWrapper.orderByDesc("id");
        return this.list(queryWrapper);
	}

	@Override
	public List<FotaApprovalRecordPo> selectByProcessInstanceId(String processInstanceId) {
		QueryWrapper<FotaApprovalRecordPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_instance_id", processInstanceId);
        return this.list(queryWrapper);
	}

}
