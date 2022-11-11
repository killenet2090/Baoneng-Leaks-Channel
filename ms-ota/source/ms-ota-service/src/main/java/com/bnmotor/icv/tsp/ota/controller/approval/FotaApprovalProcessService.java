package com.bnmotor.icv.tsp.ota.controller.approval;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.enums.ApprovalTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PlanModeStateEnum;
import com.bnmotor.icv.tsp.ota.config.ApprovalConfig;
import com.bnmotor.icv.tsp.ota.model.entity.FotaApprovalRecordPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPo;
import com.bnmotor.icv.tsp.ota.model.req.approval.DeleteOperateRequest;
import com.bnmotor.icv.tsp.ota.model.req.approval.TaskApprovalInitiateRequest;
import com.bnmotor.icv.tsp.ota.model.resp.feign.ProcessDefinitionQueryVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.ProcessDefinitionVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.StartProcessInstanceVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.SubmitProcessVo;
import com.bnmotor.icv.tsp.ota.service.IFotaApprovalRecordDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyDbService;
import com.bnmotor.icv.tsp.ota.service.feign.ZebraApprovalPlatformFeignService;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaApprovalProcessService.java
 * @Description: OTA升级任务审批处理
 * @author E.YanLonG
 * @since 2021-3-23 16:16:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class FotaApprovalProcessService {

	@Autowired
	IFotaPlanDbService fotaPlanDbService;
	
	@Autowired
	IFotaStrategyDbService fotaStrategyDbService;

	@Autowired
	FotaApprovalProcessService fotaApprovalProcessService;
	
	@Autowired
	ZebraApprovalPlatformFeignService zebraApprovalPlatformFeignService;
	
	@Autowired
	IFotaApprovalRecordDbService fotaApprovalRecordDbService;
	
	@Autowired
	ApprovalConfig approvalConfig;

	/**
	 * 判断是否允许删除操作
	 * @return
	 */
	public boolean checkDeleteOperate(DeleteOperateRequest deleteOperateRequest) {
		
		Long primaryKey = deleteOperateRequest.getPrimaryKey();
		Integer approvalType = deleteOperateRequest.getApprovalType();
		FotaApprovalRecordPo fotaApprovalRecordPo = fotaApprovalRecordDbService.selectLatestApprovalRecord(primaryKey, approvalType);
		Optional.ofNullable(fotaApprovalRecordPo).ifPresent(it -> {
			throw new AdamException(RespCode.SERVER_DATA_ERROR, "存在审批记录，不允许删除操作");
		});
		
		return false;
	}
	/**
	 * 发起人开启流程
	 * @param processDefinition
	 * @param initiateFlow
	 * @return 开启流程定义是否成功 true操作成功 false操作失败
	 */
	public boolean startProcessByDefinition(ProcessDefinitionVo processDefinition, InitiateFlow initiateFlow) {
		StartProcessInstanceVo startProcessInstance = new StartProcessInstanceVo();
		startProcessInstance.setBusinessKey(initiateFlow.getBusinessKey());
		startProcessInstance.setProcessDefinitionId(processDefinition.getId());
		startProcessInstance.setProcessDefinitionKey(processDefinition.getKey());
		
		startProcessInstance.setVariables(Maps.newHashMap());
		startProcessInstance.setAccount(initiateFlow.getAccount());
		startProcessInstance.setFormObject(initiateFlow.getFormObject());
		
		// 流程实例命名
		startProcessInstance.setName(initiateFlow.getFormTitle());
		startProcessInstance.setComment(initiateFlow.getComment());
		
		String processInstanceId = "";
		log.info("发起人开启流程请求参数|{}", startProcessInstance);
		RestResponse<String> response = zebraApprovalPlatformFeignService.startProcessByDefinition(startProcessInstance);
		log.info("发起人开启流程响应参数|{}", response);
		if (response.isSuccess()) {
			processInstanceId = response.getRespData();
			initiateFlow.setProcessInstanceId(processInstanceId);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 发起人提交表单（被驳回时重新发起提交）
	 * @param definitionKey
	 * @return
	 */
	public boolean submitProcessForm(InitiateFlow initiateFlow) {
		String processInstanceId = initiateFlow.getProcessInstanceId();
		
		SubmitProcessVo submitProcessVo = new SubmitProcessVo();
		submitProcessVo.setProcessInstanceId(processInstanceId);
		submitProcessVo.setFormObject(initiateFlow.getFormObject());
		submitProcessVo.setAccount(initiateFlow.getAccount());
		submitProcessVo.setComment(initiateFlow.getComment());
		log.info("发起人提交表单请求参数|{}", submitProcessVo);
		RestResponse<Boolean> response = zebraApprovalPlatformFeignService.submitProcessForm(submitProcessVo);
		log.info("发起人提交表单响应参数|{}", response);
		if (response.isSuccess()) {
			return response.getRespData();
		}
		return false;
	}
	
	public String forObject2Json(InitiateFlow initiateFlow) throws AdamException {
		Map<String, Object> parameters = Maps.newHashMap();
		Integer approvalType = initiateFlow.getApprovalType();
		if(ApprovalTypeEnum.isFotaStrategy(approvalType)) {
			
			FotaStrategyPo fotaStrategyPo = initiateFlow.getFotaStrategyPo();
			fotaStrategyPo.setCreateTime(null);
			fotaStrategyPo.setUpdateTime(null);
			parameters.put("primaryKey", String.valueOf(fotaStrategyPo.getId()));
			parameters.put("primaryName", fotaStrategyPo.getName());
		}
		
		if (ApprovalTypeEnum.isFotaPlan(approvalType)) {
			FotaPlanPo fotaPlanPo = initiateFlow.getFotaPlanPo();
			fotaPlanPo.setCreateTime(null);
			fotaPlanPo.setUpdateTime(null);
			parameters.put("primaryKey", String.valueOf(fotaPlanPo.getId()));
			parameters.put("primaryName", fotaPlanPo.getPlanName());
			
		}
		// parameters.putAll(BeanUtils.beanToMap(object));
		parameters.putAll(initiateFlow.getVars());
		
		String json = toJson(parameters);
		initiateFlow.setFormObject(json);
		return json;
	}
	
	/**
	 * 查询审批流程定义
	 * @param definitionKey 流程key
	 * @return
	 */
	public List<ProcessDefinitionVo> getFlowDefinition(String definitionKey) {
		ProcessDefinitionQueryVo processDefinitionQueryVo = new ProcessDefinitionQueryVo();
		processDefinitionQueryVo.setKey(definitionKey);
		processDefinitionQueryVo.setCurrent(1);
		processDefinitionQueryVo.setPageSize(20);
		RestResponse<Page<ProcessDefinitionVo>> response = zebraApprovalPlatformFeignService.queryProcessDefinition(processDefinitionQueryVo);
		log.info("查询流程定义响应参数|{}", response);
		
		Page<ProcessDefinitionVo> page = null;
		List<ProcessDefinitionVo> list = Lists.newArrayList();
		if (response.isSuccess()) {
			page = response.getRespData();
			list = page.getList();
		}

		return list;
	}
	
	public ProcessDefinitionVo definition(String definitionKey) {
		List<ProcessDefinitionVo> list = getFlowDefinition(definitionKey);
		if (CollectionUtils.isEmpty(list)) {
			log.error("不存在对应的流程定义|{}", definitionKey);
			throw new AdamException(RespCode.SERVER_DATA_NOT_FOUND, "不存在流程定义");
		}
		
		// 只使用激活状态的流程定义 流程定义状态: 1激活 2挂起
		List<ProcessDefinitionVo> targets = list.stream().filter(it ->  it.getSuspensionState() == 1).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(targets)) {
			log.error("无激活的流程定义|{}", definitionKey);
			throw new AdamException(RespCode.SERVER_DATA_NOT_FOUND, "无激活的流程定义");
		}
		
		// 相同key取最新的流程定义，根据最大版本号获取流程定义
		Supplier<AdamException> exceptionSupplier = () -> new AdamException(RespCode.SERVER_DATA_NOT_FOUND, "无可用流程定义");
		return targets.stream().max(Comparator.comparing(ProcessDefinitionVo::getVersion)).orElseThrow(exceptionSupplier);
	}
	
	public FotaApprovalRecordPo selectLatestApproval(TaskApprovalInitiateRequest taskApprovalInitiateRequest) {
		Long primaryKey = taskApprovalInitiateRequest.getPrimaryKey();
		Integer approvalType = taskApprovalInitiateRequest.getApprovalType();
		FotaApprovalRecordPo fotaApprovalRecordPo = fotaApprovalRecordDbService.selectLatestApprovalRecord(primaryKey, approvalType);
		return fotaApprovalRecordPo;
	}
	
	public FotaApprovalRecordPo selectApprovalRecordPo(String processInstanceId) {
		List<FotaApprovalRecordPo> fotaApprovalRecordPos = fotaApprovalRecordDbService.selectByProcessInstanceId(processInstanceId);
		return fotaApprovalRecordPos.stream().findFirst().orElse(null);
	}
	
	public boolean updateArrpovalRecordPo(FotaApprovalRecordPo fotaApprovalRecordPo) {
		fotaApprovalRecordPo.setUpdateTime(LocalDateTime.now());
		return fotaApprovalRecordDbService.updateById(fotaApprovalRecordPo);
	}
	
	public void callbackTaskStatus(String processInstanceId, int state, Consumer<ApprovalPair> invocation) {
		FotaApprovalRecordPo fotaApprovalRecordPo = selectApprovalRecordPo(processInstanceId);
		if (Objects.isNull(fotaApprovalRecordPo)) {
			log.info("对应的审批流程实例不存在|{}", processInstanceId);
			return;
		}
		
		ApprovalPair approvalPair = ApprovalPair.of();
		approvalPair.setTest(false);
		approvalPair.setType(fotaApprovalRecordPo.getApprovalType());
		approvalPair.setPrimaryKey(fotaApprovalRecordPo.getOtaObjectKey());
		fotaApprovalRecordPo.setStatus(state);
//		fotaApprovalRecordPo.setUpdateBy("sys");
		CommonUtil.wrapBasePo4Update(fotaApprovalRecordPo, null, null);
		if (updateArrpovalRecordPo(fotaApprovalRecordPo)) {
			invocation.accept(approvalPair);
		}
	}
	
	/**
	 * 发起审批请求入口
	 * @param primaryKey 任务主键 或者 策略主键
	 * @param approvalType 审批类型 1策略审批 2任务审批
	 * @param link 页面链接
	 */
	public void initial(TaskApprovalInitiateRequest taskApprovalInitiateRequest, Consumer<ApprovalPair> invocation) {
		Long primaryKey = taskApprovalInitiateRequest.getPrimaryKey();
		Integer approvalType = taskApprovalInitiateRequest.getApprovalType();
		Map<String, String> vars = taskApprovalInitiateRequest.getVars();
		String account = taskApprovalInitiateRequest.getCreateBy();
		log.info("当前操作用户|{}", account);
		ApprovalPair pair = ApprovalPair.of().setTest(false).setType(approvalType);
		
		InitiateFlow initiateFlow = new InitiateFlow();
		if (ApprovalTypeEnum.isFotaStrategy(approvalType)) {
			FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(primaryKey);
			initiateFlow.setFotaStrategyPo(fotaStrategyPo);
			initiateFlow.setFormTitle(fotaStrategyPo.getName());
//			initiateFlow.setDefinitionKey("update_strategy_flow");
			String processDefine = approvalConfig.getProcessDefineStrategy();
			initiateFlow.setDefinitionKey(processDefine);
			initiateFlow.setComment(fotaStrategyPo.getRemark());
		}
		
		// 升级任务如果是测试任务则免审批
		if (ApprovalTypeEnum.isFotaPlan(approvalType)) {
			FotaPlanPo fotaPlanPo = fotaPlanDbService.getById(primaryKey);
			
			if (PlanModeStateEnum.isTestsPlan(fotaPlanPo.getPlanMode())) {
				log.info("升级任务是测试任务，免审批|{}", fotaPlanPo.getId());
				pair.setTest(true);
				invocation.accept(pair);
				return;
			}
			initiateFlow.setFotaPlanPo(fotaPlanPo);
			initiateFlow.setFormTitle(fotaPlanPo.getPlanName());
			// initiateFlow.setDefinitionKey("update_plan_flow");
			String processDefine = approvalConfig.getProcessDefinePlan();
			initiateFlow.setDefinitionKey(processDefine);
			initiateFlow.setComment("");
		}
		
//		String businessKey = BusinessKeyGenerator.businessKey();
//		initiateFlow.setBusinessKey(businessKey);
		initiateFlow.setApprovalType(approvalType);
		initiateFlow.setPrimaryKey(primaryKey);
		initiateFlow.setVars(vars);
		initiateFlow.setAccount(account);
		
		String formObject = forObject2Json(initiateFlow);
		initiateFlow.setFormObject(formObject);
		
		// 最近一次审批的状态如果是驳回，则直接提交表单，否则需要重新发起流程
		FotaApprovalRecordPo fotaApprovalRecordPo = selectLatestApproval(taskApprovalInitiateRequest);
		if (Objects.isNull(fotaApprovalRecordPo)) {
			log.info("首次审批 需要发起流程");
			initial0(taskApprovalInitiateRequest, initiateFlow);
			invocation.accept(pair);
			return;
		}
		
		initiateFlow.setFotaApprovalRecordPo(fotaApprovalRecordPo);
		initiateFlow.setProcessInstanceId(fotaApprovalRecordPo.getProcessInstanceId());
		initiateFlow.setBusinessKey(fotaApprovalRecordPo.getBusinessKey());
		
		// <span style="TEXT-DECORATION: line-through">审批状态：0待审批 1审批中，2通过 3驳回 4拒绝</span>
		// 1免审批 2待审批 3审批中 4审批通过 5未通过 6驳回
		Integer status = fotaApprovalRecordPo.getStatus();
		
		if (status == 7) {
			log.info("最近的一次审批被撤回，重新提交表单即可");
			if (initial1(taskApprovalInitiateRequest, initiateFlow)) {
				invocation.accept(pair);
			}
		}
		
		// 如果是拒绝，当前任务不允许再进行审批
		if (status == 5) {
			log.info("最近的一次审批被拒绝，需要重新发起新的审批");
			return;
		}
		
		if (status == 6) {
			log.info("最近的一次审批被驳回，重新提交表单即可");
			if (initial1(taskApprovalInitiateRequest, initiateFlow)) {
				invocation.accept(pair);
			}
		}
		
		if (status == 4) {
			log.info("当前任务已完成审批，无需处理");
			return;
		}
		
		if (status == 3) {
			log.info("当前任务正处在审批过程中，无需处理");
			return;
		}
		
	}
	
	/**
	 * 发起人首次发起审批
	 * @param taskApprovalInitiateRequest
	 * @param initiateFlow
	 */
	public void initial0(TaskApprovalInitiateRequest taskApprovalInitiateRequest, InitiateFlow initiateFlow) {
		
		String businessKey = BusinessKeyGenerator.businessKey();
		initiateFlow.setBusinessKey(businessKey);
		// 查询流程定义
		String definitionKey = initiateFlow.getDefinitionKey();
		ProcessDefinitionVo processDefinition = definition(definitionKey);
		
		// 发起人发起流程
		if (startProcessByDefinition(processDefinition, initiateFlow)) {
			log.info("发起人发起流程操作成功");
			//保存数据
			FotaApprovalRecordPo fotaApprovalRecordPo = build(initiateFlow);
			fotaApprovalRecordDbService.save(fotaApprovalRecordPo);
			log.info("新增加审批流程记录成功key|{}", fotaApprovalRecordPo.getId());
		} else {
			log.info("发起人发起流程操作失败");
		}
		
	}
	
	/**
	 * 上一次审批被驳回 此次发起审批直接提交表单
	 * @param taskApprovalInitiateRequest
	 * @param initiateFlow
	 */
	public boolean initial1(TaskApprovalInitiateRequest taskApprovalInitiateRequest, InitiateFlow initiateFlow) {
		
		// 查询流程定义
//		String definitionKey = initiateFlow.getDefinitionKey();
		
		// 发起人直接提交表单
		if (submitProcessForm(initiateFlow)) {
			log.info("发起人提交表单流程操作成功");
			// 更新审批数据
			
			FotaApprovalRecordPo fotaApprovalRecordPo = initiateFlow.getFotaApprovalRecordPo();
			fotaApprovalRecordPo.setFormTitle(initiateFlow.getFormTitle());
			fotaApprovalRecordPo.setOtaObjectBody(initiateFlow.getFormObject());
			fotaApprovalRecordPo.setVars(toJson(initiateFlow.getVars()));
			fotaApprovalRecordPo.setStatus(3); // 审批中
			fotaApprovalRecordPo.setUpdateTime(LocalDateTime.now());
			fotaApprovalRecordPo.setUpdateBy(initiateFlow.getAccount());
			
			fotaApprovalRecordDbService.updateById(fotaApprovalRecordPo);
			log.info("发起人提交表单入库成功key|{}", fotaApprovalRecordPo.getId());
			return true;
		} else {
			log.info("发起人提交表单流程操作失败");
		}
		
		return false;
	}
	
	public FotaApprovalRecordPo build(InitiateFlow initiateFlow) {
		FotaApprovalRecordPo po = new FotaApprovalRecordPo();
		
		po.setBusinessKey(initiateFlow.getBusinessKey());
		po.setApprovalType(initiateFlow.getApprovalType());
		po.setOtaObjectKey(initiateFlow.getPrimaryKey());
		po.setDefinitionKey(initiateFlow.getDefinitionKey());
		po.setCreateTime(LocalDateTime.now());
		po.setCreateBy(initiateFlow.getAccount());
		
		po.setVars(toJson(initiateFlow.getVars()));
		po.setProcessInstanceId(initiateFlow.getProcessInstanceId());
		
		po.setFormTitle(initiateFlow.getFormTitle());
		po.setOtaObjectBody(initiateFlow.getFormObject());
		po.setDescription("OTA审批流程");
		// 1免审批 2待审批 3审批中 4审批通过 5未通过 6驳回
		po.setStatus(3); 
		
		return po;
	}
	
	public String toJson(Object object) {
		try {
			return Objects.isNull(object) ? null : JsonUtil.toJson(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

@Data
class InitiateFlow {

	FotaPlanPo fotaPlanPo;
	FotaStrategyPo fotaStrategyPo;
	FotaApprovalRecordPo fotaApprovalRecordPo;
	
	/**
	 * 页面链接
	 */
	Map<String, String> vars;
	
	/**
	 * 审批类型 1策略审批 2任务审批
	 */
	int approvalType;
	
	/**
	 * 业务主键
	 */
	String businessKey;
	
	/**
	 * 表单title
	 */
	String formTitle;
	
	/**
	 * 策略主键或者任务主键
	 */
	Long primaryKey;
	
	/**
	 * 审批流程实例
	 */
	String processInstanceId;
	
	/**
	 * 审批类型对应的流程定义key
	 */
	String definitionKey;
	
	/**
	 * 表单内容json用于页面展示
	 */
	String formObject;
	
	/**
	 * 提交表单时的用户id/唯一账号
	 */
	String account;
	
	/**
	 * 备注
	 */
	String comment;
	
}