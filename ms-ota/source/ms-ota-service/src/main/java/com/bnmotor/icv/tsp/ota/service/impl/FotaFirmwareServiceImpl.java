package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.event.MyOtaEventKit;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventKit;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventType;
import com.bnmotor.icv.tsp.ota.config.MinIoConfig;
import com.bnmotor.icv.tsp.ota.event.FotaDeviceSyncMessage;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.Dto2PoMapper;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.UpdatePkgReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradePkgReq;
import com.bnmotor.icv.tsp.ota.model.req.feign.BuildPackageDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaFirmwareDto;
import com.bnmotor.icv.tsp.ota.model.resp.FotaFirmwarePkgVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.service.feign.BuildPackageService;
import com.bnmotor.icv.tsp.ota.util.*;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xuxiaochang1
 * @ClassName: FotaFirmwarePo
 * @Description: OTA固件信息，定义各个零部件需要支持的OTA升级软件服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-06-11
 */

@Service
@Slf4j
public class FotaFirmwareServiceImpl implements IFotaFirmwareService {
    @Autowired
    private IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    private IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;

    @Autowired
    private IFotaFirmwareVersionDependenceService fotaFirmwareVersionDependenceService;

    @Autowired
    private IFotaFirmwareVersionPathDbService fotaFirmwareVersionPathDbService;

    @Autowired
    private IFotaFirmwarePkgService fotaFirmwarePkgService;

    @Autowired
    private IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

    @Autowired
    private IFotaFileUploadRecordDbService fotaFileUploadRecordDbService;

    @Autowired
    private IFotaFileUploadRecordService fotaFileUploadRecordService;

    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Autowired
    private IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;

    @Autowired
    private BuildPackageService buildPackageService;

    @Autowired
    MyOtaEventKit myOtaEventKit;

    @Autowired
    OtaEventKit otaEventKit;

    @Autowired
    MinIoConfig minIoConfig;

    /**
     * 加密开关
     */
    @Value("${pki.encrypt.switch}")
    Integer encryptSwitch;

    /**
     * 根据固件Code获取固件
     *
     * @param firmwareCode
     * @return
     */
    /*private List<FotaFirmwarePo> getByFrimwareCodes(String firmwareCode) {
        QueryWrapper<FotaFirmwarePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("firmware_code", firmwareCode);
        return baseMapper.selectList(queryWrapper);
    }*/

