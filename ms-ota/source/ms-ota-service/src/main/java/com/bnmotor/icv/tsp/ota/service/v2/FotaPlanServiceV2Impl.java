package com.bnmotor.icv.tsp.ota.service.v2;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.tsp.ota.common.enums.*;
import com.bnmotor.icv.tsp.ota.common.event.MyOtaEventKit;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.Dto2PoMapper;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.Po2VoMapper;
import com.bnmotor.icv.tsp.ota.mapper.*;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPo;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanDelayQuery;
import com.bnmotor.icv.tsp.ota.model.req.v2.*;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanDetailV2Vo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanIsEnableV2RespVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanV2Vo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: FotaPlanServiceV2Impl.java FotaPlanServiceV2Impl
 * @Description: V2版本升级任务
 * @author E.YanLonG
 * @since 2020-12-1 17:12:53
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service("fotaPlanServiceV2")
public class FotaPlanServiceV2Impl implements IFotaPlanV2Service {

    @Autowired
    IFotaPlanDbService fotaPlanDbService;

	@Autowired
	IFotaStrategyDbService fotaStrategyDbService;
	
	@Autowired
    private IUpgradeTaskConditionDbService upgradeTaskConditionDbService;

    @Autowired
    private IUpgradeStrategyDbService upgradeStrategyDbService;

    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Autowired
    private IFotaPlanTaskDetailDbService planTaskDetailDbService;

    @Autowired
    private IFotaPlanFirmwareListDbService fotaPlanFirmwareListDbService;

    @Autowired
    private ITaskTerminateDbService taskTerminateDbService;

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;
    
    @Autowired
    MyOtaEventKit myOtaEventKit;
    
    @Autowired
    IFotaPlanPublishService fotaPlanPublishService;

    @Deprecated
    @Override
    public IPage<FotaPlanV2Vo> queryPage(FotaPlanV2Query query) {
    	Long treeNodeId = query.getTreeNodeId();
        IPage<FotaPlanPo> fotaPlanDos = fotaPlanDbService.queryPage(query.getCurrent(), query.getPageSize(), treeNodeId, RebuildFlagEnum.V2.getFlag());

        IPage<FotaPlanV2Vo> fotaPlanVos = new Page<>(fotaPlanDos.getCurrent(), fotaPlanDos.getSize(), fotaPlanDos.getTotal());
        fotaPlanVos.setTotal(fotaPlanDos.getTotal());
        if (MyCollectionUtil.isNotEmpty(fotaPlanDos.getRecords())) {
            List<FotaPlanV2Vo> vos = fotaPlanDos.getRecords().stream().map(fotaPlan -> {
            	// 查询并设置升级任务发布状态
            	// fotaPlanPublishService.selectPublishState(fotaPlan);
                FotaPlanV2Vo fotaPlanV2Vo = Po2VoMapper.INSTANCE.fotaPlanPo2FotaPlanV2Vo(fotaPlan);
                fotaPlanV2Vo.setEntireVersionNo(fotaPlan.getTargetVersion());
                return fotaPlanV2Vo;
            }).collect(Collectors.toList());
            fotaPlanVos.setRecords(vos);
            
            wrapFieldStatusDesc(vos);
        }
        return fotaPlanVos;
    }
    
    /**
     * 填充描述信息
     * @param vos
     */
    public void wrapFieldStatusDesc(List<FotaPlanV2Vo> vos) {
    	vos.forEach(it -> {
    		String approveStatus = ApproveStateEnum.selectKey(it.getApproveStatus());
    		it.setApproveStatusDesc(approveStatus);
    		String publishStatus = PublishStateEnum.selectKey(it.getPublishStatus());
    		it.setPublishStatusDesc(publishStatus);
    		String enableDesc = EnableStateEnum.selectKey(it.getIsEnable());
    		it.setIsEnableDesc(enableDesc);
    		String planModeDesc = PlanModeStateEnum.selectKey(it.getPlanMode());
    		it.setPlanModeDesc(planModeDesc);
    		String upgradeModeDesc = UpgradeModeStateEnum.selectKey(it.getUpgradeMode());
    		it.setUpgradeModeDesc(upgradeModeDesc);
    	});
    }

