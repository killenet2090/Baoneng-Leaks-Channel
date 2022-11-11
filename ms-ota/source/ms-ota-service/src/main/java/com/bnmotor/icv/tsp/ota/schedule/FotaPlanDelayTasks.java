package com.bnmotor.icv.tsp.ota.schedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.ota.common.enums.ApproveStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.EnableStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanDbService;
import com.bnmotor.icv.tsp.ota.service.v2.IFotaPlanV2Service;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaPlanDelayTasks.java 
 * @Description: 监控发布时间且未发布的任务，需要修改状态为延迟发布
 * 计划任务的时间间隔目前暂时为5MIN执行一次
 * @author E.YanLonG
 * @since 2021-2-7 14:45:06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value = "升级任务延迟状态", tags = { "升级任务延迟状态同步" })
@Component
@Slf4j
public class FotaPlanDelayTasks {

	@Autowired
	IFotaPlanV2Service fotaPlanV2Service;
	
	@Autowired
	IFotaPlanDbService fotaPlanDbService;
	
	@XxlJob("otaFotaPlanDelayJobHandler")
    public ReturnT<String> handle4BuildFotaPlanDelay(String s) throws Exception {
		process();
        return ReturnT.SUCCESS;
    }
	
	public void process() {
		log.info("更新任务延迟发布状态任务开始执行");
		Date datetime = new Date();
		Integer current = 1;
		Integer pagesize = 100;
		IPage<FotaPlanPo> ipage = fotaPlanV2Service.queryDelayPlanPage(datetime, current, pagesize);
		while(hashNext(ipage)) {
			log.info("更新任务延迟发布状态任务分页执行|第{}页", current);
			current++;
			processs(ipage);
			ipage = fotaPlanV2Service.queryDelayPlanPage(datetime, current, pagesize);
		}
		log.info("更新任务延迟发布状态任务结束执行");
	}
	
	public boolean hashNext(IPage<FotaPlanPo> ipage) {
		if (CollectionUtils.isNotEmpty(ipage.getRecords())) {
			return true;
		}
		
		return false;
	}
	
	public void processs(IPage<FotaPlanPo> ipage) {
		List<FotaPlanPo> fotaPlanPos = ipage.getRecords();
		fotaPlanPos.forEach(fotaPlanPo -> {
			EnableStateEnum enableState = EnableStateEnum.select(fotaPlanPo.getIsEnable());
			ApproveStateEnum approveState = ApproveStateEnum.select(fotaPlanPo.getApproveState());
			log.info("任务id|{} 名称|{} 审批状态|{}", fotaPlanPo.getId(), fotaPlanPo.getPlanName(), approveState.getKey());
			if (EnableStateEnum.OPEN.equals(enableState)) {
				if (ApproveStateEnum.approved(fotaPlanPo.getApproveState())) {
					fotaPlanPo.setPublishState(PublishStateEnum.IN_PUBLISHING.getState());
					fotaPlanDbService.updateById(fotaPlanPo);
				}
			}
			if (EnableStateEnum.CLOSE.equals(enableState)) {
				fotaPlanPo.setPublishState(PublishStateEnum.DELAY_PUBLISH.getState());
				fotaPlanDbService.updateById(fotaPlanPo);
			}
		});
		
	}
	

}