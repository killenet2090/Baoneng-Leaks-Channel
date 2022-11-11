package com.bnmotor.icv.tsp.ota.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaApprovalRecordPo;

/**
 * @ClassName: IFotaApprovalRecordDbService.java 
 * @Description: 审批流程数据库
 * @author E.YanLonG
 * @since 2021-3-24 17:09:07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IFotaApprovalRecordDbService extends IService<FotaApprovalRecordPo> {

	FotaApprovalRecordPo selectLatestApprovalRecord(Long primaryKey, Integer approvalType);
	
	List<FotaApprovalRecordPo> selectList(Long primaryKey, Integer approvalType);
	
	List<FotaApprovalRecordPo> selectByProcessInstanceId(String processInstanceId);

}