    @Deprecated
    @Override
    public FotaPlanDetailV2Vo getFotaPlanDetailVoById(Long planId) {
        FotaPlanPo fotaPlanDo = fotaPlanDbService.getById(planId);
        FotaPlanDetailV2Vo fotaPlanDetailVO = Po2VoMapper.INSTANCE.fotaPlanPo2FotaPlanDetailV2Vo(fotaPlanDo);

        // 填充附加字段
        FotaPlanExtraData fotaPlanExtraData = reverseExtraData(fotaPlanDo);
        fotaPlanDetailVO.setFotaPlanExtraData(fotaPlanExtraData);
        
        //补充设备树信息
        if(Objects.nonNull(fotaPlanDo.getObjectParentId())) {
            DeviceTreeNodePo deviceTreeNodeDo = deviceTreeNodeDbService.getById(fotaPlanDo.getObjectParentId());
            fotaPlanDetailVO.setTreeNodeId(Long.parseLong(fotaPlanDo.getObjectParentId()));
            fotaPlanDetailVO.setTreeNodeDesc(deviceTreeNodeDo.getNodeNamePath());
            String nodeIdPath = deviceTreeNodeDo.getNodeIdPath();
            List<String> vehicleOptions = Arrays.asList(StringUtils.split(nodeIdPath, "/"));
            fotaPlanDetailVO.setVehicleOptions(vehicleOptions);
        }
        
        Long treeNodeId = fotaPlanDo.getTreeNodeId();

        //分页查询结果集
//        IPage<FotaPlanObjListPo> iPageFotaPlanObjList = fotaPlanObjListDbService.queryPage(planId, 1, 20);
//        IPage<FotaPlanObjListV2Vo> iPageFotaPlanObjV2Vos = new Page<>(1, 20 ,iPageFotaPlanObjList.getTotal());
//        iPageFotaPlanObjV2Vos.setRecords(MyCollectionUtil.newCollection(iPageFotaPlanObjList.getRecords(), item ->{
//            FotaPlanObjListV2Vo fotaPlanObjListV2Vo = new FotaPlanObjListV2Vo();
//            fotaPlanObjListV2Vo.setOtaObjectId(item.getOtaObjectId());
//            fotaPlanObjListV2Vo.setVin(item.getVin());
//            fotaPlanObjListV2Vo.setCurrentArea(item.getCurrentArea());
//            return fotaPlanObjListV2Vo;
//        }));
//        fotaPlanDetailVO.setFotaPlanObjListVoIPage(iPageFotaPlanObjV2Vos);

        //TODO
//        List<FotaPlanObjListPo> fotaPlanObjListDos = fotaPlanObjListDbService.listByOtaPlanId(planId);
//        //如果已经选择了车辆信息
//        if(MyCollectionUtil.isNotEmpty(fotaPlanObjListDos)) {
//            //TODO
//            /*List<FotaObjectPo> fotaObjectDos = fotaObjectDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListDos, item -> item.getOtaObjectId()));
//            Map<Long, FotaObjectPo> fotaObjectDoMap = fotaObjectDos.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
//            //补充currentArea和saleArea属性字段
//            fotaPlanObjListDos.forEach(item -> {
//                FotaObjectPo fotaObjectDo = fotaObjectDoMap.get(item.getOtaObjectId());
//                if (Objects.nonNull(fotaObjectDo)) {
//                    item.setCurrentArea(fotaObjectDo.getCurrentArea());
//                    item.setSaleArea(fotaObjectDo.getSaleArea());
//                }
//            });*/
//        }

        //包装升级ecu列表

        //设置返回对象列表信息
        /*FotaPlanDetailV2Vo fotaPlanDetailVO = new FotaPlanDetailV2Vo();*/
        /*fotaPlanDetailVO.setFotaPlanFirmwareLists(fotaPlanFirmwareLists);*/
//        List<FotaPlanObjListV2Vo> fotaPlanObjListV2Vos = MyCollectionUtil.map2NewList(fotaPlanObjListDos, item ->{
//            FotaPlanObjListV2Vo fotaPlanObjListV2Vo = new FotaPlanObjListV2Vo();
//
//            fotaPlanObjListV2Vo.setOtaObjectId(item.getOtaObjectId());
//            fotaPlanObjListV2Vo.setVin(item.getVin());
//            fotaPlanObjListV2Vo.setCurrentArea(item.getCurrentArea());
//            return fotaPlanObjListV2Vo;
//        });
//        fotaPlanDetailVO.setFotaPlanObjListVos(fotaPlanObjListV2Vos);

        return fotaPlanDetailVO;
    }
    