    /**
     * 检查固件是否存在
     *
     * @param projectId
     * @param firmwareCode
     */
    private void checkExistFotaFirmware(String projectId, String firmwareCode) {
        List<FotaFirmwarePo> existList = fotaFirmwareDbService.getByFrimwareCodes(firmwareCode);
        if (MyCollectionUtil.isNotEmpty(existList)) {
            log.warn("exist firmware,quit.projectId={}, firmwareCode={}", projectId, firmwareCode);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_NEW_EXIST_ERROR);
        }
        return;
    }

    /**
     *
     * @param componentCode
     * @param componentModel
     */
    private void checkExistFotaFirmwareV1(String componentCode, String componentModel) {
        List<FotaFirmwarePo> existList = fotaFirmwareDbService.getByComponentCodeAndModel(componentCode, componentModel);
        if (MyCollectionUtil.isNotEmpty(existList)) {
            log.warn("exist firmware,quit.componentCode={}, componentModel={}", componentCode, componentModel);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_NEW_EXIST_ERROR);
        }
        return;
    }

    @Override
    public FotaFirmwarePo getByFrimwareCode(String projectId, String firmwareCode) {
        return fotaFirmwareDbService.getByFrimwareCode(/*projectId, */firmwareCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFotaFirmware(FotaFirmwareDto fotaFirmwareDto) {
        FotaFirmwarePo fotaFirmwarePo = Dto2PoMapper.INSTANCE.fotaFirmwareDto2FotaFirmwarePo(fotaFirmwareDto);

        //get parent DeviceTreeNode
        Long treeNodeId = fotaFirmwarePo.getTreeNodeId();
        DeviceTreeNodePo parent = deviceTreeNodeDbService.getById(treeNodeId);
        MyAssertUtil.notNull(parent, OTARespCodeEnum.DATA_NOT_FOUND);

        //check FotaFirmware exist
        checkExistFotaFirmware(fotaFirmwarePo.getProjectId(), fotaFirmwarePo.getFirmwareCode());
        //checkExistFotaFirmwareV1(fotaFirmwarePo.getComponentCode(), fotaFirmwarePo.getComponentModel());

        /*fotaFirmwarePo.setInfoCollScriptType(fotaFirmwareDto.getBusinessProcScript());
        fotaFirmwarePo.setInstAlgorithmType(fotaFirmwareDto.getSafeAlgorithm());*/


        CommonUtil.beanAttributeValueTrimPo(fotaFirmwarePo);

        fotaFirmwarePo.setTreeNodeId(treeNodeId);
        LocalDateTime date = LocalDateTime.now();
        //add firmware record
        fotaFirmwarePo.setId(IdWorker.getId());
        fotaFirmwarePo.setCreateTime(date);
        fotaFirmwareDbService.save(fotaFirmwarePo);

        //需要同步固件与零件绑定关系到车辆固件列表
        otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_FOTA_DEVICE_SYNC_MESSAGE, new FotaDeviceSyncMessage(treeNodeId, fotaFirmwarePo.getId(), Enums.DeviceSyncActionTypeEnum.ADD.getType()));
    }

    @Override
    public void updateFotaFirmware(FotaFirmwarePo fotaFirmwarePo) {
        //check FotaFirmware exist
        List<FotaFirmwarePo> existList = fotaFirmwareDbService.getByFrimwareCodes(fotaFirmwarePo.getFirmwareCode());
        if (MyCollectionUtil.isNotEmpty(existList)) {
            for (FotaFirmwarePo item : existList) {
                MyAssertUtil.isTrue(item.getId().equals(Long.parseLong(fotaFirmwarePo.getIdStr())), OTARespCodeEnum.FIRMWARE_CODE_NEW_EXIST_ERROR);
            }
        }

        FotaFirmwarePo exist = fotaFirmwareDbService.getById(Long.parseLong(fotaFirmwarePo.getIdStr()));
        MyAssertUtil.notNull(exist, OTARespCodeEnum.FIRMWARE_NOT_EXIST_ERROR);

        CommonUtil.beanAttributeValueTrimPo(fotaFirmwarePo);

        LocalDateTime date = LocalDateTime.now();
        Integer originVersion = exist.getVersion();
        fotaFirmwarePo.setId(Long.parseLong(fotaFirmwarePo.getIdStr()));
        fotaFirmwarePo.setUpdateTime(date);
        fotaFirmwarePo.setVersion(originVersion + 1);
        boolean update = this.updateByIdWithVersion(fotaFirmwarePo, originVersion);
        if (!update) {
            log.error("update fotaFirmwarePo error.fotaFirmwarePo={}", fotaFirmwarePo.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_UPDATE_ERROR);
        }

        otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_FOTA_DEVICE_SYNC_MESSAGE, new FotaDeviceSyncMessage(exist.getTreeNodeId(), fotaFirmwarePo.getId(), Enums.DeviceSyncActionTypeEnum.UPDATE.getType()));
        //TODO 是否需同步到升级车辆固件关联表中去
    }

    /**
     * 并发更新
     *
     * @param fotaFirmwarePo
     * @param version
     * @return
     */
    private boolean updateByIdWithVersion(FotaFirmwarePo fotaFirmwarePo, Integer version) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", fotaFirmwarePo.getId());
        updateWrapper.eq("version", version);
        return fotaFirmwareDbService.update(fotaFirmwarePo, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delFotaFirmware(String projectId, long firmwareId) {
        FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(firmwareId);
        MyAssertUtil.notNull(fotaFirmwarePo, OTARespCodeEnum.FIRMWARE_NOT_EXIST_ERROR);

        List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareVersionDbService.list(projectId, firmwareId);
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPos)) {
            log.warn("exist firmware version records,not allowed deleted.projectId={},firmwareId={}", projectId, firmwareId);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_WITHVERSION_NOTALLOWED_DEL);
        }

        List<Long> pkgIds = Lists.newArrayList();
        List<Long> versionIds = Lists.newArrayList();
        List<Long> versionPathIds = Lists.newArrayList();
        List<Long> versionDependenceIds = Lists.newArrayList();
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPos)) {
            pkgIds = fotaFirmwareVersionPos.stream().map(item -> item.getFullPkgId()).collect(Collectors.toList());
            versionIds = fotaFirmwareVersionPos.stream().map(item -> item.getId()).collect(Collectors.toList());

            //TODO move to service impl
            List<FotaFirmwareVersionDependencePo> fotaFirmwareVersionDependencePos = fotaFirmwareVersionDependenceService.list(firmwareId, versionIds);
            if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionDependencePos)) {
                versionDependenceIds = fotaFirmwareVersionDependencePos.stream().map(item -> item.getId()).collect(Collectors.toList());
            }

            List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.list(versionIds);
            if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
                versionPathIds = fotaFirmwareVersionPathPos.stream().map(item -> item.getId()).collect(Collectors.toList());
                List<Long> otherPkgIds = fotaFirmwareVersionPathPos.stream().map(item -> item.getFirmwarePkgId()).collect(Collectors.toList());
                if (MyCollectionUtil.isEmpty(pkgIds)) {
                    pkgIds = Lists.newArrayList();
                }
                pkgIds.addAll(otherPkgIds);
            }
        }

        fotaFirmwareDbService.getBaseMapper().deleteById(firmwareId);
        fotaFirmwareVersionDbService.deleteBatchIdsPhysical(versionIds);
        fotaFirmwareVersionDependenceService.deleteBatchIdsPhysical(versionDependenceIds);
        fotaFirmwareVersionPathDbService.deleteBatchIdsPhysical(versionPathIds);
        fotaFirmwarePkgService.deleteBatchIdsPhysical(pkgIds);

        //需要同步固件与零件绑定关系到车辆固件列表
        otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_FOTA_DEVICE_SYNC_MESSAGE, new FotaDeviceSyncMessage(fotaFirmwarePo.getTreeNodeId(), firmwareId, Enums.DeviceSyncActionTypeEnum.DELETE.getType()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFotaFirmwareVersion(FotaFirmwareVersionPo fotaFirmwareVersionPo) {
        //MyAssertUtil.state(Enums.ZeroOrOneEnum.contains(fotaFirmwareVersionPo.getIsForceFullUpdate()), "是否强制全量更新传值异常");
        //check if exist firmware with the same version
        FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(fotaFirmwareVersionPo.getFirmwareId());
        MyAssertUtil.notNull(fotaFirmwarePo, OTARespCodeEnum.FIRMWARE_NOT_EXIST_ERROR);

        CommonUtil.beanAttributeValueTrimPo(fotaFirmwareVersionPo);
        List<FotaFirmwareVersionPo> existFotaFirmwareVersionPos = fotaFirmwareVersionDbService.list(fotaFirmwareVersionPo.getProjectId(), fotaFirmwareVersionPo.getFirmwareId());

        //check and caculate version path
        List<FotaFirmwareVersionPo> selectedAppliedFotaFirmwareVersionPos = null;
        FotaFirmwareVersionPo latestFotaFirmwareVersionPo = null;
        boolean firstAdd = false;
        //exist history fotaFirmwareVersionPos
        if (MyCollectionUtil.isNotEmpty(existFotaFirmwareVersionPos)) {
            boolean exist = existFotaFirmwareVersionPos.stream().filter(item -> item.getFirmwareVersionNo().equals(fotaFirmwareVersionPo.getFirmwareVersionNo())).findFirst().isPresent();
            if (exist) {
                log.warn("exist firmware version,quit.fotaFirmwarePo={}", fotaFirmwareVersionPo.toString());
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_VERSION_NEW_EXIST_ERROR);
            }

            boolean existFullPkgIsNull = existFotaFirmwareVersionPos.stream().filter(item -> Objects.isNull(item.getFullPkgId())).findFirst().isPresent();
            if (existFullPkgIsNull) {
                log.warn("exist firmware version without pkg.fotaFirmwarePo={}", fotaFirmwareVersionPo.toString());
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_VERSION_PRE_WITHOUT_PACKAGE_ERROR);
            }

            latestFotaFirmwareVersionPo = existFotaFirmwareVersionPos.stream().sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).findFirst().orElse(null);
            if (Objects.nonNull(latestFotaFirmwareVersionPo)) {
                log.info("find latestFotaFirmwareVersionPo={}", latestFotaFirmwareVersionPo.toString());
            }

            //check applied firmwareVersion
            if (!StringUtils.isEmpty(fotaFirmwareVersionPo.getAppliedFirmwareVersion())) {
                //appliedVersoinIds =
                List<String> appliedFirmwareVersionIds = Arrays.asList(fotaFirmwareVersionPo.getAppliedFirmwareVersion().split(CommonConstant.SEPARATOR_SEMICOLON));
                selectedAppliedFotaFirmwareVersionPos = existFotaFirmwareVersionPos.stream().filter(item -> appliedFirmwareVersionIds.contains(Long.toString(item.getId()))).collect(Collectors.toList());
                if (MyCollectionUtil.isNotEmpty(selectedAppliedFotaFirmwareVersionPos)) {
                    if (selectedAppliedFotaFirmwareVersionPos.size() != appliedFirmwareVersionIds.size()) {
                        log.warn("selected applied fotaFirmwareVersionPos error.appliedFirmwareVersionIds={}", appliedFirmwareVersionIds.toString());
                        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_VERSION_APPLIED_SELECT_ERROR);
                    }
                }
            } else {
                selectedAppliedFotaFirmwareVersionPos = existFotaFirmwareVersionPos;
            }
        } else {
            //if not exist firmware version records
            firstAdd = true;
        }

        //check dependencies
        //checkDependence(fotaFirmwarePo, fotaFirmwareVersionPo);

        //TODO check version dependencies
        if (Objects.isNull(fotaFirmwareVersionPo.getFirmwareVersionName())) {
            fotaFirmwareVersionPo.setFirmwareVersionName(fotaFirmwareVersionPo.getFirmwareVersionNo());
        }
        if (Objects.nonNull(latestFotaFirmwareVersionPo)) {
            fotaFirmwareVersionPo.setParentVersionId(latestFotaFirmwareVersionPo.getId());
        }
        LocalDateTime dateTime = LocalDateTime.now();
        fotaFirmwareVersionPo.setId(IdWorker.getId());
        fotaFirmwareVersionPo.setCreateTime(dateTime);
        fotaFirmwareVersionPo.setUpdateTime(dateTime);

        Date date = MyDateUtil.localDateTimeToUDate(dateTime);
        fotaFirmwareVersionPo.setReleaseDt(date);
        fotaFirmwareVersionPo.setVersion(0);
        fotaFirmwareVersionPo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
        CommonUtil.wrapBasePo(fotaFirmwareVersionPo, true);
        fotaFirmwareVersionPo.setStatus(Enums.FirmwareVersionStatusEnum.NEW.getStatus());
        fotaFirmwareVersionPo.setPackageModel(Enums.FirmwareUpgradeModeTypeEnum.WHOLE.getType());
        //firmware_version_id_path:该字段暂时不设置
        fotaFirmwareVersionDbService.save(fotaFirmwareVersionPo);

        //如果是第一次添加版本，保存一条初始到目标版本一致的升级路径
        if (firstAdd) {
            if (MyCollectionUtil.isEmpty(selectedAppliedFotaFirmwareVersionPos)) {
                selectedAppliedFotaFirmwareVersionPos = Lists.newArrayList();
            }
            selectedAppliedFotaFirmwareVersionPos.add(fotaFirmwareVersionPo);
        }

        //初始化全量升级路径
        saveFotaFirmwareVersionPathBatch(fotaFirmwareVersionPo, selectedAppliedFotaFirmwareVersionPos, date);

        //依赖列表处理
        saveFotaFirmwareVersionDependenceBatch(fotaFirmwarePo, fotaFirmwareVersionPo, date);
    }

    /**
     * 批量添加路径记录
     *
     * @param fotaFirmwareVersionPo
     * @param selectedAppliedFotaFirmwareVersionPos
     * @param date
     */
    private void saveFotaFirmwareVersionPathBatch(final FotaFirmwareVersionPo fotaFirmwareVersionPo, final List<FotaFirmwareVersionPo> selectedAppliedFotaFirmwareVersionPos, final Date date) {
        if(MyCollectionUtil.isNotEmpty(selectedAppliedFotaFirmwareVersionPos)) {
            LocalDateTime now = MyDateUtil.uDateToLocalDateTime(date);
            List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = selectedAppliedFotaFirmwareVersionPos.stream().map(item -> {
                FotaFirmwareVersionPathPo fotaFirmwareVersionPathPo = new FotaFirmwareVersionPathPo();
                fotaFirmwareVersionPathPo.setId(IdWorker.getId());
                fotaFirmwareVersionPathPo.setProjectId(fotaFirmwareVersionPo.getProjectId());
                fotaFirmwareVersionPathPo.setUpgradePathType(Enums.FirmwareUpgradeModeTypeEnum.WHOLE.getType());
                fotaFirmwareVersionPathPo.setFirmwarePkgId(item.getFullPkgId());
                fotaFirmwareVersionPathPo.setStartFirmwareVerId(item.getId());
                fotaFirmwareVersionPathPo.setTargetFirmwareVerId(fotaFirmwareVersionPo.getId());
                fotaFirmwareVersionPathPo.setVersion(0);
                fotaFirmwareVersionPathPo.setPkgUpload(Enums.ZeroOrOneEnum.ZERO.getValue());
                CommonUtil.wrapBasePo(fotaFirmwareVersionPathPo, fotaFirmwareVersionPo.getCreateBy(), now,true);
                return fotaFirmwareVersionPathPo;
            }).collect(Collectors.toList());
            //添加升级路径数据记录
            fotaFirmwareVersionPathDbService.saveBatch(fotaFirmwareVersionPathPos);
        }else{
            log.warn("selectedAppliedFotaFirmwareVersionPos.size=0");
        }
    }

    /**
     * 批量添加依赖记录
     *
     * @param fotaFirmwarePo
     * @param fotaFirmwareVersionPo
     * @param date
     */
    private void saveFotaFirmwareVersionDependenceBatch(final FotaFirmwarePo fotaFirmwarePo, final FotaFirmwareVersionPo fotaFirmwareVersionPo, final Date date) {
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPo.getFotaFirmwareVersionDependenceDoList())) {
            LocalDateTime now = MyDateUtil.uDateToLocalDateTime(date);
            fotaFirmwareVersionPo.getFotaFirmwareVersionDependenceDoList().stream().forEach(item -> {
                item.setProjectId(fotaFirmwarePo.getProjectId());
                item.setFirmwareId(fotaFirmwareVersionPo.getFirmwareId());
                item.setFirmwareVersionId(fotaFirmwareVersionPo.getId());
                item.setDependFirmwareId(Long.parseLong(item.getDependFirmwareIdStr()));
                item.setDependFirmwareVersionId(Long.parseLong(item.getDependFirmwareVersionIdStr()));
                item.setVersion(0);
                item.setId(IdWorker.getId());
                CommonUtil.wrapBasePo(item, fotaFirmwareVersionPo.getCreateBy(), now,true);
            });
            //fotaFirmwareVersionDependenceService.saveBatch(fotaFirmwareVersionPo.getFotaFirmwareVersionDependenceDoList());
        }else{
            log.warn("fotaFirmwareVersionPo.getFotaFirmwareVersionDependenceDoList().size=0");
        }
    }

    /*private void checkDependence(final FotaFirmwarePo fotaFirmwarePo, final FotaFirmwareVersionPo fotaFirmwareVersionPo) {
        //check dependencies
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPo.getFotaFirmwareVersionDependenceDoList())) {
            //根据Id集合获取依赖的版本列表设备结点集合
            List<Long> dependencyFirmwareIds = fotaFirmwareVersionPo.getFotaFirmwareVersionDependenceDoList().stream().map(item -> Long.parseLong(item.getDependFirmwareIdStr())).collect(Collectors.toList());
            dependencyFirmwareIds.remove(fotaFirmwarePo.getId());
            MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(dependencyFirmwareIds), OTARespCodeEnum.FIRMWARE_DEPENDENCE_VERSIONS_NOT_ALLOWED_BELONGTO_SAME);

            List<FotaFirmwarePo> dependenceFirmwarePos = fotaFirmwareDbService.listByIds(dependencyFirmwareIds);
            dependenceFirmwarePos.forEach(item -> MyAssertUtil.state(item.getTreeNodeId().equals(fotaFirmwarePo.getTreeNodeId()), OTARespCodeEnum.FIRMWARE_DEPENDENCE_VERSIONS_NOT_ALLOWED_BELONGTO_OTHER_CAR));
        }
    }*/

    @Override
    public List<FotaFirmwareVersionPo> listFirmwareVersions(String projectId, long firmwareId) {
        FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(firmwareId);
        MyAssertUtil.notNull(fotaFirmwarePo, OTARespCodeEnum.FIRMWARE_NOT_EXIST_ERROR);
        List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareVersionDbService.list(projectId, firmwareId);
        wrapFotaFirmwareVersionPos(fotaFirmwarePo, fotaFirmwareVersionPos);
        fotaFirmwareVersionPos = fotaFirmwareVersionPos.stream().sorted(Comparator.comparing(FotaFirmwareVersionPo::getCreateTime).reversed()).collect(Collectors.toList());
        return MyCollectionUtil.safeList(fotaFirmwareVersionPos);
    }

    @Override
    public FotaFirmwareVersionPo getLatestFirmwareVersion(String projectId, long firmwareId) {
        List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareVersionDbService.list(projectId, firmwareId);
        return MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPos) ? fotaFirmwareVersionPos.stream().max(Comparator.comparing(BasePo::getCreateTime)).orElse(null) : null;
    }

    /**
     * 包装固件版本信息
     *
     * @param fotaFirmwareVersionPos
     */
    private void wrapFotaFirmwareVersionPos(final FotaFirmwarePo fotaFirmwarePo, final List<FotaFirmwareVersionPo> fotaFirmwareVersionPos) {
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPos)) {
            final AtomicBoolean patch = new AtomicBoolean(false);
            //if getPackageModel is patch type, add flag for version if pkg records exist
            if (Objects.nonNull(fotaFirmwarePo.getPackageModel()) && Enums.FirmwareUpgradeModeTypeEnum.PATCH.getType() == fotaFirmwarePo.getPackageModel()) {
                patch.set(true);
            }

            fotaFirmwareVersionPos.forEach(item -> {
                if (Objects.nonNull(fotaFirmwarePo)) {
                    item.setComponentName(fotaFirmwarePo.getComponentName());
                    item.setComponentCode(fotaFirmwarePo.getComponentCode());
                    item.setFirmwareName(fotaFirmwarePo.getFirmwareName());
                    item.setFirmwareCode(fotaFirmwarePo.getFirmwareCode());
                    item.setPackageModel(fotaFirmwarePo.getPackageModel());

                    //judge pkgupload finished
                    if (patch.get()) {
                        List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.list(/*fotaFirmwarePo.getProjectId(), */item.getId(), true);
                        item.setPkgUpload(MyCollectionUtil.isEmpty(fotaFirmwareVersionPathPos) ? 1 : 0);
                    } else {
                        item.setPkgUpload(Objects.nonNull(item.getFullPkgId()) && item.getFullPkgId() > 0L ? 1 : 0);
                    }

                    //wrap for dependence
                    try {
                        List<FotaFirmwareVersionDependencePo> fotaFirmwareVersionDependencePos = fotaFirmwareVersionDependenceService.list(fotaFirmwarePo.getId(), item.getId());
                        warpFotaFirmwareVersionDependencePos(fotaFirmwareVersionDependencePos);
                        item.setFotaFirmwareVersionDependenceDoList(fotaFirmwareVersionDependencePos);
                    } catch (Exception ex) {
                        log.error("wrap firmware version error.e={}", ex.getMessage(), ex);
                    }

                } else {
                    log.warn("firmware is null.id={}", item.getFirmwareId());
                }
            });
        }
        MyCollectionUtil.wrapCollection(fotaFirmwareVersionPos, FotaFirmwareServiceImpl::wrapFotaFirmwareVersionPoStr);
    }

    /**
     * 补充回写到前端的属性
     *
     * @param fotaFirmwareVersionDependencePos
     */
    private void warpFotaFirmwareVersionDependencePos(List<FotaFirmwareVersionDependencePo> fotaFirmwareVersionDependencePos) {
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionDependencePos)) {
            List<Long> depenFirmwareVersionIds = fotaFirmwareVersionDependencePos.stream().map(item1 -> item1.getDependFirmwareVersionId()).collect(Collectors.toList());
            List<FotaFirmwareVersionPo> depenFotaFirmwareVersionPos = fotaFirmwareVersionDbService.getBaseMapper().selectBatchIds(depenFirmwareVersionIds);
            Map<Long, FotaFirmwareVersionPo> depenFotaFirmwareVersionPosMap = depenFotaFirmwareVersionPos.stream().collect(Collectors.toMap(FotaFirmwareVersionPo::getId, Function.identity()));
            boolean depenFotaFirmwareVersionPosMapExist = !MyCollectionUtil.isEmpty(depenFotaFirmwareVersionPosMap);

            List<Long> depenFirmwareIds = fotaFirmwareVersionDependencePos.stream().map(item1 -> item1.getDependFirmwareId()).collect(Collectors.toList());
            List<FotaFirmwarePo> depenFotaFirmwarePos1 = fotaFirmwareDbService.listByIds(depenFirmwareIds);
            Map<Long, FotaFirmwarePo> depenFotaFirmwarePosMap = depenFotaFirmwarePos1.stream().collect(Collectors.toMap(FotaFirmwarePo::getId, Function.identity()));
            boolean depenFotaFirmwarePosMapExist = !MyCollectionUtil.isEmpty(depenFotaFirmwarePosMap);

            fotaFirmwareVersionDependencePos.stream().forEach(item1 -> {
                wrapFotaFirmwareVersionDependencePoStr(item1);

                if (depenFotaFirmwareVersionPosMapExist) {
                    FotaFirmwareVersionPo denpencFotaFirmwareVersionPo = depenFotaFirmwareVersionPosMap.get(item1.getDependFirmwareVersionId());
                    if (Objects.nonNull(denpencFotaFirmwareVersionPo)) {
                        item1.setDependFirmwareVersionNo(denpencFotaFirmwareVersionPo.getFirmwareVersionNo());
                    }
                }

                if (depenFotaFirmwarePosMapExist) {
                    FotaFirmwarePo depencFotaFirmwarePo = depenFotaFirmwarePosMap.get(item1.getDependFirmwareId());
                    if (Objects.nonNull(depencFotaFirmwarePo)) {
                        item1.setDependFirmwareCode(depencFotaFirmwarePo.getFirmwareCode());
                        item1.setDependFirmwareName(depencFotaFirmwarePo.getFirmwareName());
                        item1.setDependComponentCode(depencFotaFirmwarePo.getComponentCode());
                        item1.setDependComponentName(depencFotaFirmwarePo.getComponentName());
                    }
                }
            });
        }else{
            log.warn("fotaFirmwareVersionDependenceDos.size=0");
        }
    }

    @Override
    public List<FotaFirmwarePkgVo> listFirmwarePkgs(long firmwareId, long firmwareVersionId) {
        FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(firmwareId);
        MyAssertUtil.notNull(fotaFirmwarePo, OTARespCodeEnum.FIRMWARE_NOT_EXIST_ERROR);
        List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.list(/*fotaFirmwarePo.getProjectId(), */firmwareVersionId, true);
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
            List<Long> fotaFirmwareVersionDoIds = fotaFirmwareVersionPathPos.stream().map(item -> item.getStartFirmwareVerId()).collect(Collectors.toList());
            fotaFirmwareVersionDoIds.add(firmwareVersionId);
            List<FotaFirmwareVersionPo> fotaFirmwareVerisonDos = fotaFirmwareVersionDbService.listByIds(fotaFirmwareVersionDoIds);
            Map<Long, FotaFirmwareVersionPo> fotaFirmwareVerisonDoMap = fotaFirmwareVerisonDos.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

            List<FotaFirmwarePkgVo> allFotaFirmwarePkgResps = Lists.newArrayList();
            //支持分页应该对路径信息添加一个激活的状态，用于标识全量升级路径
            FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVerisonDoMap.get(firmwareVersionId);
            if (Objects.nonNull(fotaFirmwareVersionPo) && !((Long) 0L).equals(fotaFirmwareVersionPo.getFullPkgId())) {
                FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(fotaFirmwareVersionPo.getFullPkgId());
                if (Objects.nonNull(fotaFirmwarePkgPo)) {
                    FotaFirmwarePkgVo fotaFirmwarePkgResp = buildFotaFirmwarePkgResp(fotaFirmwarePkgPo, fotaFirmwarePo);
                    if (fotaFirmwarePkgPo.getBuildPkgStatus() != null) {
                        Enums.BuildStatusEnum byType = Enums.BuildStatusEnum.getByType(fotaFirmwarePkgPo.getBuildPkgStatus());
                        if (byType != null) {
                            fotaFirmwarePkgResp.setBuildPkgStatusStr(byType.getName());
                            fotaFirmwarePkgResp.setBuildPkgStatus(byType.getType());
                        }
                    }

                    if (fotaFirmwarePkgPo.getEncryptPkgStatus() != null) {
                        Enums.EncryptStatusEnum byType = Enums.EncryptStatusEnum.getByType(fotaFirmwarePkgPo.getEncryptPkgStatus());
                        if (byType != null) {
                            fotaFirmwarePkgResp.setEncryptPkgStatusStr(byType.getName());
                        }
                    }

                    if (Objects.nonNull(fotaFirmwarePkgPo.getBuildPkgCode())) {
                        Enums.CodeEnum byCode = Enums.CodeEnum.getByCode(fotaFirmwarePkgPo.getBuildPkgCode());
                        if (byCode != null) {
                            fotaFirmwarePkgResp.setStatusCode(Integer.toString(byCode.getCode()));
                            fotaFirmwarePkgResp.setStatusDesc(byCode.getComment());
                        }
                    }

                    fotaFirmwarePkgResp.setTargetFirmwareVersionNo(fotaFirmwareVersionPo.getFirmwareVersionNo());
                    if (!StringUtils.isEmpty(fotaFirmwareVersionPo.getAppliedFirmwareVersion())) {
                        String[] appliedFirmwareVersions = fotaFirmwareVersionPo.getAppliedFirmwareVersion().split(CommonConstant.SEPARATOR_SEMICOLON);
                        StringBuffer startFirmwareVersionNoSb = new StringBuffer();
                        for (String appliedFirmwareVersionId : appliedFirmwareVersions) {
                            startFirmwareVersionNoSb.append(fotaFirmwareVerisonDoMap.get(Long.parseLong(appliedFirmwareVersionId)).getFirmwareVersionNo() + CommonConstant.SEPARATOR_SEMICOLON);
                        }
                        String startFirmwareVersionNo = startFirmwareVersionNoSb.toString();
                        if (startFirmwareVersionNo.endsWith(CommonConstant.SEPARATOR_SEMICOLON)) {
                            startFirmwareVersionNo = startFirmwareVersionNo.substring(0, startFirmwareVersionNo.length() - 1);
                        }
                        fotaFirmwarePkgResp.setStartFirmwareVersionNo(startFirmwareVersionNo);
                    }
                    allFotaFirmwarePkgResps.add(fotaFirmwarePkgResp);
                }
            }

            //diff or patch
            List<FotaFirmwareVersionPathPo> nonWholeFotaFirmwareVersionPathPos = fotaFirmwareVersionPathPos.stream().filter(item -> Objects.nonNull(item.getFirmwarePkgId()) && !Enums.FirmwareUpgradeModeTypeEnum.isWholeType(item.getUpgradePathType())).collect(Collectors.toList());
            if (MyCollectionUtil.isNotEmpty(nonWholeFotaFirmwareVersionPathPos)) {
                List<Long> nonWholePkgIds = nonWholeFotaFirmwareVersionPathPos.stream().map(item -> item.getFirmwarePkgId()).collect(Collectors.toList());
                List<FotaFirmwarePkgPo> fotaFirmwarePkgPos = fotaFirmwarePkgDbService.listByIds(nonWholePkgIds);
                final Map<Long, FotaFirmwarePkgPo> fotaFirmwarePkgPoMap = fotaFirmwarePkgPos.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
                List<FotaFirmwarePkgVo> fotaFirmwarePkgResps = MyCollectionUtil.newCollection(nonWholeFotaFirmwareVersionPathPos, item -> {
                    FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgPoMap.get(item.getFirmwarePkgId());
                    FotaFirmwarePkgVo fotaFirmwarePkgResp = buildFotaFirmwarePkgResp(fotaFirmwarePkgPo, fotaFirmwarePo);
                    fotaFirmwarePkgResp.setTargetFirmwareVersionNo(fotaFirmwareVersionPo.getFirmwareVersionNo());
                    fotaFirmwarePkgResp.setStartFirmwareVersionNo(fotaFirmwareVerisonDoMap.get(item.getStartFirmwareVerId()).getFirmwareVersionNo());
                    if (Objects.nonNull(fotaFirmwarePkgPo.getBuildPkgStatus())) {
                        Enums.BuildStatusEnum byType = Enums.BuildStatusEnum.getByType(fotaFirmwarePkgPo.getBuildPkgStatus());
                        if (byType != null) {
                            fotaFirmwarePkgResp.setBuildPkgStatusStr(byType.getName());
                            fotaFirmwarePkgResp.setBuildPkgStatus(byType.getType());
                        }
                    }

                    if (Objects.nonNull(fotaFirmwarePkgPo.getEncryptPkgStatus())) {
                        Enums.EncryptStatusEnum byType = Enums.EncryptStatusEnum.getByType(fotaFirmwarePkgPo.getEncryptPkgStatus());
                        if (byType != null) {
                            fotaFirmwarePkgResp.setEncryptPkgStatusStr(byType.getName());
                        }
                    }
                    return fotaFirmwarePkgResp;
                });
                allFotaFirmwarePkgResps.addAll(fotaFirmwarePkgResps);
            }
            return allFotaFirmwarePkgResps;
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delFirmwarePkgs(long firmwareId, long firmwareVersionId) {
        FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(firmwareId);
        MyAssertUtil.notNull(fotaFirmwarePo, OTARespCodeEnum.DATA_NOT_FOUND);
        FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(firmwareVersionId);
        MyAssertUtil.notNull(fotaFirmwareVersionPo, OTARespCodeEnum.DATA_NOT_FOUND);

        //如果存在升级任务，则不允许删除升级包
        List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListDbService.list(firmwareId, firmwareVersionId);
        MyAssertUtil.isTrue(MyCollectionUtil.isEmpty(fotaStrategyFirmwareListPos), OTARespCodeEnum.FIRMWARE_PKGS_WITH_STRATEGY);

        List<Long> pkIds = Lists.newArrayList();
        List<Long> versionPathIds = Lists.newArrayList();
        LocalDateTime dateTime = LocalDateTime.now();
        if (Objects.nonNull(fotaFirmwareVersionPo.getFullPkgId()) && !((Long) 0L).equals(fotaFirmwareVersionPo.getFullPkgId())) {
            FotaFirmwareVersionPo newFotaFirmwareVersionPo = new FotaFirmwareVersionPo();
            Integer originVersion = fotaFirmwareVersionPo.getVersion();
            newFotaFirmwareVersionPo.setUpdateTime(dateTime);
            newFotaFirmwareVersionPo.setUpdateBy(fotaFirmwareVersionPo.getUpdateBy());
            newFotaFirmwareVersionPo.setFullPkgId(0L);
            newFotaFirmwareVersionPo.setId(fotaFirmwareVersionPo.getId());
            newFotaFirmwareVersionPo.setVersion(originVersion + 1);
            boolean update = fotaFirmwareVersionDbService.updateByIdWithVersion(newFotaFirmwareVersionPo, originVersion);
            if (!update) {
                log.warn("update error.newFotaFirmwareVersionDo={}", newFotaFirmwareVersionPo.toString());
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_DEL_ERROR);
            }

            pkIds.add(fotaFirmwareVersionPo.getFullPkgId());
        }

        List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.list(firmwareVersionId, false);
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
            for (FotaFirmwareVersionPathPo fotaFirmwareVersionPathPo : fotaFirmwareVersionPathPos) {
                //只删除差分包路径，全量包路径在创建版本时候完成，不删除
                if (!Enums.FirmwareUpgradeModeTypeEnum.isWholeType(fotaFirmwareVersionPathPo.getUpgradePathType())) {
                    versionPathIds.add(fotaFirmwareVersionPathPo.getId());
                }
                if (Objects.nonNull(fotaFirmwareVersionPathPo.getFirmwarePkgId())) {
                    pkIds.add(fotaFirmwareVersionPathPo.getFirmwarePkgId());
                }
            }
        }

        //删除可能的文件和上传记录
        List<FotaFirmwarePkgPo> fotaFirmwarePkgPos = fotaFirmwarePkgDbService.listByIds(pkIds);
        Set<Long> fileIds = Sets.newHashSet();
        if(MyCollectionUtil.isNotEmpty(fotaFirmwarePkgPos)){
            fileIds.addAll(fotaFirmwarePkgPos.stream().map(item -> item.getUploadFileId()).collect(Collectors.toList()));
            fileIds.addAll(fotaFirmwarePkgPos.stream().map(item -> item.getBuildUploadFileId()).collect(Collectors.toList()));
            fileIds.addAll(fotaFirmwarePkgPos.stream().map(item -> item.getEncryptUploadFileId()).collect(Collectors.toList()));
            fileIds.addAll(fotaFirmwarePkgPos.stream().map(item -> item.getReportUploadFileId()).collect(Collectors.toList()));
        }

        //物理删除
        fotaFirmwarePkgService.deleteBatchIdsPhysical(pkIds);
        //物理删除升级路径
        fotaFirmwareVersionPathDbService.deleteBatchIdsPhysical(versionPathIds);

        if(fileIds.size() > 0){
            fotaFileUploadRecordService.delByFileIds(fileIds);
        }
    }

    /**
     * @param fotaFirmwarePkgPo
     * @param fotaFirmwarePo
     * @return
     */
    private FotaFirmwarePkgVo buildFotaFirmwarePkgResp(final FotaFirmwarePkgPo fotaFirmwarePkgPo, final FotaFirmwarePo fotaFirmwarePo) {
        Enums.FirmwareUpgradeModeTypeEnum type = Enums.FirmwareUpgradeModeTypeEnum.getByType(fotaFirmwarePkgPo.getPkgType());
        FotaFirmwarePkgVo fotaFirmwarePkgResp = new FotaFirmwarePkgVo();
        fotaFirmwarePkgResp.setIdStr(Long.toString(fotaFirmwarePkgPo.getId()));
        fotaFirmwarePkgResp.setFirmwareName(fotaFirmwarePo.getFirmwareName());
        if (fotaFirmwarePkgPo.getOriginalPkgSize() != null) {
            fotaFirmwarePkgResp.setPkgFileSize(Long.toString(fotaFirmwarePkgPo.getOriginalPkgSize()));
        }
//        fotaFirmwarePkgResp.setPkgFileSize(Long.toString(fotaFirmwarePkgPo.getReleasePkgFileSize()));
        fotaFirmwarePkgResp.setPkgType(type.getType());
        fotaFirmwarePkgResp.setPkgTypeStr(type.getDesc());
        fotaFirmwarePkgResp.setPkgFileName(fotaFirmwarePkgPo.getPkgFileName());
        fotaFirmwarePkgResp.setStatus("完成");
        fotaFirmwarePkgResp.setUploadTime(MyDateUtil.localDateTimeToUDate(fotaFirmwarePkgPo.getCreateTime()));
        return fotaFirmwarePkgResp;
    }

    /**
     * 安装包数据添加逻辑--需要调用做包服务
     *
     * @param updatePkgReq
     * @return
     */
    private FotaFirmwarePkgPo addPkgWithBuild(final UpdatePkgReq updatePkgReq, Long firmwareId) {
        //参数验证
        MyAssertUtil.notEmpty(updatePkgReq.getFileId(), OTARespCodeEnum.FILE_ID_PARAM_ERROR);
        FotaFileUploadRecordPo fotaFileUploadRecordPo = fotaFileUploadRecordDbService.getById(Long.parseLong(updatePkgReq.getFileId()));
        MyAssertUtil.notNull(fotaFileUploadRecordPo, OTARespCodeEnum.FILE_UPLOAD_RECORD_NULL_ERROR);

        //添加检查逻辑：检查是否上传了之前版本的包文件
        List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareVersionDbService.list(null, firmwareId);
        MyAssertUtil.notNull(fotaFirmwareVersionPos, OTARespCodeEnum.FIRMWARE_VERSION_NOT_EXIST_ERROR);
        List<Long> fullPkgIds = MyCollectionUtil.newCollection(fotaFirmwareVersionPos, item -> item.getFullPkgId());
        if(MyCollectionUtil.isNotEmpty(fullPkgIds)) {
            List<FotaFirmwarePkgPo> fotaFirmwarePkgPos = fotaFirmwarePkgDbService.listByIds(fullPkgIds);
            List<Long> fileIds = MyCollectionUtil.newCollection(fotaFirmwarePkgPos, item -> item.getUploadFileId());
            if(MyCollectionUtil.isNotEmpty(fileIds)) {
                List<FotaFileUploadRecordPo> fotaFileUploadRecordPos = fotaFileUploadRecordDbService.listByIds(fileIds);

                if (MyCollectionUtil.isNotEmpty(fotaFileUploadRecordPos)) {
                    //如果该版本上传的全量包已经存在，可能会导致差分包制作失败。所以此处需要校验不能上传同一个全量升级包文件
                    fotaFileUploadRecordPos.stream().filter(item -> item.getFileSha().equals(fotaFileUploadRecordPo.getFileSha())).findFirst().ifPresent(item -> {
                        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_WITH_SAME_VERSION_ERROR);
                    });
                }
            }
        }

        boolean verify = updatePkgReq.getVerifyCode().equalsIgnoreCase(fotaFileUploadRecordPo.getFileSha());
        MyAssertUtil.state(verify, OTARespCodeEnum.FILE_VERIFY_ERROR);

        FotaFirmwarePkgPo entity = new FotaFirmwarePkgPo();
        entity.setEstimateFlashTime((long) updatePkgReq.getEstimateTime());
        entity.setPkgType(updatePkgReq.getPkgType());
        entity.setUploadFileId(fotaFileUploadRecordPo.getId());
        entity.setReportUploadFileId(Long.parseLong(updatePkgReq.getFileId()));
        entity.setPkgFileName(fotaFileUploadRecordPo.getFileName() + "_" + Enums.FirmwareUpgradePkgTypeEnum.WHOLE.getFileName());

        //差分包补充difScriptUrl字段
        if (Enums.FirmwareUpgradeModeTypeEnum.DIFF.getType() == updatePkgReq.getPkgType() && !StringUtils.isEmpty(updatePkgReq.getDifScriptUrl())) {
            entity.setOriginalDifScriptUrl(updatePkgReq.getDifScriptUrl());
        }

        //添加差分测试报告
        entity.setOriginalReportFilePath(updatePkgReq.getOriginalReportFilePath());

        CommonUtil.wrapBasePo(entity, updatePkgReq.getCreateBy(), true);
        entity.setVersion(0);

        entity.setBuildPkgStatus(Enums.BuildStatusEnum.TYPE_WAIT.getType());
        fotaFirmwarePkgDbService.save(entity);
        return entity;
    }

    /**
     * 安装包数据添加逻辑--不需要做包服务
     *
     * @param updatePkgReq
     * @return
     */
    private FotaFirmwarePkgPo addPkgWithNoBuild(final UpdatePkgReq updatePkgReq) {
        //参数验证
        FotaFileUploadRecordPo fotaFileUploadRecordPo = fotaFileUploadRecordDbService.getById(Long.parseLong(updatePkgReq.getFileId()));
        MyAssertUtil.notNull(fotaFileUploadRecordPo, OTARespCodeEnum.FILE_UPLOAD_RECORD_NULL_ERROR);

        //前端参数传递原因，difFileId定义不规范，difFileId=测试报告上传记录Id
        FotaFileUploadRecordPo reportFotaFileUploadRecordPo = fotaFileUploadRecordDbService.getById(Long.parseLong(updatePkgReq.getDifFileId()));
        MyAssertUtil.notNull(reportFotaFileUploadRecordPo, OTARespCodeEnum.FILE_UPLOAD_RECORD_NULL_ERROR);

        boolean verify = updatePkgReq.getVerifyCode().equalsIgnoreCase(fotaFileUploadRecordPo.getFileSha());
        MyAssertUtil.isTrue(verify, OTARespCodeEnum.FILE_VERIFY_ERROR);

        FotaFirmwarePkgPo entity = new FotaFirmwarePkgPo();
        entity.setEstimateFlashTime((long) updatePkgReq.getEstimateTime());
        entity.setPkgType(updatePkgReq.getPkgType());
        entity.setUploadFileId(fotaFileUploadRecordPo.getId());
        entity.setReportUploadFileId(reportFotaFileUploadRecordPo.getId());
        entity.setBuildUploadFileId(fotaFileUploadRecordPo.getId());
        entity.setPkgFileName(fotaFileUploadRecordPo.getFileName());
        entity.setOriginalPkgSize(fotaFileUploadRecordPo.getFileSize());
        entity.setOriginalPkgShaCode(fotaFileUploadRecordPo.getFileSha());
        entity.setOriginalPkgFilePath(fotaFileUploadRecordPo.getFileAddress());


        entity.setOriginalReportFilePath(reportFotaFileUploadRecordPo.getFileAddress());

        //TODO release handle will do
        entity.setReleasePkgFilePath(fotaFileUploadRecordPo.getFileAddress());
        entity.setReleasePkgFileDownloadUrl(fotaFileUploadRecordPo.getFileAddress());
        entity.setReleasePkgFileSize(fotaFileUploadRecordPo.getFileSize());
        //发布到CDN网络的包的地址
        entity.setReleasePkgCdnDownloadUrl(fotaFileUploadRecordPo.getFileAddress());


        CommonUtil.wrapBasePo(entity, updatePkgReq.getCreateBy(), true);
        entity.setVersion(0);

        //整包升级模式不需要UA差分引擎支持，不需要进行差分包制作处理
        entity.setBuildPkgStatus(Enums.BuildStatusEnum.TYPE_FINISH.getType());
        entity.setBuildPkgTime(new Date());
        fotaFirmwarePkgDbService.save(entity);

        log.info("整包升级模式 不做差分处理，直接触发升级包加密操作... firmwarePkgId|{}", entity.getId());
        myOtaEventKit.triggerPackageBuildCompletedEvent(entity.getId());
        return entity;
    }

    /**
     * 添加包之前做一些判断和准备
     *
     * @param updatePkgReq
     */
    private void checkForAddPkg(final UpdatePkgReq updatePkgReq) {
        FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(Long.parseLong(updatePkgReq.getFirmwareVersionIdStr()));
        MyAssertUtil.notNull(fotaFirmwareVersionPo, OTARespCodeEnum.FIRMWARE_VERSION_NOT_EXIST_ERROR);

        if (Objects.nonNull(fotaFirmwareVersionPo.getFullPkgId()) && fotaFirmwareVersionPo.getFullPkgId() > 0L) {
            MyAssertUtil.notNull(fotaFirmwareVersionPo, OTARespCodeEnum.FIRMWARE_VERSION_PKG_UPLOAD_FINISHED);
        }

        CommonUtil.beanAttributeValueTrimPo(updatePkgReq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWholePkg(UpdatePkgReq updatePkgReq) {
        checkForAddPkg(updatePkgReq);

        FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(updatePkgReq.getFirmwareVersionIdStr());
        MyAssertUtil.notNull(fotaFirmwareVersionPo, OTARespCodeEnum.FIRMWARE_VERSION_NOT_EXIST_ERROR);

        LocalDateTime dateTime = LocalDateTime.now();
        updatePkgReq.setPkgType(Enums.FirmwareUpgradeModeTypeEnum.WHOLE.getType());
        FotaFirmwarePkgPo entity = addPkgWithNoBuild(updatePkgReq);
        //update fota_firmware_version record
        FotaFirmwareVersionPo updateItem = new FotaFirmwareVersionPo();
        updateItem.setId(fotaFirmwareVersionPo.getId());
        updateItem.setVersion(fotaFirmwareVersionPo.getVersion() + 1);
        updateItem.setFullPkgId(entity.getId());
        updateItem.setUpdateBy(updatePkgReq.getUpdateBy());
        updateItem.setUpdateTime(dateTime);
        updateItem.setPackageModel(Enums.BuildEnum.TYPE_FULL.getType());
        fotaFirmwareVersionDbService.saveOrUpdate(updateItem);

        Long versionId = Long.parseLong(updatePkgReq.getFirmwareVersionIdStr());//目标

        List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.list(/*firmwareVersionDo.getProjectId(),*/ versionId, false);
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
            //active fotaFirmwareVersionPath(set pkgUpload=1)
            List<FotaFirmwareVersionPathPo> newFotaFirmwareVersionPathPos = fotaFirmwareVersionPathPos.stream().map(item -> {
                FotaFirmwareVersionPathPo newItem1 = new FotaFirmwareVersionPathPo();
                newItem1.setId(item.getId());
                newItem1.setUpdateBy(updatePkgReq.getUpdateBy());
                newItem1.setUpdateTime(dateTime);
                newItem1.setPkgUpload(Enums.ZeroOrOneEnum.ONE.getValue());
                newItem1.setVersion(item.getVersion() + 1);
                //补充冗余所有类型的升级路径上的pkgId属性
                newItem1.setFirmwarePkgId(entity.getId());
                return newItem1;
            }).collect(Collectors.toList());
            fotaFirmwareVersionPathDbService.updateBatchById(newFotaFirmwareVersionPathPos);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDifPkg(UpdatePkgReq updatePkgReq) {
        //生成差分模式全量包相关记录
        BuildWholePkgInfo buildWholePkgInfo = addDifWholePkg(updatePkgReq);

        List<BuildPackageDto> buildPackageDtos = Lists.newArrayList();
        buildPackageDtos.add(buildWholePkgInfo.getBuildPackageDto());
        /*
          差分模式下，需要同时生成整包和差分包
         */
        if (MyCollectionUtil.isNotEmpty(updatePkgReq.getTargetFirmwareVersonIds())&&!StringUtils.isEmpty(updatePkgReq.getTargetFirmwareVersonIds().get(0))) {
            List<FotaFirmwareVersionPo> existFotaFirmwareVersionPos = fotaFirmwareVersionDbService.list(/*endFotaFirmwareVersionDo.getProjectId()*/null,         // 源 --> 目 比较hash
                    buildWholePkgInfo.getFotaFirmwareVersionPo().getFirmwareId());

            final Date date = new Date();

            //获取满足条件的版本列表
            Long startFirmwareVersionId = Long.parseLong(updatePkgReq.getTargetFirmwareVersonIds().get(0));
            FotaFirmwareVersionPo startFotaFirmwareVersionPo = existFotaFirmwareVersionPos.stream().filter(item -> item.getId().equals(startFirmwareVersionId)).findFirst().orElse(null);
            MyAssertUtil.notNull(startFotaFirmwareVersionPo, OTARespCodeEnum.FIRMWARE_VERSION_NOT_EXIST_ERROR);
            existFotaFirmwareVersionPos.stream().filter(item -> item.getCreateTime().compareTo(startFotaFirmwareVersionPo.getCreateTime()) >= 0 && item.getCreateTime().compareTo(buildWholePkgInfo.getFotaFirmwareVersionPo().getCreateTime()) <= 0 && !item.getId().equals(buildWholePkgInfo.getFotaFirmwareVersionPo().getId())).forEach(item -> {
                //TODO
                FotaFirmwarePkgPo sourceFirmwarePkgDo = fotaFirmwarePkgDbService.getById(item.getFullPkgId());
                MyAssertUtil.notNull(sourceFirmwarePkgDo, OTARespCodeEnum.FIRMWARE_PKGS_NOT_EXISTS);
                FotaFileUploadRecordPo sourceFileUploadRcdDo = fotaFileUploadRecordDbService.getById(sourceFirmwarePkgDo.getUploadFileId());
                MyAssertUtil.notNull(sourceFileUploadRcdDo, OTARespCodeEnum.FIRMWARE_PKGS_UPLOAD_NOT_EXISTS);

                FotaFirmwarePkgPo deltaEntity = new FotaFirmwarePkgPo();
                //补充将原始上传的文件id设置到pkg对应记录中
                deltaEntity.setUploadFileId(buildWholePkgInfo.getFotaFileUploadRecordPo().getId());
                deltaEntity.setReportUploadFileId(Long.parseLong(updatePkgReq.getDifFileId()));
                deltaEntity.setEstimateFlashTime((long) updatePkgReq.getEstimateTime());
                deltaEntity.setPkgFileName(buildWholePkgInfo.getFotaFileUploadRecordPo().getFileName()+ "_" + item.getFirmwareVersionNo() + "-" + buildWholePkgInfo.getFotaFirmwareVersionPo().getFirmwareVersionNo() + "_" +Enums.FirmwareUpgradePkgTypeEnum.DIFF.getFileName());
                deltaEntity.setPkgType(Enums.FirmwareUpgradeModeTypeEnum.DIFF.getType());
                deltaEntity.setVersion(0);
                CommonUtil.wrapBasePo(deltaEntity, updatePkgReq.getCreateBy(), true);
                deltaEntity.setBuildPkgStatus(Enums.BuildStatusEnum.TYPE_WAIT.getType());
                fotaFirmwarePkgDbService.save(deltaEntity);
                //add versionPath
                addFotaFirmwareVersionPathPo(buildWholePkgInfo.getFotaFirmwareVersionPo().getProjectId(), item.getId(), buildWholePkgInfo.getFotaFirmwareVersionPo().getId(), deltaEntity.getId(), Enums.FirmwareUpgradeModeTypeEnum.DIFF, updatePkgReq.getCreateBy(), date);

                // 添加制作差分包
                BuildPackageDto difBuildPackageDto = createTask(Enums.FirmwareUpgradePkgTypeEnum.DIFF, deltaEntity.getId(), sourceFileUploadRcdDo.getFileKey(), buildWholePkgInfo.getFotaFileUploadRecordPo().getFileKey(), sourceFileUploadRcdDo.getFileSha(), buildWholePkgInfo.getFotaFileUploadRecordPo().getFileSha());
                buildPackageDtos.add(difBuildPackageDto);
            });
        }

        //统一处理做包调用
        if(MyCollectionUtil.isNotEmpty(buildPackageDtos)){
            buildPackageDtos.forEach(item ->{
                log.info("发起做包调用---start---");
                log.info("参数：做包类型={}, firmwareVersionId={}, item={}", item.getType(), updatePkgReq.getFirmwareVersionIdStr(), item);
                buildPackageService.createTask(item);
                log.info("发起做包调用---end---");
            });
        }
    }

    /**
     * 调用做包服务
     * @param pkgId
     * @param sourceFileKey
     * @param sourceFileSha
     * @param targetFileKey
     * @param targetFileSha
     */
    private BuildPackageDto createTask(Enums.FirmwareUpgradePkgTypeEnum firmwareUpgradePkgTypeEnum, long pkgId, String sourceFileKey, String targetFileKey, String sourceFileSha, String targetFileSha){
        BuildPackageDto packageDto = new BuildPackageDto();
        packageDto.setPackageId(pkgId);
        packageDto.setType(firmwareUpgradePkgTypeEnum.getFirmwareUpgradeModeTypeEnum().getType());
        //生成包的文件名称
        packageDto.setPackageName(firmwareUpgradePkgTypeEnum.getFileName());
        packageDto.setSourceVersionFileKey(sourceFileKey);
        packageDto.setTargetVersionFileKey(targetFileKey);
        packageDto.setSourceHash(sourceFileSha);
        packageDto.setTargetHash(targetFileSha);
        log.info("制作包参数:buildPackageDto={}", packageDto);
        return packageDto;
    }

    /**
     * 添加差分升级模式下的全量包
     *
     * @param updatePkgReq
     * @return
     */
    private BuildWholePkgInfo addDifWholePkg(UpdatePkgReq updatePkgReq) {
        //检验数据是否存在
        String versionId = updatePkgReq.getFirmwareVersionIdStr();
        FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(versionId);
        MyAssertUtil.notNull(fotaFirmwareVersionPo, OTARespCodeEnum.FIRMWARE_VERSION_NOT_EXIST_ERROR);

        checkForAddPkg(updatePkgReq);

        LocalDateTime dateTime = LocalDateTime.now();
        updatePkgReq.setPkgType(Enums.FirmwareUpgradeModeTypeEnum.WHOLE.getType());
        FotaFirmwarePkgPo entity = addPkgWithBuild(updatePkgReq, fotaFirmwareVersionPo.getFirmwareId());
        //update fota_firmware_version record
        FotaFirmwareVersionPo updateItem = new FotaFirmwareVersionPo();
        updateItem.setId(fotaFirmwareVersionPo.getId());
        updateItem.setVersion(fotaFirmwareVersionPo.getVersion() + 1);
        updateItem.setFullPkgId(entity.getId());
        updateItem.setUpdateTime(dateTime);
        updateItem.setUpdateBy(updatePkgReq.getUpdateBy());
        //TODO
        updateItem.setPackageModel(Enums.BuildEnum.TYPE_FULL.getType());
        fotaFirmwareVersionDbService.saveOrUpdate(updateItem);


        List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.list(/*fotaFirmwareVersionDo.getProjectId(),*/ Long.parseLong(updatePkgReq.getFirmwareVersionIdStr()), false);
        if (MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
            List<FotaFirmwareVersionPathPo> newFotaFirmwareVersionPathPos = fotaFirmwareVersionPathPos.stream().map(item -> {
                FotaFirmwareVersionPathPo newItem1 = new FotaFirmwareVersionPathPo();
                newItem1.setId(item.getId());
                newItem1.setUpdateBy(updatePkgReq.getUpdateBy());
                newItem1.setUpdateTime(dateTime);
                newItem1.setPkgUpload(Enums.ZeroOrOneEnum.ONE.getValue());
                newItem1.setVersion(item.getVersion() + 1);
                newItem1.setFirmwarePkgId(entity.getId());
                return newItem1;
            }).collect(Collectors.toList());
            log.info("newFotaFirmwareVersionPathPos.size={}", newFotaFirmwareVersionPathPos.size());
            fotaFirmwareVersionPathDbService.updateBatchById(newFotaFirmwareVersionPathPos);
        }

        //重新获取获取差分模式做整包的相关参数
        FotaFirmwareVersionPo newFotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(versionId);
        FotaFirmwarePkgPo firmwarePkgDo = fotaFirmwarePkgDbService.getById(entity.getId());
        FotaFileUploadRecordPo fileUploadRcdDo = fotaFileUploadRecordDbService.getById(firmwarePkgDo.getUploadFileId());
        //发送做包请求
        BuildPackageDto buildPackageDto = createTask(Enums.FirmwareUpgradePkgTypeEnum.WHOLE, firmwarePkgDo.getId(), fileUploadRcdDo.getFileKey(), fileUploadRcdDo.getFileKey(), fileUploadRcdDo.getFileSha(), fileUploadRcdDo.getFileSha());
        return BuildWholePkgInfo.builder().fotaFirmwareVersionPo(newFotaFirmwareVersionPo).fotaFirmwarePkgPo(firmwarePkgDo).fotaFileUploadRecordPo(fileUploadRcdDo).buildPackageDto(buildPackageDto).build();
    }

    /**
     * 添加升级包临时中间结果类
     */
    @Data
    @Builder
    static class BuildWholePkgInfo{
        private FotaFirmwareVersionPo fotaFirmwareVersionPo;
        private FotaFirmwarePkgPo fotaFirmwarePkgPo;
        private FotaFileUploadRecordPo fotaFileUploadRecordPo;

        private BuildPackageDto buildPackageDto;
    }

    /**
     * 添加升级路径
     *
     * @param projectId
     * @param startVersionId
     * @param targetVersionId
     * @param pkgId
     * @param upgradePathTypeEnum
     * @param createBy
     * @param date
     */
    private void addFotaFirmwareVersionPathPo(String projectId, Long startVersionId, Long targetVersionId, Long pkgId, Enums.FirmwareUpgradeModeTypeEnum upgradePathTypeEnum, String createBy, Date date) {
        FotaFirmwareVersionPathPo fotaFirmwareVersionPathPo = new FotaFirmwareVersionPathPo();
        fotaFirmwareVersionPathPo.setProjectId(projectId);
        fotaFirmwareVersionPathPo.setStartFirmwareVerId(startVersionId);
        fotaFirmwareVersionPathPo.setTargetFirmwareVerId(targetVersionId);
        fotaFirmwareVersionPathPo.setFirmwarePkgId(pkgId);
        fotaFirmwareVersionPathPo.setPkgUpload(Enums.ZeroOrOneEnum.ONE.getValue());
        fotaFirmwareVersionPathPo.setUpgradePathType(upgradePathTypeEnum.getType());
        fotaFirmwareVersionPathPo.setVersion(0);
        CommonUtil.wrapBasePo(fotaFirmwareVersionPathPo, createBy, true);
        fotaFirmwareVersionPathDbService.save(fotaFirmwareVersionPathPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPatchPkg(UpdatePkgReq updatePkgReq) {
        if (CollectionUtils.isEmpty(updatePkgReq.getTargetFirmwareVersonIds()) || updatePkgReq.getTargetFirmwareVersonIds().size() > 1) {
            log.error("add Patch pkg startVersionIds error.");
            return;
        }
    }

    @Override
    public List<FotaFirmwarePo> listFirmwarePos(Long treeNodeId) {

        List<FotaFirmwarePo> fotaFirmwarePos = fotaFirmwareDbService.listByTreeNodeId(treeNodeId);
        List<FotaFirmwarePo> list = MyCollectionUtil.newSortedList(fotaFirmwarePos, (o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()), item -> wrapFotaFirmwarePo(item));
        return list;
    }

    @Override
    public void addUpgradePkg(UpgradePkgReq req) {
        FotaFirmwarePkgPo existEntity = fotaFirmwarePkgDbService.getById(req.getPackageId());
        if(Objects.isNull(existEntity)) {
            log.warn("更新包Id不存在,不能更新.pkgId={}", req.getPackageId());
            return;
        }

        Enums.CodeEnum codeEnum = Enums.CodeEnum.getByCode(req.getCode());

        // 添加上传记录
        if (Enums.CodeEnum.CREATE_SUCCESS.equals(codeEnum)) {
            //补充做包后的文件上传记录
            FotaFileUploadRecordPo fileUploadRecordDo = new FotaFileUploadRecordPo();
            fileUploadRecordDo.setId(IdWorker.getId());
            fileUploadRecordDo.setFileSize(req.getFileSize());
            fileUploadRecordDo.setFileName(req.getFileName());
            fileUploadRecordDo.setFileAddress(req.getFilePath());
            fileUploadRecordDo.setFileKey(req.getFileKey());
            fileUploadRecordDo.setFileType(req.getType());
            fileUploadRecordDo.setFileSha(req.getFileHash());
            Date date = new Date();
            fileUploadRecordDo.setUploadBeginDt(date);
            fileUploadRecordDo.setUploadEndDt(date);
            fileUploadRecordDo.setStatus("0");
            fileUploadRecordDo.setVersion(0);
            CommonUtil.wrapBasePo(fileUploadRecordDo, CommonConstant.USER_ID_SYSTEM, true);
            fotaFileUploadRecordDbService.saveOrUpdate(fileUploadRecordDo);

            FotaFirmwarePkgPo entity = new FotaFirmwarePkgPo();
            entity.setId(req.getPackageId());

            if (!StringUtils.isEmpty(req.getType())) {
                Enums.BuildEnum byType = Enums.BuildEnum.getByType(req.getType());
                if (Objects.nonNull(byType)) {
                    //设置构建完成后文件包上传记录Id
                    entity.setBuildUploadFileId(fileUploadRecordDo.getId());
                    entity.setOriginalPkgSize(req.getFileSize());
                    entity.setOriginalPkgShaCode(req.getFileHash());
                    entity.setOriginalPkgFilePath(req.getFilePath());
                    entity.setReleasePkgFileSize(req.getFileSize());
                    entity.setReleasePkgFilePath(req.getFilePath());
                    entity.setReleasePkgShaCode(req.getFileHash());
                    entity.setReleasePkgFileDownloadUrl(req.getFilePath());
                    entity.setReleasePkgCdnDownloadUrl(req.getFilePath());
                }
            }
            entity.setBuildPkgStatus(Enums.BuildStatusEnum.TYPE_FINISH.getType());
            entity.setBuildPkgCode(codeEnum.getCode());
            entity.setBuildPkgTime(new Date());
            CommonUtil.wrapBasePo4Update(entity, null, null);
            fotaFirmwarePkgDbService.saveOrUpdate(entity);

            // 升级包制作成功后开始触发升级包加密操作
            myOtaEventKit.triggerPackageBuildCompletedEvent(entity.getId());

        } else/* if (Enums.CodeEnum.PACKAGE_CREATING.getCode() == req.getCode() || Enums.CodeEnum.PACKAGE_FAIL.getCode() == req.getCode())*/ {
            boolean isCreating = Enums.CodeEnum.PACKAGE_CREATING.equals(codeEnum);
            FotaFirmwarePkgPo entity = new FotaFirmwarePkgPo();
            entity.setId(req.getPackageId());
            entity.setPkgType(req.getType());
            entity.setBuildPkgStatus(isCreating ? Enums.BuildStatusEnum.TYPE_BUILDING.getType() : Enums.BuildStatusEnum.TYPE_FAIL.getType());
            entity.setBuildPkgCode(codeEnum.getCode());
            CommonUtil.wrapBasePo4Update(entity, null, null);
            fotaFirmwarePkgDbService.saveOrUpdate(entity);
        }
    }

    /**
     * 添加前端需要的附加属性--包装FotaFirmwarePo
     *
     * @param item
     */
    private void wrapFotaFirmwarePo(final FotaFirmwarePo item) {
        if (Objects.nonNull(item)) {
            item.setIdStr(MyCollectionUtil.long2Str(item.getId()));
            item.setTreeNodeIdStr(MyCollectionUtil.long2Str(item.getTreeNodeId()));
            item.setInstScriptUrl(MyBusinessUtil.relaceDownloadUrl(minIoConfig.getTboxPkgDownloadDomain(), item.getInstScriptUrl()));
        }
    }

    /**
     * 添加前端需要的附加属性--包装FotaFirmwareVersionDo
     *
     * @param item
     */
    private static void wrapFotaFirmwareVersionPoStr(final FotaFirmwareVersionPo item) {
        if (Objects.nonNull(item)) {
            item.setIdStr(MyCollectionUtil.long2Str(item.getId()));
            item.setFirmwareIdStr(MyCollectionUtil.long2Str(item.getFirmwareId()));
        }
    }

    /**
     * 添加前端需要的附加属性--包装fotaFirmwareVersionDependenceDo
     *
     * @param item
     */
    private static void wrapFotaFirmwareVersionDependencePoStr(final FotaFirmwareVersionDependencePo item) {
        if (Objects.nonNull(item)) {
            item.setIdStr(MyCollectionUtil.long2Str(item.getId()));
            item.setFirmwareIdStr(MyCollectionUtil.long2Str(item.getFirmwareId()));
            item.setFirmwareVersionIdStr(MyCollectionUtil.long2Str(item.getFirmwareVersionId()));
            item.setDependFirmwareIdStr(MyCollectionUtil.long2Str(item.getDependFirmwareId()));
            item.setDependFirmwareVersionIdStr(MyCollectionUtil.long2Str(item.getDependFirmwareVersionId()));
        }
    }

    @Override
    public boolean isFirmwarePkgBuildedSuccess(Long firmwareId, Long firmwareVersionId) {
        List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.listByTargetVersionId(firmwareVersionId);

        List<Long> pkgIds = fotaFirmwareVersionPathPos.stream().map(item1 -> item1.getFirmwarePkgId()).collect(Collectors.toList());
        AtomicBoolean fail = new AtomicBoolean(true);
        if(MyCollectionUtil.isNotEmpty(pkgIds)){
            /*boolean isEncryptOn = Objects.nonNull(encryptSwitch) && encryptSwitch.intValue() == 1;*/
            pkgIds.forEach(item ->{
                FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(item);
                //暂时只判断升级包制作是否完成的标识为是否最终下载地址是否为空
                boolean buildFail = Objects.isNull(fotaFirmwarePkgPo) || Objects.isNull(fotaFirmwarePkgPo.getBuildPkgStatus()) || Enums.BuildStatusEnum.TYPE_FINISH.getType() != fotaFirmwarePkgPo.getBuildPkgStatus() || StringUtils.isEmpty(fotaFirmwarePkgPo.getReleasePkgFileDownloadUrl());
                if( buildFail){
                    log.warn("升级包制作失败/未完成.firmwareId={}, firmwareVersionId={}", firmwareId, firmwareVersionId);
                    fail.compareAndSet(true, false);
                }
                /*boolean encryptFail = isEncryptOn && (Objects.isNull(fotaFirmwarePkgPo.getBuildPkgStatus()) || Enums.BuildStatusEnum.TYPE_FINISH.getType() != fotaFirmwarePkgPo.getEncryptPkgStatus());
                if(encryptFail){
                    log.warn("升级包加密未完成.firmwareId={}, firmwareVersionId={}", firmwareId, firmwareVersionId);
                    fail.compareAndSet(true, false);
                }*/
            });
        }else{
            log.warn("升级路径对应的升级包不存在，请检查.firmwareId={}, firmwareVersionId={}", firmwareId, firmwareVersionId);
            fail.set(false);
        }
        return fail.get();
    }
}
