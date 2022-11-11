package com.bnmotor.icv.tsp.ota.controller.approval;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.common.enums.ApprovalTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.ApproveStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.CommentTypeEnum;
import com.bnmotor.icv.tsp.ota.model.req.approval.TaskApprovalInitiateRequest;
import com.bnmotor.icv.tsp.ota.model.req.approval.TaskCreateRequest;
import com.bnmotor.icv.tsp.ota.model.req.approval.TaskTerminateRequest;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanApproveV2Req;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyAuditDto;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyService;
import com.bnmotor.icv.tsp.ota.service.v2.IFotaPlanV2Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaApprovalProcessController.java
 * @Description: 审批流回调接口
 * 
 * <pre>审批平台侧的审批状态定义：审批类型 0 待提交 1 审批中 2 审批完成 3 审批未通过 4 驳回待提交</pre>
 * <pre>OTA平台侧的审批状态定义：1 免审批，2待审批、3审批中、4已审批、5拒绝 6驳回</pre>
 * @author E.YanLonG
 * @since 2021-3-22 9:39:36
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value = "审批流程管理", tags = { "审批流程管理" })
@RestController
@RequestMapping("/approval")
@Slf4j
public class FotaApprovalProcessController {

	@Autowired
	FotaApprovalProcessService fotaApprovalProcessService;

	@Autowired
	IFotaPlanV2Service fotaPlanService;

	@Autowired
	IFotaStrategyService fotaStrategyService;

	@ApiOperation(value = "任务开始消息通知", notes = "供审批平台回调使用", response = Boolean.class)
	@WrapBasePo
	@PostMapping("/callback/create")
	public ResponseEntity taskCreate(@RequestBody TaskCreateRequest taskCreateRequest) {
		log.info("任务开始消息通知:请求参数|{}", taskCreateRequest);
		
		String processInstanceId = taskCreateRequest.getProcessInstanceId();
		String initiator = taskCreateRequest.getInitiator();
		String result = taskCreateRequest.getResult();
		String taskId = taskCreateRequest.getTaskId();
		 //* <pre>审批平台侧的审批状态定义：审批类型 0 待提交 1 审批中 2 审批完成 3 审批未通过 4 驳回待提交</pre>
		 //* <pre>OTA平台侧的审批状态定义：1 免审批，2待审批、3审批中、4已审批、5拒绝 6驳回</pre>
		int otaKey = CommentTypeEnum.otaKey(result);
		if (otaKey == ApproveStateEnum.REVIEW_APPROVAL.getState()) { // 审批当前任务状态是‘驳回’，需要同步状态到任务状态中去
			fotaApprovalProcessService.callbackTaskStatus(processInstanceId, ApproveStateEnum.REVIEW_APPROVAL.getState(), (data) -> {
				int approvalType = data.getType();
				Long primaryKey = data.getPrimaryKey();
				if (ApprovalTypeEnum.isFotaPlan(approvalType)) { // 升级任务状态更新为"驳回"
					FotaPlanApproveV2Req fotaPlanApproveV2Req = new FotaPlanApproveV2Req();
					fotaPlanApproveV2Req.setId(primaryKey);
					Integer status = ApproveStateEnum.REVIEW_APPROVAL.getState(); 
					fotaPlanApproveV2Req.setApproveStatus(status);
					fotaPlanService.approveFotaPlan(fotaPlanApproveV2Req);
				} else {
					// 策略状态修改成'驳回'
					FotaStrategyAuditDto fotaStrategyAuditDto = new FotaStrategyAuditDto();
					fotaStrategyAuditDto.setId(String.valueOf(primaryKey));
					fotaStrategyAuditDto.setStatus(5); // 策略驳回 可以重新提交，待审批
					fotaStrategyAuditDto.setUpdateBy(taskCreateRequest.getAccount());
					fotaStrategyService.strategyAudit(fotaStrategyAuditDto);
				}
			});
		}
		if (otaKey == ApproveStateEnum.REVOKE_APPROVAl.getState()) { // 审批撤回 修改后重新提交 OTA侧审批状态对应为'待审批'
			fotaApprovalProcessService.callbackTaskStatus(processInstanceId, ApproveStateEnum.REVOKE_APPROVAl.getState(), (data) -> {
				int approvalType = data.getType();
				Long primaryKey = data.getPrimaryKey();
				if (ApprovalTypeEnum.isFotaPlan(approvalType)) { // 升级任务状态更新为'待审批'
					FotaPlanApproveV2Req fotaPlanApproveV2Req = new FotaPlanApproveV2Req();
					fotaPlanApproveV2Req.setId(primaryKey);
					Integer status = ApproveStateEnum.READY_FOR_APPROVE.getState(); 
					fotaPlanApproveV2Req.setApproveStatus(status);
					fotaPlanService.approveFotaPlan(fotaPlanApproveV2Req);
				} else {
					// 策略状态修改成'驳回'
					FotaStrategyAuditDto fotaStrategyAuditDto = new FotaStrategyAuditDto();
					fotaStrategyAuditDto.setId(String.valueOf(primaryKey));
					fotaStrategyAuditDto.setStatus(0); // 策略撤回相当于'待审批'
					fotaStrategyAuditDto.setUpdateBy(taskCreateRequest.getAccount());
					fotaStrategyService.strategyAudit(fotaStrategyAuditDto);
				}
			});
		}
		
		// 其他状态不作修改
		return RestResponse.ok(true);
	}