    @Transactional(rollbackFor = {Exception.class, AdamException.class})
    @Override
    public FotaPlanPo insertFotaPlan(FotaPlanV2Req fotaPlanV2Req) {
        FotaPlanPo fotaPlanPo = Dto2PoMapper.INSTANCE.fotaPlanV2Req2FotaPlanPo(fotaPlanV2Req);
        fotaPlanPo.setId(IdWorker.getId());
        fotaPlanPo.setRebuildFlag(RebuildFlagEnum.V2.getFlag());
        //TODO 暂时修改为新增则审核通过
        fotaPlanPo.setPublishState(PublishStateEnum.UNPUBLISH.getState());
        fotaPlanPo.setIsEnable(EnableStateEnum.CLOSE.getState());

        Long treeNodeId = fotaPlanV2Req.getTreeNodeId();

        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getById(treeNodeId);
        MyAssertUtil.notNull(deviceTreeNodePo, "设备树节点数据异常");
        //兼容之前的做法
        fotaPlanPo.setObjectParentId(Long.toString(treeNodeId));

    	// 策略id 目前一个任务只对应一个策略
    	Long fotaStrategyId = fotaPlanV2Req.getFotaStrategyId();
        fotaPlanPo.setFotaStrategyId(fotaStrategyId);
    	FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(fotaStrategyId);
        MyAssertUtil.notNull(fotaStrategyPo, "策略数据异常");
        MyAssertUtil.state(fotaStrategyPo.getTreeNodeId().equals(treeNodeId), "策略数据异常，不在同一个车辆配置下");

    	String entireVersionNo = fotaStrategyPo.getEntireVersionNo();
    	// 填写整车设备树节点
    	fotaPlanPo.setTreeNodeId(fotaStrategyPo.getTreeNodeId());
    	// 填写整车版本
        fotaPlanPo.setTargetVersion(entireVersionNo);

        fotaPlanPo.setVersion(0);
        //fotaPlanPo.setPlanStatus(Enums.PlanStatusTypeEnum.OUTLINE.getType());
        
        // 任务模式
        PlanModeStateEnum planMode = PlanModeStateEnum.select(fotaPlanPo.getPlanMode());
        
        // 如果任务模式是'测试任务' 审批状态则设置为'免审批' 正式任务临时设置成'已审批'
        ApproveStateEnum approved = PlanModeStateEnum.TEST.equals(planMode) ? ApproveStateEnum.NO_NEED_APPROVE : ApproveStateEnum.READY_FOR_APPROVE;
        PublishStateEnum published = PlanModeStateEnum.TEST.equals(planMode) ? PublishStateEnum.UNPUBLISH: PublishStateEnum.UNPUBLISH;
        
        // 由'免审批'修改为'已审批'
        fotaPlanPo.setApproveState(approved.getState());
        fotaPlanPo.setPublishState(published.getState());
        fotaPlanPublishService.selectPublishState(fotaPlanPo);
        
        CommonUtil.wrapBasePo(fotaPlanPo, true);
        
        // 填充附加参数
        wrapExtraData(fotaPlanPo, fotaPlanV2Req);
        fotaPlanDbService.save(fotaPlanPo);

        return fotaPlanPo;
    }

