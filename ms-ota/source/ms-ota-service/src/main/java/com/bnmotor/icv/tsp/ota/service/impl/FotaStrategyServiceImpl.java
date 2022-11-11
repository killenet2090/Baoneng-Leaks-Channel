package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.*;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.Dto2PoMapper;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.Po2VoMapper;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.query.FotaStrategyQuery;
import com.bnmotor.icv.tsp.ota.model.req.web.*;
import com.bnmotor.icv.tsp.ota.model.resp.web.FotaStrategySelectableFirmwareVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName: FotaStrategyPo
 * @Description: OTA升级策略-新表 服务实现类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaStrategyServiceImpl implements IFotaStrategyService {

    @Autowired
    private IFotaStrategyDbService fotaStrategyDbService;

    @Autowired
    IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;

    @Autowired
    IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Autowired
    IFotaFirmwareService fotaFirmwareService;

    @Autowired
    IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;

    @Autowired
    IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

    @Autowired
    IFotaStrategyPreConditionDbService fotaStrategyPreConditionDbService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;
    
    @Autowired
    IFotaFileUploadRecordDbService fotaFileUploadRecordDbService;

    private final Class<?>[] PARAM_CLASSES = {String.class};


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateFotaStrategy(FotaStrategyDto fotaStrategyDto) {
        MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(fotaStrategyDto.getFotaStrategyFirmwareListDtos()), "关联固件列表不能为空");
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getById(Long.parseLong(fotaStrategyDto.getTreeNodeId()));
        MyAssertUtil.notNull(deviceTreeNodePo, "获取设备树节点数据异常");
        MyAssertUtil.isTrue(deviceTreeNodePo.getTreeLevel().equals(DeviceTreeNodeLevelEnum.CONF.getLevel()), "设备树节点非配置节点");

        //检查固件列表是否有重复
        int count = (int)fotaStrategyDto.getFotaStrategyFirmwareListDtos().stream().map(FotaStrategyFirmwareListDto::getFirmwareId).distinct().count();
        MyAssertUtil.isTrue(count == fotaStrategyDto.getFotaStrategyFirmwareListDtos().size(), "固件列表中存在重复项");

        FotaStrategyPo fotaStrategyPo = Dto2PoMapper.INSTANCE.fotaStrategyDto2FotaStrategyPo(fotaStrategyDto);
        Long id = fotaStrategyPo.getId();
        boolean isSave = Objects.isNull(id);
        //新增逻辑
        if(isSave){
            id = IdWorker.getId();
            fotaStrategyPo.setId(id);
        }

        //判断数据库是否存在重复名称或整车版本的策略数据
        checkExist("name", fotaStrategyDto.getName(), isSave, fotaStrategyPo, "策略名称");
        checkExist("entire_version_no", fotaStrategyDto.getEntireVersionNo(), isSave, fotaStrategyPo, "策略整车版本号");

        //计算升级时长
        int estimateUpgradeTime = calcEstimateUpgradeTime(fotaStrategyDto.getFotaStrategyFirmwareListDtos());
        MyAssertUtil.isTrue(fotaStrategyDto.getEstimateUpgradeTime().intValue() == estimateUpgradeTime, "升级时长计算异常");

        //补全FotaStrategyPo对象中设备树层级下属性
        wrapFotaStrategyPo(deviceTreeNodePo, fotaStrategyPo);
        CommonUtil.wrapBasePo4Date(fotaStrategyPo, null, isSave);
        fotaStrategyPo.setStatus(0);

        fotaStrategyDbService.saveOrUpdate(fotaStrategyPo);

        //保存策略固件详情列表
        saveOrUpdateStrategyFirmwareLists(isSave, fotaStrategyDto, fotaStrategyPo);

        //保存前置条件检查列表
        saveOrUpdateStrategyPreConditions(isSave, fotaStrategyPo, fotaStrategyDto.getFotaStrategyPreConditionDtos());

        return fotaStrategyPo.getId();
    }

    @Override
    public int calcEstimateUpgradeTime(final List<FotaStrategyFirmwareListDto> fotaStrategyFirmwareListDtos){
        AtomicLong estimateUpgradeTime = new AtomicLong(0L);
        if(MyCollectionUtil.isNotEmpty(fotaStrategyFirmwareListDtos)) {
            fotaStrategyFirmwareListDtos.stream().forEach(item -> {
                //固件是否存在
                Long firmwareId = Long.parseLong(item.getFirmwareId());
                FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(firmwareId);
                MyAssertUtil.notNull(fotaFirmwarePo, "选择固件数据异常");

                MyAssertUtil.notNull(item.getTargetVersionId(), "选择固件目标版本参数未传递");

                //固件版本是否存在
                Long firmwareVersionId = Long.parseLong(item.getTargetVersionId());
                List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareService.listFirmwareVersions(null, firmwareId);
                MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPos) && fotaFirmwareVersionPos.stream().filter(item0 -> item0.getId().equals(firmwareVersionId)).findFirst().isPresent(), "选择的固件版本数据异常");

                FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(firmwareVersionId);
                MyAssertUtil.notNull(fotaFirmwareVersionPo, "选择的固件版本数据异常");
                MyAssertUtil.notNull(fotaFirmwareVersionPo.getFullPkgId(), "选择的固件版本全量包数据异常");

                //检查固件各个版本的升级包是否存在
                boolean pkgBuildSuccess = fotaFirmwareService.isFirmwarePkgBuildedSuccess(fotaFirmwarePo.getId(), Long.parseLong(item.getTargetVersionId()));
                if (!pkgBuildSuccess) {
                    throw ExceptionUtil.buildAdamException(OTARespCodeEnum.BUILD_DATA_NOT_FINISH, "固件" + fotaFirmwarePo.getFirmwareCode() + "存在版本升级包未构建完成或构建失败");
                }

                FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(fotaFirmwareVersionPo.getFullPkgId());
                MyAssertUtil.notNull(fotaFirmwarePkgPo, "选择的固件版本全量包数据异常");
                estimateUpgradeTime.addAndGet(fotaFirmwarePkgPo.getEstimateFlashTime());
            });
        }
        int intEstimateUpgradeTime = Double.valueOf(Math.ceil(estimateUpgradeTime.get() * CommonConstant.FOTA_FIRMWARE_VERSION_PKG_ESTIMATE_UPGRADE_TIME_SCALE)).intValue();
        log.info("计算升级时长={}", intEstimateUpgradeTime);
        return intEstimateUpgradeTime;
    }

    /**
     * 新增/更新策略固件列表
     * @param isSave
     * @param fotaStrategyDto
     * @param fotaStrategyPo
     */
    private void saveOrUpdateStrategyFirmwareLists(boolean isSave, final FotaStrategyDto fotaStrategyDto, final FotaStrategyPo fotaStrategyPo){
        AtomicInteger orderNum = new AtomicInteger(0);
        FotaStrategyFirmwareListDto maxFotaStrategyFirmwareListDto = fotaStrategyDto.getFotaStrategyFirmwareListDtos().stream().max(Comparator.comparingInt((FotaStrategyFirmwareListDto item) -> {
            if (Objects.isNull(item.getGroupSeq())) {
                return 0;
            } else {
                return item.getGroupSeq();
            }
        })).get();
        Integer maxGroupSeq = maxFotaStrategyFirmwareListDto.getGroupSeq();
        AtomicInteger atomicMaxGroupSeq = new AtomicInteger(Objects.isNull(maxGroupSeq) ? 0 : maxGroupSeq.intValue());
        List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyDto.getFotaStrategyFirmwareListDtos().stream().map(item -> {
            //固件是否存在
            Long firmwareId = Long.parseLong(item.getFirmwareId());
            FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(firmwareId);
            MyAssertUtil.notNull(fotaFirmwarePo, "选择固件数据异常");

            MyAssertUtil.notNull(item.getTargetVersionId(), "选择固件("+fotaFirmwarePo.getFirmwareCode()+")目标版本参数未传递");

            //固件版本是否存在
            Long firmwareVersionId = Long.parseLong(item.getTargetVersionId());
            List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareService.listFirmwareVersions(null, firmwareId);
            MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPos) && fotaFirmwareVersionPos.stream().filter(item0 -> item0.getId().equals(firmwareVersionId)).findFirst().isPresent(), "选择的固件版本数据异常");

            FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(firmwareVersionId);
            MyAssertUtil.notNull(fotaFirmwareVersionPo, "选择的固件("+fotaFirmwarePo.getFirmwareCode()+")版本数据异常");
            MyAssertUtil.notNull(fotaFirmwareVersionPo.getFullPkgId(), "选择的固件("+fotaFirmwarePo.getFirmwareCode()+")版本("+fotaFirmwareVersionPo.getFirmwareVersionNo()+")全量包数据异常");

            //检查固件各个版本的升级包是否存在
            boolean pkgBuildSuccess = fotaFirmwareService.isFirmwarePkgBuildedSuccess(fotaFirmwarePo.getId(), Long.parseLong(item.getTargetVersionId()));
            if(!pkgBuildSuccess){
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.BUILD_DATA_NOT_FINISH, "固件"+fotaFirmwarePo.getFirmwareCode()+"存在版本升级包未构建完成或构建失败");
            }

            FotaStrategyFirmwareListPo fotaStrategyFirmwareListPo = new FotaStrategyFirmwareListPo();
            fotaStrategyFirmwareListPo.setId(IdWorker.getId());
            fotaStrategyFirmwareListPo.setProjectId(fotaStrategyPo.getProjectId());
            fotaStrategyFirmwareListPo.setOtaStrategyId(fotaStrategyPo.getId());
            fotaStrategyFirmwareListPo.setFirmwareId(Long.parseLong(item.getFirmwareId()));
            fotaStrategyFirmwareListPo.setUpgradeMode(item.getUpgradeMode());
            fotaStrategyFirmwareListPo.setTargetVersionId(Long.parseLong(item.getTargetVersionId()));
            fotaStrategyFirmwareListPo.setOrderNum(orderNum.incrementAndGet());
            fotaStrategyFirmwareListPo.setGroupSeq(item.getGroupSeq());
            //如果不存在依赖组，设置所有ecu依赖组值=当前ecu升级顺序
            if (Objects.isNull(fotaStrategyFirmwareListPo.getGroupSeq())) {
                fotaStrategyFirmwareListPo.setDbGroupSeq(atomicMaxGroupSeq.incrementAndGet());
            }else{
                //存在依赖组，需要检查参数是否已经设置
                fotaStrategyFirmwareListPo.setDbGroupSeq(fotaStrategyFirmwareListPo.getGroupSeq());
            }
            fotaStrategyFirmwareListPo.setVersion(0);
            CommonUtil.wrapBasePo(fotaStrategyFirmwareListPo, isSave ? fotaStrategyPo.getCreateBy() : fotaStrategyPo.getUpdateBy(), isSave);
            return fotaStrategyFirmwareListPo;
        }).collect(Collectors.toList());
        if(!isSave){
            //删除之前可能存在的策略固件关系列表
            fotaStrategyFirmwareListDbService.delByStrategyIdPhysical(fotaStrategyPo.getId());
        }
        fotaStrategyFirmwareListDbService.saveBatch(fotaStrategyFirmwareListPos);
    }

    /**
     * 添加/更细前置条件
     *
     * @param fotaStrategyPo
     * @param fotaStrategyPreConditionDtos
     */
    private void saveOrUpdateStrategyPreConditions(boolean isSave, final FotaStrategyPo fotaStrategyPo, final List<FotaStrategyPreConditionDto> fotaStrategyPreConditionDtos){
        if(MyCollectionUtil.isNotEmpty(fotaStrategyPreConditionDtos)){
            List<FotaStrategyPreConditionPo> fotaStrategyPreConditionPos = MyCollectionUtil.map2NewList(fotaStrategyPreConditionDtos, item ->{
                FotaStrategyPreConditionPo fotaStrategyPreConditionPo = Dto2PoMapper.INSTANCE.fotaStrategyPreConditionDto2FotaStrategyPreConditionPo(item);
                WebEnums.PreConditionTypeEnum preConditionTypeEnum = (WebEnums.PreConditionTypeEnum)MyEnumUtil.getByValue(Integer.parseInt(item.getCondCode()), WebEnums.PreConditionTypeEnum.class);
                if(Objects.nonNull(preConditionTypeEnum)){
                    fotaStrategyPreConditionPo.setOperatorType(preConditionTypeEnum.getOperateType());
                    fotaStrategyPreConditionPo.setOperatorValue(preConditionTypeEnum.getOperateValue());
                }
                fotaStrategyPreConditionPo.setId(IdWorker.getId());
                fotaStrategyPreConditionPo.setOtaStrategyId(fotaStrategyPo.getId());
                CommonUtil.wrapBasePo(fotaStrategyPreConditionPo, fotaStrategyPo.getCreateBy(), true);
                return fotaStrategyPreConditionPo;
            });
            if(!isSave) {
                fotaStrategyPreConditionDbService.delByStrategyIdPhysical(fotaStrategyPo.getId());
            }
            fotaStrategyPreConditionDbService.saveBatch(fotaStrategyPreConditionPos);
        }else{
            log.warn("策略前置条件列表未空,请检查!fotaStrategyPo={}", fotaStrategyPo);
        }
    }
    /**
     * 补全策略一些属性字段值
     * @param deviceTreeNodePo
     * @param fotaStrategyPo
     */
    private void wrapFotaStrategyPo(DeviceTreeNodePo deviceTreeNodePo, FotaStrategyPo fotaStrategyPo){
        String nodeIdPath = deviceTreeNodePo.getNodeIdPath();
        if(nodeIdPath.startsWith("/")){
            nodeIdPath = nodeIdPath.substring(1);
        }
        String[] treeNodeIdStrs = nodeIdPath.split("/");
        log.info("treeNodeIdStrs={}", treeNodeIdStrs);
        List<Long> treeNodeIds = Stream.of(treeNodeIdStrs).map(item -> Long.parseLong(item)).collect(Collectors.toList());
        List<DeviceTreeNodePo> deviceTreeNodePos = deviceTreeNodeDbService.listByIds(treeNodeIds);
        deviceTreeNodePos.forEach(item -> {
            DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum = DeviceTreeNodeLevelEnum.getByLevel(item.getTreeLevel());
            if(Objects.isNull(deviceTreeNodeLevelEnum)){
                log.warn("设备树节点节点层次异常.item={}", item);
                throw new AdamException("设备树节点节点层次异常");
            }
            String getNameMethodName = "set" + (deviceTreeNodeLevelEnum.getCode().substring(0, 1).toUpperCase()) + deviceTreeNodeLevelEnum.getCode().substring(1, deviceTreeNodeLevelEnum.getCode().length());
            //设置对应属性
            ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(FotaStrategyPo.class, getNameMethodName, PARAM_CLASSES), fotaStrategyPo, item.getNodeName());
        });
    }

    /**
     * 检查名称或整车版本是否重复
     * @param columnName
     * @param columnValue
     * @param isSave
     * @param fotaStrategyPo
     * @param paramDesc
     */
    private void checkExist(String columnName, String columnValue, boolean isSave, FotaStrategyPo fotaStrategyPo, String paramDesc){
        //判断数据库是否存在重复名称或整车版本的策略数据
        List<FotaStrategyPo> fotaStrategyPos1 = fotaStrategyDbService.listByColumnValue(columnName, columnValue);
        if(MyCollectionUtil.isNotEmpty(fotaStrategyPos1)){
            boolean existOther = fotaStrategyPos1.stream().filter(item -> !item.getId().equals(fotaStrategyPo.getId())).findAny().isPresent();
            if(isSave || existOther){
                log.warn(paramDesc +"不能重复.fotaStrategyPo={}", fotaStrategyPo);
                throw new AdamException( paramDesc + "不能重复");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delFotaStrategy(Long id) {
        List<FotaPlanPo> fotaPlanPos = fotaPlanDbService.listWithOtaStrategyId(id);
        if(MyCollectionUtil.isNotEmpty(fotaPlanPos)){
            log.warn("策略已经绑定了任务，禁止删除.id={}", fotaPlanPos);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_STRATEGY_NOT_ALLOWED_ERROR);
        }
        fotaStrategyDbService.getBaseMapper().deleteById(id);
        fotaStrategyFirmwareListDbService.deleteByOtaStrategyId(id);
    }

    @Override
    public IPage<FotaStrategyDto> list(FotaStrategyQuery fotaStrategyQuery) {
        boolean isPageable = Objects.nonNull(fotaStrategyQuery.getCurrent()) && Objects.nonNull(fotaStrategyQuery.getPageSize());
        QueryWrapper<FotaStrategyPo> queryWrapper = buildQueryWrapper(fotaStrategyQuery);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = new Page(fotaStrategyQuery.getCurrent(), fotaStrategyQuery.getPageSize());

        List<FotaStrategyPo> fotaStrategyPos;
        IPage<FotaStrategyDto> iPageDto = new Page(0, 0, 0);
        if(isPageable){
            IPage<FotaStrategyPo> iPage = fotaStrategyDbService.getBaseMapper().selectPage(page, queryWrapper);
            iPageDto = new Page(iPage.getCurrent(), iPage.getSize(), iPage.getTotal());
            fotaStrategyPos = iPage.getRecords();
        }else{
            fotaStrategyPos = fotaStrategyDbService.getBaseMapper().selectList(queryWrapper);
        }
        iPageDto.setRecords(MyCollectionUtil.map2NewList(fotaStrategyPos, item ->{
            FotaStrategyDto fotaStrategyDto = Po2VoMapper.INSTANCE.fotaStrategyPo2FotaStrategyDto(item);
            //设置前端状态枚举
            WebEnums.StrategyStatusEnum strategyStatusEnum = (WebEnums.StrategyStatusEnum)MyEnumUtil.getByValue(fotaStrategyDto.getStatus(), WebEnums.StrategyStatusEnum.class);
            fotaStrategyDto.setStatusDesc(Objects.nonNull(strategyStatusEnum) ? strategyStatusEnum.getDesc() : "");
            // 填充策略的验证报告
            Long fileRecordId = item.getFileRecordId();
            paddingReportInfo(fotaStrategyDto, fileRecordId);
            return fotaStrategyDto;
        }));
        iPageDto.getRecords().stream().sorted(Comparator.comparing(FotaStrategyDto::getCreateTime));
        return iPageDto;
    }

    /**
     *
     * @param fotaStrategyQuery
     * @return
     */
    private QueryWrapper buildQueryWrapper(FotaStrategyQuery fotaStrategyQuery){
        QueryWrapper<FotaStrategyPo> queryWrapper = new QueryWrapper<>();
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> Objects.nonNull(item.getTreeNodeId()), (FotaStrategyQuery item) -> queryWrapper.eq("tree_node_id", Long.parseLong(item.getTreeNodeId())));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> !StringUtils.isEmpty(item.getBrand()), (FotaStrategyQuery item) -> queryWrapper.eq("brand", item.getBrand()));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> !StringUtils.isEmpty(item.getSeries()), (FotaStrategyQuery item) -> queryWrapper.eq("series", item.getSeries()));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> !StringUtils.isEmpty(item.getModel()), (FotaStrategyQuery item) -> queryWrapper.eq("model", item.getModel()));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> !StringUtils.isEmpty(item.getYear()), (FotaStrategyQuery item) -> queryWrapper.eq("year", item.getYear()));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> !StringUtils.isEmpty(item.getConf()), (FotaStrategyQuery item) -> queryWrapper.eq("conf", item.getConf()));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> Objects.nonNull(item.getStatus()), (FotaStrategyQuery item) -> queryWrapper.eq("status", item.getStatus()));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> Objects.nonNull(item.getIsEnable()), (FotaStrategyQuery item) -> queryWrapper.eq("is_enable", item.getIsEnable()));
        MyPredicateUtil.ifTest(fotaStrategyQuery, item -> Objects.nonNull(item.getPlanMode()), (FotaStrategyQuery item) ->{
            if(Objects.nonNull(item.getPlanMode())){
                /*if(PlanModeStateEnum.TEST.equals(item.getPlanMode())){
                    queryWrapper.eq("status", item.getStatus());
                }else */if(PlanModeStateEnum.FORMAL.getState().equals(item.getPlanMode())){
                     queryWrapper.eq("status", ApproveStateEnum.BE_APPROVED.getState());
                }
            }
        });
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    public List<FotaStrategyDto> list(Long treeNodeId) {
        FotaStrategyQuery fotaStrategyQuery = new FotaStrategyQuery();
        fotaStrategyQuery.setTreeNodeId(Long.toString(treeNodeId));
        QueryWrapper<FotaStrategyPo> queryWrapper = buildQueryWrapper(fotaStrategyQuery);
        List<FotaStrategyPo> fotaStrategyPos = fotaStrategyDbService.list(queryWrapper);
        List<FotaStrategyDto> fotaStrategyDtos =  fotaStrategyPos.stream().map(item ->{
            FotaStrategyDto fotaStrategyDto = Po2VoMapper.INSTANCE.fotaStrategyPo2FotaStrategyDto(item);
            //设置前端状态枚举
            WebEnums.StrategyStatusEnum strategyStatusEnum = (WebEnums.StrategyStatusEnum)MyEnumUtil.getByValue(fotaStrategyDto.getStatus(), WebEnums.StrategyStatusEnum.class);
            fotaStrategyDto.setStatusDesc(Objects.nonNull(strategyStatusEnum) ? strategyStatusEnum.getDesc() : "");
            return fotaStrategyDto;
        }).collect(Collectors.toList());
        return fotaStrategyDtos;
    }

    @Override
    public FotaStrategyDto findOneFotaStrategy(Long id) {
        FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(id);
        FotaStrategyDto fotaStrategyDto = Po2VoMapper.INSTANCE.fotaStrategyPo2FotaStrategyDto(fotaStrategyPo);
        // 填充策略审批报告
        paddingReportInfo(fotaStrategyDto, fotaStrategyPo.getFileRecordId());
        
        List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListDbService.listByOtaStrategyId(id);
        if(MyCollectionUtil.isEmpty(fotaStrategyFirmwareListPos)){
            log.warn("ecu固件与策略管理列表未空.id={}", id);
            return fotaStrategyDto;
        }
        fotaStrategyDto.setFotaStrategyFirmwareListDtos(fotaStrategyFirmwareListPos.stream().map(item -> {
            FotaStrategyFirmwareListDto fotaStrategyFirmwareListDto = Po2VoMapper.INSTANCE.fotaStrategyPo2FotaStrategyDto(item);
            FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(Long.parseLong(fotaStrategyFirmwareListDto.getFirmwareId()));
            fotaStrategyFirmwareListDto.setComponentCode(fotaFirmwarePo.getComponentCode());
            fotaStrategyFirmwareListDto.setComponentName(fotaFirmwarePo.getComponentName());
            fotaStrategyFirmwareListDto.setFirmwareName(fotaFirmwarePo.getFirmwareName());
            fotaStrategyFirmwareListDto.setFirmwareCode(fotaFirmwarePo.getFirmwareCode());

            FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(item.getTargetVersionId());
            fotaStrategyFirmwareListDto.setTargetVersion(fotaFirmwareVersionPo.getFirmwareVersionNo());
            return fotaStrategyFirmwareListDto;
        }).collect(Collectors.toList()));

        List<FotaStrategyPreConditionPo> fotaStrategyPreConditionDtos = fotaStrategyPreConditionDbService.listByOtaStrategyId(id);
        fotaStrategyDto.setFotaStrategyPreConditionDtos(fotaStrategyPreConditionDtos.stream().map(item -> {
            FotaStrategyPreConditionDto fotaStrategyPreConditionDto = Po2VoMapper.INSTANCE.fotaStrategyPreConditionPo2FotaStrategyPreConditionDto(item);
            return fotaStrategyPreConditionDto;
        }).collect(Collectors.toList()));

        //添加设备树路径描述
        StringBuilder stringBuilder = new StringBuilder();
        paddingTreeNodeDesc(fotaStrategyPo.getTreeNodeId(), stringBuilder);
        fotaStrategyDto.setTreeNodeDesc(stringBuilder.toString());
        return fotaStrategyDto;
    }

    /**
     * 拼接设备树节点路径描述
     * @param treeNodeId
     * @param stringBuilder
     */
    private void paddingTreeNodeDesc(Long treeNodeId, final StringBuilder stringBuilder){
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getById(treeNodeId);
        if(deviceTreeNodePo.getTreeLevel().equals(DeviceTreeNodeLevelEnum.CONF.getLevel())) {
            stringBuilder.insert(0, deviceTreeNodePo.getNodeName());
        }else{
            stringBuilder.insert(0, deviceTreeNodePo.getNodeName() + "/");
        }
        if(Objects.nonNull(deviceTreeNodePo.getParentId())){
            paddingTreeNodeDesc(deviceTreeNodePo.getParentId(), stringBuilder);
        }
    }
    
    public void paddingReportInfo(FotaStrategyDto fotaStrategyDto, Long fileRecordId) {
    	if (Objects.isNull(fileRecordId)) {
    		return;
    	}
    	
    	FotaFileUploadRecordPo fotaFileUploadRecordPo = fotaFileUploadRecordDbService.getById(fileRecordId);
    	if (Objects.isNull(fotaFileUploadRecordPo)) {
    		return;
    	}
    	
    	String reportName = fotaFileUploadRecordPo.getFileName();
    	String reportUrl = fotaFileUploadRecordPo.getFileAddress();
    	fotaStrategyDto.setReportName(reportName);
    	fotaStrategyDto.setReportUrl(reportUrl);
    	fotaStrategyDto.setFileRecordId(fileRecordId);
    }

    @Override
    public void strategyAudit(FotaStrategyAuditDto fotaStrategyAuditDto) {
        WebEnums.StrategyStatusEnum strategyStatusEnum = (WebEnums.StrategyStatusEnum)MyEnumUtil.getByValueWithCheck(fotaStrategyAuditDto.getStatus(), WebEnums.StrategyStatusEnum.class);

        FotaStrategyPo fotaStrategyPo = new FotaStrategyPo();
        fotaStrategyPo.setId(Long.parseLong(fotaStrategyAuditDto.getId()));
        //TODO 临时设置提交即审核通过
        /*if(strategyStatusEnum.equals(WebEnums.StrategyStatusEnum.TO_AUDIT)){
            fotaStrategyPo.setStatus(WebEnums.StrategyStatusEnum.AUDIT_PASSED.getValue());
        }else{
            fotaStrategyPo.setStatus(strategyStatusEnum.getValue());
        }*/
        fotaStrategyPo.setStatus(strategyStatusEnum.getValue());
        fotaStrategyPo.setUpdateBy(fotaStrategyAuditDto.getUpdateBy());
        fotaStrategyPo.setUpdateTime(LocalDateTime.now());
        fotaStrategyDbService.updateById(fotaStrategyPo);
    }

    @Override
    public List<FotaStrategySelectableFirmwareVo> listSelectableFirmwares(Long treeNodeId){
        List<FotaFirmwarePo> fotaFirmwarePos = fotaFirmwareService.listFirmwarePos(treeNodeId);
        if(MyCollectionUtil.isEmpty(fotaFirmwarePos)){
            log.warn("fotaFirmwarePos.size is zero. treeNodeId={}", treeNodeId);
            List<FotaStrategySelectableFirmwareVo> emptyList = Lists.newArrayList();
            //emptyList.add(null);
            return emptyList;
        }
        List<FotaStrategySelectableFirmwareVo> fotaStrategySelectableFirmwareVos = MyCollectionUtil.map2NewList(fotaFirmwarePos, item -> {
            MyAssertUtil.notNull(item.getPackageModel(), "固件升级模式异常，请检查数据");
            FotaStrategySelectableFirmwareVo fotaStrategySelectableFirmwareVo = Po2VoMapper.INSTANCE.fotaFirmwarePo2FotaStrategySelectableFirmwareVo(item);
            fotaStrategySelectableFirmwareVo.setFirmwareId(Long.toString(item.getId()));
            //使用全量或差分升级包
            if(Enums.FirmwareUpgradeModeTypeEnum.WHOLE.getType() == item.getPackageModel().intValue()){
                fotaStrategySelectableFirmwareVo.setUpgradeModes(Lists.newArrayList(Enums.FirmwareUpgradeModeTypeEnum.WHOLE.getType()));
            }else if(Enums.FirmwareUpgradeModeTypeEnum.DIFF.getType() == item.getPackageModel().intValue()){
                fotaStrategySelectableFirmwareVo.setUpgradeModes(Lists.newArrayList(Enums.FirmwareUpgradeModeTypeEnum.WHOLE.getType(), Enums.FirmwareUpgradeModeTypeEnum.DIFF.getType()));
            }

            //TODO 是否需要排除第一个初始版本
            List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareService.listFirmwareVersions(null, item.getId());
            //设置可能的目标版本
            fotaStrategySelectableFirmwareVo.setTargetVersions(MyCollectionUtil.map2NewList(fotaFirmwareVersionPos, item1 -> {
                FotaStrategySelectableFirmwareVo.FirmwareVersionInfo firmwareVersionInfo = new FotaStrategySelectableFirmwareVo.FirmwareVersionInfo();
                firmwareVersionInfo.setTargetVersionId(Long.toString(item1.getId()));
                firmwareVersionInfo.setTargetVersionName(item1.getFirmwareVersionName());

                FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(item1.getId());
                if(Objects.nonNull(fotaFirmwareVersionPo.getFullPkgId())){
                    FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(fotaFirmwareVersionPo.getFullPkgId());
                    firmwareVersionInfo.setEstimateUpgradeTime(Objects.nonNull(fotaFirmwarePkgPo) ? fotaFirmwarePkgPo.getEstimateFlashTime().intValue() : 0);
                }else{
                    firmwareVersionInfo.setEstimateUpgradeTime(0);
                }
                return firmwareVersionInfo;
            }));
            return fotaStrategySelectableFirmwareVo;
        });
        return MyCollectionUtil.safeList(fotaStrategySelectableFirmwareVos);
    }

    @Override
    public String latestEntireVersionNo(String treeNodeId){
        FotaStrategyQuery fotaStrategyQuery = new FotaStrategyQuery();
        fotaStrategyQuery.setTreeNodeId(treeNodeId);
        IPage<FotaStrategyDto> iPage = list(fotaStrategyQuery);
        if(Objects.nonNull(iPage) && MyCollectionUtil.isNotEmpty(iPage.getRecords())){
            return iPage.getRecords().get(0).getEntireVersionNo();
        }
        return null;
    }

    @Override
    public void strategyEnable(FotaStrategyEnableDto fotaStrategyEnableDto) {
        //校验参数
        MyAssertUtil.isTrue(1== fotaStrategyEnableDto.getIsEnable() || 0== fotaStrategyEnableDto.getIsEnable(), OTARespCodeEnum.PARAMETER_VALIDATION_ERROR);

        //如果是关停策略，需要过滤掉已经关联任务的策略
        if(WebEnums.IsEnableEnum.NOT_ENABLED.getFlag().equals(fotaStrategyEnableDto.getIsEnable())){
            List<FotaPlanPo> fotaPlanPos = fotaPlanDbService.listWithOtaStrategyId(Long.valueOf(fotaStrategyEnableDto.getId()));
            MyAssertUtil.isTrue(MyCollectionUtil.isEmpty(fotaPlanPos), "策略已经绑定任务，不允许关闭");
        }

        FotaStrategyPo fotaStrategyPo = new FotaStrategyPo();
        fotaStrategyPo.setId(Long.parseLong(fotaStrategyEnableDto.getId()));
        fotaStrategyPo.setIsEnable(fotaStrategyEnableDto.getIsEnable());
        CommonUtil.wrapBasePo4Update(fotaStrategyPo, fotaStrategyEnableDto.getUpdateBy(), null);
        fotaStrategyDbService.updateById(fotaStrategyPo);
    }

	@Override
	public boolean accociateStrategyReport(FotaStrategyReportDto fotaStrategyReportDto) {
		Long primaryKey = fotaStrategyReportDto.getStrategyId();
		Long fileRecordId = fotaStrategyReportDto.getFileUploadRecordId();
		String remark = fotaStrategyReportDto.getRemark();
		FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(primaryKey);
		fotaStrategyPo.setFileRecordId(fileRecordId);
		fotaStrategyPo.setRemark(remark);
		return fotaStrategyDbService.updateById(fotaStrategyPo);
	}
}