	@ApiOperation(value = "流程结束消息通知", notes = "供审批平台回调使用", response = Boolean.class)
	@WrapBasePo
	@PostMapping("/callback/terminate")
	public ResponseEntity taskTerminate(@RequestBody TaskTerminateRequest taskTerminateRequest) {
		log.info("流程结束消息通知:请求参数|{}", taskTerminateRequest);
		String processInstanceId = taskTerminateRequest.getProcessInstanceId();
		String result = taskTerminateRequest.getResult();
		int otaKey = CommentTypeEnum.otaKey(result);
		List<Integer> finishStates = Lists.newArrayList(ApproveStateEnum.REJECTED_APPROVE.getState(), ApproveStateEnum.BE_APPROVED.getState());
		if (finishStates.contains(otaKey)) { // 审批终结状态，通过/拒绝
			ApproveStateEnum approveState = ApproveStateEnum.select(otaKey);
			fotaApprovalProcessService.callbackTaskStatus(processInstanceId, approveState.getState(), (data) -> {
				int approvalType = data.getType();
				Long primaryKey = data.getPrimaryKey();
				if (ApprovalTypeEnum.isFotaPlan(approvalType)) { // 升级任务状态更新为"驳回"
					FotaPlanApproveV2Req fotaPlanApproveV2Req = new FotaPlanApproveV2Req();
					fotaPlanApproveV2Req.setId(primaryKey);
					Integer status = approveState.getState();
					fotaPlanApproveV2Req.setApproveStatus(status);
					fotaPlanService.approveFotaPlan(fotaPlanApproveV2Req);
				} else {
					FotaStrategyAuditDto fotaStrategyAuditDto = new FotaStrategyAuditDto();
					fotaStrategyAuditDto.setId(String.valueOf(primaryKey));
					Integer status = ApproveStateEnum.approved(otaKey) ? 2 : 3;
					fotaStrategyAuditDto.setStatus(status); // 策略驳回相当于审批拒绝
					fotaStrategyAuditDto.setUpdateBy(taskTerminateRequest.getAccount());
					fotaStrategyService.strategyAudit(fotaStrategyAuditDto);
				}
			});
		}
		
		return RestResponse.ok(true);
	}

//	@ApiOperation(value="生成业务businessKey", notes="生成业务businessKey", response = String.class)
//	@WrapBasePo
//	@PutMapping("/businessKey")
//	public ResponseEntity makeBusinessKey() {
//		return RestResponse.ok(BusinessKeyGenerator.businessKey());
//	}

	@ApiOperation(value = "OTA升级任务发起审批", notes = "OTA系统从页面发起审批")
	@WrapBasePo
	@PutMapping("/initiate")
	public ResponseEntity initiate(@RequestBody TaskApprovalInitiateRequest taskApprovalInitiateRequest) {
		log.info("发起审批 请求参数|{}", taskApprovalInitiateRequest);

		// 先判断是否需要审批，测试任务不需要审批，所以免审批
		Integer approvalType = taskApprovalInitiateRequest.getApprovalType();
		fotaApprovalProcessService.initial(taskApprovalInitiateRequest, (result) -> {
			Long primaryKey = taskApprovalInitiateRequest.getPrimaryKey();
			if (ApprovalTypeEnum.isFotaPlan(approvalType)) {
				FotaPlanApproveV2Req fotaPlanApproveV2Req = new FotaPlanApproveV2Req();
				fotaPlanApproveV2Req.setId(primaryKey);
				Integer status = result.isTest() ? ApproveStateEnum.NO_NEED_APPROVE.getState() : ApproveStateEnum.IN_PROCESS_APPROVE.getState(); 
				fotaPlanApproveV2Req.setApproveStatus(status);
				fotaPlanService.approveFotaPlan(fotaPlanApproveV2Req);
			} else {
				FotaStrategyAuditDto fotaStrategyAuditDto = new FotaStrategyAuditDto();
				fotaStrategyAuditDto.setId(String.valueOf(primaryKey));
				fotaStrategyAuditDto.setStatus(1);
				fotaStrategyAuditDto.setUpdateBy(taskApprovalInitiateRequest.getUpdateBy());
				fotaStrategyService.strategyAudit(fotaStrategyAuditDto);
			}
		});
		return RestResponse.ok(true);
	}

}