    /**
     * 记录升级计划额外参数，用于返回页面修改时提供显示
     * @param fotaPlanPo
     * @param fotaPlanV2Req
     * @throws Exception
     */
    @Override
    public void wrapExtraData(FotaPlanPo fotaPlanPo, FotaPlanV2Req fotaPlanV2Req) {
//		Long otaPlanId = fotaPlanPo.getId();
//		Long treeNodeId = fotaPlanV2Req.getTreeNodeId();
//		Long strategyId = fotaPlanV2Req.getFotaStrategyId();
		Integer operateType = fotaPlanV2Req.getOperateType();
		List<String> regions = fotaPlanV2Req.getRegions();
		List<String> labels = fotaPlanV2Req.getLabels();
		List<LabelInfo> labels0 = fotaPlanV2Req.getLabels0();
		
		FotaPlanExtraData fotaPlanExtraData = FotaPlanExtraData.of();
		fotaPlanExtraData.setOperateType(operateType);
		fotaPlanExtraData.setLabels(labels);
		fotaPlanExtraData.setLabels0(labels0);
		fotaPlanExtraData.setRegions(regions);
		fotaPlanExtraData.setFileName(fotaPlanV2Req.getFileName());
		
//		fotaPlanExtraData.setStrategyId(strategyId);
//		fotaPlanExtraData.setTreeNodeId(treeNodeId);
//		fotaPlanExtraData.setOtaPlanId(otaPlanId);
		
		try {
			String extra = OtaJsonUtil.toJson(fotaPlanExtraData);
			fotaPlanPo.setExtra(extra);
		} catch (Exception e) {
			log.error("输出到json异常|{}", e.getMessage(), e);
			// TODO: 正常情况下输出到JSON不应有异常，如果有异常需要抛出异常，临时屏蔽
		}
	}
    
    /**
     * 从表字段中反转到对象中 升级计划详情使用
     * @param fotaPlanPo
     * @return
     */
    public FotaPlanExtraData reverseExtraData(FotaPlanPo fotaPlanPo) {
    	String extra = fotaPlanPo.getExtra();
		FotaPlanExtraData fotaPlanExtraData = null;
		try {
			fotaPlanExtraData = StringUtils.isBlank(extra) ? null : OtaJsonUtil.toObject(extra, FotaPlanExtraData.class);
		} catch (Exception e) {
			log.error("反转FotaPlanExtraData异常|{}", e.getMessage(), e);
			// TODO: 正常情况下输出到JSON不应有异常，如果有异常需要抛出异常，临时屏蔽
		}
		return fotaPlanExtraData;
	}
    
    /**
     * 更新升级计划
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateFotaPlan(FotaPlanV2Req fotaPlanV2Req) {
        if (Objects.isNull(fotaPlanV2Req.getId()) || fotaPlanV2Req.getId() <= 0) {
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
        }

//        FotaPlanPo fotaPlanPo = new FotaPlanPo();
//        BeanUtils.copyProperties(fotaPlanV2Req, fotaPlanPo);
        FotaPlanPo fotaPlanPo = Dto2PoMapper.INSTANCE.fotaPlanV2Req2FotaPlanPo(fotaPlanV2Req);

    	Long fotaStrategyId = fotaPlanV2Req.getFotaStrategyId();
    	FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(fotaStrategyId);
    	String entireVersionNo = fotaStrategyPo.getEntireVersionNo();
        fotaPlanPo.setTargetVersion(entireVersionNo); // 整车版本

        // 如果是测试任务，需要更新审批状态为免审批
        if (PlanModeStateEnum.isTestsPlan(fotaPlanPo.getPlanMode())) {
        	fotaPlanPo.setApproveState(ApproveStateEnum.NO_NEED_APPROVE.getState());
        }
        
        // 更新升级计划附加参数
        // wrapExtraData(fotaPlanPo, fotaPlanV2Req);
        
        CommonUtil.wrapBasePo4Update(fotaPlanPo, fotaPlanPo.getUpdateBy(), null);
        boolean update = fotaPlanDbService.updateById(fotaPlanPo);
        if (!update) { 
            log.error("任务基础信息更新写入失败. 任务ID : [ {} ]", fotaPlanV2Req.getId());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
        }

        return fotaPlanV2Req.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteById(Long planId) {
        FotaPlanPo fotaPlanDo = fotaPlanDbService.getById(planId);
//        //TODO 魔数
//        if (fotaPlanDo.getPlanStatus() > Enums.PlanStatusTypeEnum.OUTLINE.getType()) {
//            log.warn("当前任务非草稿状态，不允许删除");
//            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_CANT_NOT_DELETE);
//        }
        // V2版本任务删除逻辑
        if (!fotaPlanPublishService.canDeleteFotaPlan(fotaPlanDo)) {
        	log.info("当前任务状态不允许删除");
        	return 0;
        }
        
        ((FotaPlanObjListMapper) fotaPlanObjListDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((FotaPlanFirmwareListMapper) fotaPlanFirmwareListDbService.getBaseMapper()).deleteByPlanIdPhysical(planId);
        ((FotaPlanTaskDetailMapper) planTaskDetailDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((TaskTerminateMapper) taskTerminateDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((UpgradeTaskConditionMapper) upgradeTaskConditionDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        return ((FotaPlanMapper)fotaPlanDbService.getBaseMapper()).deleteByIdPhysical(planId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FotaPlanIsEnableV2RespVo isEnableFotaPlan(FotaPlanIsEnableV2Req fotaPlanIsEnableV2Req) {
        FotaPlanPo exist = fotaPlanDbService.getById(fotaPlanIsEnableV2Req.getId());
        MyAssertUtil.notNull(exist, "升级任务不存在,请检查");
        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        fotaPlanPo.setId(fotaPlanIsEnableV2Req.getId());
        fotaPlanPo.setIsEnable(fotaPlanIsEnableV2Req.getIsEnable());
        
        CommonUtil.wrapBasePo4Update(fotaPlanPo, fotaPlanIsEnableV2Req.getUpdateBy(), null);
        boolean flag = fotaPlanDbService.updateById(fotaPlanPo);
        myOtaEventKit.triggerFotaUpgradePlanSwitchEvent(fotaPlanIsEnableV2Req.getId(), fotaPlanIsEnableV2Req.getIsEnable());
        
        // 重新查询出任务
        fotaPlanPo = fotaPlanDbService.getById(fotaPlanIsEnableV2Req.getId());
        fotaPlanPublishService.selectPublishState(fotaPlanPo);

        FotaPlanIsEnableV2RespVo fotaPlanIsEnableV2RespVo = FotaPlanIsEnableV2RespVo.of();
        fotaPlanIsEnableV2RespVo.setResult(flag);
        PublishStateEnum publishStateEnum = PublishStateEnum.select(fotaPlanPo.getPublishState());
        Optional.ofNullable(publishStateEnum).ifPresent(stateEnum -> {
        	fotaPlanIsEnableV2RespVo.setPublishState(stateEnum.getState());
        	fotaPlanIsEnableV2RespVo.setPublishStateDesc(stateEnum.getKey());
        });

        //更新可能已经开始的属于该车辆的升级状态
        updatePlanObjStatus(fotaPlanPo.getId(), fotaPlanIsEnableV2Req.getIsEnable());

        return fotaPlanIsEnableV2RespVo;
    }

    private void updatePlanObjStatus(Long otaPlanId, Integer isEnable){
        if(Objects.nonNull(isEnable)) {
            boolean enable = WebEnums.IsEnableEnum.ENABLED.getFlag().equals(isEnable);
            if(enable){
                fotaPlanObjListDbService.updateInvalidStatus(otaPlanId, Enums.TaskObjStatusTypeEnum.INSTALLED_PLAN_INVALID.getType(), Enums.TaskObjStatusTypeEnum.CREATED.getType());
            }/*else{
                fotaPlanObjListDbService.updateInvalidStatus(otaPlanId, null, Enums.TaskObjStatusTypeEnum.INSTALLED_PLAN_INVALID.getType());
            }*/
            //Integer planObjListStatus = ? Enums.TaskObjStatusTypeEnum.CREATED.getType() : Enums.TaskObjStatusTypeEnum.INSTALLED_PLAN_INVALID.getType();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveFotaPlan(FotaPlanApproveV2Req fotaPlanApproveV2Req) {
        FotaPlanPo exist = fotaPlanDbService.getById(fotaPlanApproveV2Req.getId());
        MyAssertUtil.notNull(exist, "升级任务不存在,请检查");
        WebEnums.PlanApproveStateEnum planApproveStateEnum = (WebEnums.PlanApproveStateEnum) MyEnumUtil.getByValueWithCheck(fotaPlanApproveV2Req.getApproveStatus(), WebEnums.PlanApproveStateEnum.class);
        log.info("任务审核id={}, 审核状态={}",fotaPlanApproveV2Req.getId(), planApproveStateEnum.getDesc());
        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        fotaPlanPo.setId(fotaPlanApproveV2Req.getId());
        fotaPlanPo.setApproveState(fotaPlanApproveV2Req.getApproveStatus());
        CommonUtil.wrapBasePo4Update(fotaPlanPo, fotaPlanApproveV2Req.getUpdateBy(), null);
        boolean flag = fotaPlanDbService.updateById(fotaPlanPo);
        
        // 审批按钮与发布状态联动
        fotaPlanPo = fotaPlanDbService.getById(fotaPlanApproveV2Req.getId());
        myOtaEventKit.triggerFotaUpgradePlanSwitchEvent(fotaPlanPo.getId(), fotaPlanPo.getIsEnable());
        return flag; 
    }

    @Override
    public Boolean publishFotaPlan(FotaPlanPublishV2Req fotaPlanPublishV2Req) {
        FotaPlanPo exist = fotaPlanDbService.getById(fotaPlanPublishV2Req.getId());
        MyAssertUtil.notNull(exist, "升级任务不存在,请检查");
        WebEnums.PlanPublishStateEnum planPublishStateEnum = (WebEnums.PlanPublishStateEnum) MyEnumUtil.getByValueWithCheck(fotaPlanPublishV2Req.getPublishStatus(), WebEnums.PlanPublishStateEnum.class);
        log.info("任务审核id={}, 审核状态={}",fotaPlanPublishV2Req.getId(), planPublishStateEnum.getDesc());
        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        fotaPlanPo.setId(fotaPlanPublishV2Req.getId());
        fotaPlanPo.setPublishState(fotaPlanPublishV2Req.getPublishStatus());
        CommonUtil.wrapBasePo4Update(fotaPlanPo, fotaPlanPublishV2Req.getUpdateBy(), null);
        return fotaPlanDbService.updateById(fotaPlanPo);
    }

    @Override
    public FotaPlanPo selectFotaPlan(Long planId) {
    	return fotaPlanDbService.getById(planId);
    }

    /**
     * 更新任务状态
     */
	@Override
	public IPage<FotaPlanPo> queryDelayPlanPage(Date datetime, Integer current, Integer pagesize) {
		// 待发布状态，且开关为close的情况下
		FotaPlanDelayQuery fotaPlanDelayQuery = new FotaPlanDelayQuery();
		fotaPlanDelayQuery.setCurrent(current);
		fotaPlanDelayQuery.setPageSize(pagesize);
		fotaPlanDelayQuery.setDatetime(datetime);
		fotaPlanDelayQuery.setPublishStatus(Lists.newArrayList(PublishStateEnum.UNPUBLISH.getState()));
		// fotaPlanDelayQuery.setApprovedStatus(Lists.newArrayList(ApproveStateEnum.READY_FOR_APPROVE.getState(), ApproveStateEnum.IN_PROCESS_APPROVE.getState(), ApproveStateEnum.NO_NEED_APPROVE.getState(), ApproveStateEnum.BE_APPROVED.getState()));
		fotaPlanDelayQuery.setApprovedStatus(Lists.newArrayList(ApproveStateEnum.NO_NEED_APPROVE.getState(), ApproveStateEnum.BE_APPROVED.getState())); // 审批状态已通过或者免审批
		fotaPlanDelayQuery.setEnableState(Lists.newArrayList(EnableStateEnum.CLOSE.getState()));
		return fotaPlanDbService.queryDelayPlanPage(fotaPlanDelayQuery);
	}

}