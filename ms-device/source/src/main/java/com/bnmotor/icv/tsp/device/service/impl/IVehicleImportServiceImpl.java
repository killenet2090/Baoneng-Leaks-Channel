package com.bnmotor.icv.tsp.device.service.impl;

import com.alibaba.excel.read.metadata.ReadSheet;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.UUIDUtil;
import com.bnmotor.icv.adam.data.redis.StringRedisProvider;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.*;
import com.bnmotor.icv.tsp.device.common.enums.dataaysn.CheckStatus;
import com.bnmotor.icv.tsp.device.common.enums.dataaysn.ControlType;
import com.bnmotor.icv.tsp.device.common.enums.dataaysn.ImportFailedReason;
import com.bnmotor.icv.tsp.device.common.excel.DefaultSaxReadListener;
import com.bnmotor.icv.tsp.device.common.excel.ExcelProcessor;
import com.bnmotor.icv.tsp.device.common.excel.AbstractSaxReadListener;
import com.bnmotor.icv.tsp.device.mapper.*;
import com.bnmotor.icv.tsp.device.model.entity.*;
import com.bnmotor.icv.tsp.device.model.request.excel.ExcelDeviceDto;
import com.bnmotor.icv.tsp.device.model.request.excel.ExcelVehicleDto;

import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleDto;
import com.bnmotor.icv.tsp.device.model.response.device.CacheDeviceModelInfo;
import com.bnmotor.icv.tsp.device.model.response.feign.CmpSimVo;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.VehicleDeviceVo;
import com.bnmotor.icv.tsp.device.model.response.vehImport.ExportFailedVo;
import com.bnmotor.icv.tsp.device.model.response.vehImport.ExportVehicleVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.OrgLocalCacheVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.ProgressVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleVo;
import com.bnmotor.icv.tsp.device.service.*;
import com.bnmotor.icv.tsp.device.service.feign.CmpFeignService;
import com.bnmotor.icv.tsp.device.util.IdGenerator;
import com.bnmotor.icv.tsp.device.util.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bnmotor.icv.tsp.device.common.enums.dataaysn.ControlType.PAUSE_CHECK;

/**
 * @ClassName: IVehicleImportServiceImpl
 * @Description: ?????????????????????
 * @author: zhangwei2
 * @date: 2020/11/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. ?????????????????????????????????????????????????????????????????????????????????????????????????????????
 */
@Service
@Slf4j
public class IVehicleImportServiceImpl implements IVehicleImportService {
    /**
     * ???????????????????????????????????????
     */
    private static final Integer LIMIT = 1000;
    /**
     * ???????????????
     */
    private final ExecutorService threadPool = Executors.newFixedThreadPool(2);
    @Resource
    private Enviroment env;
    @Value("${excel.vehicle.batchSize}")
    private Integer vehBatchSize;
    @Value("${excel.vehDevice.batchSize}")
    private Integer vehDeviceBatchSize;

    @Resource
    private VehicleDeviceImportRecordMapper devRecordMapper;
    @Resource
    private VehicleImportRecordMapper vehRecordMapper;
    @Resource
    private IVehicleService vehicleService;
    @Resource
    private IDeviceService deviceService;
    @Resource
    private IVehicleDeviceService vehDeviceService;
    @Resource
    private IOrgRelationService relationService;
    @Resource
    private CmpFeignService cmpFeignService;
    @Resource
    private StringRedisProvider redisClient;
    @Resource
    private VehLocalCache vehLocalCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVehicleInfo(VehicleDto dto) {
        List<VehiclePo> vehiclePoList = new ArrayList<>();
        List<VehicleDevicePo> vehicleDevicePoList = new ArrayList<>();
        List<DevicePo> devicePoList = new ArrayList<>();

        String vin = dto.getVin();
        VehicleVo vehicleVo = vehicleService.getVehicleVo(vin);
        if (vehicleVo != null) {
            log.warn("?????????????????????????????????????????????{}", vin);
            throw new AdamException(RespCode.REQUEST_METHOD_NOT_SUPPORTED, "????????????????????????" + vin);
        }

        OrgLocalCacheVo cacheVo = vehLocalCache.getVehOrgFromCache(dto.getVehModelName(), dto.getYearStyleName(), dto.getConfigName());
        if (cacheVo == null) {
            log.warn("?????????????????????????????????????????????{}", vin);
            throw new AdamException(RespCode.USER_INVALID_INPUT, "????????????????????????" + vin);
        }

        VehiclePo vehiclePo = new VehiclePo();
        vehiclePoList.add(vehiclePo);
        List<VehDeviceDto> deviceInfoList = Optional.ofNullable(dto.getDeviceInfoList()).orElse(new ArrayList<>());
        if (!org.springframework.util.CollectionUtils.isEmpty(deviceInfoList)) {
            deviceInfoList.forEach(vehDeviceDto -> {
                VehicleDevicePo vehicleDevicePo = getVehicleDevicePo(dto, vehDeviceDto);
                vehicleDevicePoList.add(vehicleDevicePo);
                DevicePo devicePo = getDevicePo(vehDeviceDto);
                devicePoList.add(devicePo);
            });
        }

        if (!org.springframework.util.CollectionUtils.isEmpty(vehiclePoList)) {
            vehicleService.saveVehicles(vehiclePoList);
        }
        if (!org.springframework.util.CollectionUtils.isEmpty(vehicleDevicePoList)) {
            vehicleService.saveVehDevices(vehicleDevicePoList);
        }
    }

    private DevicePo getDevicePo(VehDeviceDto vehDeviceDto) {
        DevicePo devicePo = new DevicePo();
        BeanUtils.copyProperties(vehDeviceDto, devicePo);
        devicePo.setVersion(1);
        devicePo.setCreateTime(LocalDateTime.now());
        devicePo.setCreateBy(getCurrentUser());
        devicePo.setDelFlag(0);
        return devicePo;
    }

    private VehicleDevicePo getVehicleDevicePo(VehicleDto vehicleDto, VehDeviceDto vehDeviceDto) {
        VehicleDevicePo vehicleDevicePo = new VehicleDevicePo();
        BeanUtils.copyProperties(vehDeviceDto, vehicleDevicePo);
        vehicleDevicePo.setVin(vehicleDto.getVin());
        vehicleDevicePo.setVersion(1);
        vehicleDevicePo.setCreateTime(LocalDateTime.now());
        vehicleDevicePo.setCreateBy(getCurrentUser());
        vehicleDevicePo.setDelFlag(0);
        return vehicleDevicePo;
    }

    @Override
    public String importExcel(MultipartFile uploadFile) {
        String taskNo = UUIDUtil.getUUID32();
        // 1.????????????????????????
        AbstractSaxReadListener<ExcelVehicleDto> vehImportListener = createVehicleExcelListener(taskNo);
        ReadSheet vehicleSheet = ExcelProcessor.readSheet("????????????", ExcelVehicleDto.class, vehImportListener);

        // 2.????????????????????????
        AbstractSaxReadListener<ExcelDeviceDto> deviceImportListener = createDeviceExcelListener(taskNo);
        ReadSheet deviceSheet = ExcelProcessor.readSheet("?????????????????????", ExcelDeviceDto.class, deviceImportListener);

        // 3.????????????
        ExcelProcessor.readSheets(uploadFile, vehicleSheet, deviceSheet);

        List<ExcelVehicleDto> vehicles = vehImportListener.getDatas();
        List<ExcelDeviceDto> devices = deviceImportListener.getDatas();
        if (CollectionUtils.isEmpty(vehicles) && CollectionUtils.isEmpty(devices)) {
            throw new AdamException(BusinessExceptionEnums.IMPORT_DATA_NOT_EXIST);
        }

        // 4.??????????????????
        setTotalProgress(taskNo, vehicles, devices);

        // 5.??????????????????????????????????????????????????????????????????
        threadPool.execute(() -> {
            try {
                checkImportVehicleData(taskNo, vehicles);
                checkImportDeviceData(taskNo, devices);
            } catch (Exception e) {
                log.error("????????????", e);
                processException(taskNo, vehicles, devices);
            }
        });
        return taskNo;
    }


    /**
     * ??????????????????sheet?????????
     *
     * @return ?????????
     */
    private AbstractSaxReadListener<ExcelVehicleDto> createVehicleExcelListener(String taskNo) {
        DefaultSaxReadListener<ExcelVehicleDto> readListener = new DefaultSaxReadListener();
        readListener.setSize(vehBatchSize);
        readListener.setTaskNo(taskNo);
        return readListener;
    }

    /**
     * ????????????????????????????????????
     *
     * @param taskNo ?????????
     * @param datas  ????????????
     */
    private void checkImportVehicleData(String taskNo, List<ExcelVehicleDto> datas) {
        Map<String, List<ExcelVehicleDto>> vinToDto = datas.stream().collect(Collectors.groupingBy(ExcelVehicleDto::getVin));
        List<ExcelVehicleDto> dtos = new ArrayList<>(100);
        for (ExcelVehicleDto vehicleDto : datas) {
            // ?????????????????????????????????????????????????????????????????????;????????????????????????????????????????????????
            if (isDelete(taskNo)) {
                return;
            }
            // ??????????????????
            pauseCheck(taskNo);

            // ????????????
            updateProgress(taskNo);

            dtos.add(vehicleDto);
            if (dtos.size() < 100) {
                continue;
            }

            checkAndImportVehTemp(dtos, vinToDto, taskNo);
            dtos.clear();
        }

        if (CollectionUtils.isNotEmpty(dtos)) {
            checkAndImportVehTemp(dtos, vinToDto, taskNo);
        }

        setSuccessAndFailed(taskNo);
    }

    /**
     * ????????????????????????????????????
     *
     * @param taskNo ?????????
     * @param datas  ??????
     */
    private void checkImportDeviceData(String taskNo, List<ExcelDeviceDto> datas) {
        Map<String, List<ExcelDeviceDto>> snToDto = datas.stream().collect(Collectors.groupingBy(ExcelDeviceDto::getProductSn));
        List<ExcelDeviceDto> dtos = new ArrayList<>(100);
        for (ExcelDeviceDto deviceDto : datas) {
            // 1.?????????????????????????????????????????????????????????????????????;????????????????????????????????????????????????
            if (isDelete(taskNo)) {
                return;
            }
            // 2.????????????????????????
            pauseCheck(taskNo);
            // 3.????????????
            updateProgress(taskNo);

            dtos.add(deviceDto);
            if (dtos.size() < 100) {
                continue;
            }
            checkAndImportDevTemp(dtos, snToDto, taskNo);
            dtos.clear();
        }

        if (CollectionUtils.isNotEmpty(dtos)) {
            checkAndImportDevTemp(dtos, snToDto, taskNo);
        }

        setSuccessAndFailed(taskNo);
    }


    @Override
    public ProgressVo queryProgress(String taskNo) {
        return obtainCacheObj(generateProgressCacheKey(taskNo), ProgressVo.class);
    }

    /**
     * ??????????????????key
     *
     * @param taskNo ?????????
     * @return ??????key
     */
    private String generateProgressCacheKey(String taskNo) {
        return RedisHelper.generateKey(env.getAppName(), "progress", taskNo);
    }

    /**
     * ??????????????????key
     *
     * @param taskNo ?????????
     * @return ??????key
     */
    private String generateControlCacheKey(String taskNo) {
        return RedisHelper.generateKey(env.getAppName(), "control", taskNo);
    }

    /**
     * ????????????????????????????????????
     *
     * @param taskNo ?????????
     */
    private void setSuccessAndFailed(String taskNo) {
        ProgressVo temp = obtainCacheObj(generateProgressCacheKey(taskNo), ProgressVo.class);
        if (temp != null) {
            temp.setSuccessed(obtainSuccessed(taskNo).size());
            temp.setFailed(obtainfailed(taskNo).size());
            cacheObj(generateProgressCacheKey(taskNo), temp);
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     */
    private Set<String> obtainSuccessed(String taskNo) {
        List<String> vehSuccessVins = vehRecordMapper.listVinsByTaskNo(taskNo, CheckStatus.SUCCESSED.getType());
        List<String> devFailedVins = devRecordMapper.listVinsByTaskNo(taskNo, CheckStatus.FAILED.getType());
        List<String> devSuccessVins = devRecordMapper.listVinsByTaskNo(taskNo, CheckStatus.SUCCESSED.getType());
        Set<String> vins = new HashSet<>();
        vins.addAll(vehSuccessVins);
        vins.addAll(devSuccessVins);
        vins.removeAll(devFailedVins);
        return vins;
    }

    /**
     * ??????????????????????????????????????????list
     */
    private List<String> obtainfailed(String taskNo) {
        ProgressVo progressVo = obtainCacheObj(generateProgressCacheKey(taskNo), ProgressVo.class);
        if (progressVo != null && progressVo.getFailed() != null && progressVo.getVins() != null) {
            Set<String> vins = progressVo.getVins();
            if (progressVo.getFailed().equals(vins.size())) {
                return vins.stream().collect(Collectors.toList());
            }
        }

        List<String> vehFailedVins = vehRecordMapper.listVinsByTaskNo(taskNo, CheckStatus.FAILED.getType());
        List<String> devFailedVins = devRecordMapper.listVinsByTaskNo(taskNo, CheckStatus.FAILED.getType());
        if (CollectionUtils.isEmpty(vehFailedVins)) {
            vehFailedVins.addAll(new HashSet<>(devFailedVins));
            return vehFailedVins;
        } else {
            for (String vin : devFailedVins) {
                if (!vehFailedVins.contains(vin)) {
                    vehFailedVins.add(vin);
                }
            }
        }
        return vehFailedVins;
    }

    /**
     * ???????????????????????????
     *
     * @param datas ????????????
     */
    private void checkAndImportVehTemp(List<ExcelVehicleDto> datas, Map<String, List<ExcelVehicleDto>> vinToDto, String taskNo) {
        // ??????????????????????????????????????????,??????????????????
        List<String> vins = datas.stream().map(ExcelVehicleDto::getVin).collect(Collectors.toList());
        List<VehiclePo> dbVehicle = vehicleService.listVehicle(vins);
        Map<String, VehiclePo> vinToDbVehicle = dbVehicle.stream().collect(Collectors.toMap(VehiclePo::getVin, o -> o));

        Map<String, VehicleImportRecordPo> vinToRecord = new HashMap<>();
        List<VehicleImportRecordPo> vehicles = new ArrayList<>();
        for (ExcelVehicleDto vehicleDto : datas) {
            vehicleDto.backEscapeName();
            VehicleImportRecordPo recordPo = new VehicleImportRecordPo();
            recordPo.setTaskNo(taskNo);
            recordPo.setCheckStatus(1);
            recordPo.setDelFlag(0);
            recordPo.setCreateBy(getCurrentUser());
            recordPo.setCreateTime(LocalDateTime.now());
            BeanUtils.copyProperties(vehicleDto, recordPo);

            if (vinToDbVehicle.get(vehicleDto.getVin()) != null || vinToDto.get(vehicleDto.getVin()).size() > 1) {
                recordPo.setCheckStatus(2);
                recordPo.setReason(ImportFailedReason.VEHICLE_EXISTS.getDesp());
            }

            if (vinToRecord.containsKey(vehicleDto.getVin())) {
                VehicleImportRecordPo temp = vinToRecord.get(vehicleDto.getVin());
                temp.setCheckStatus(2);
                temp.setReason(ImportFailedReason.VEHICLE_DUB.getDesp());
                recordPo.setCheckStatus(2);
                recordPo.setReason(ImportFailedReason.VEHICLE_DUB.getDesp());
            }

            OrgLocalCacheVo orgRelationPo = vehLocalCache.getVehOrgFromCache(vehicleDto.getVehModelName(), vehicleDto.getYearStyleName(), vehicleDto.getConfigName());
            if (orgRelationPo == null) {
                recordPo.setCheckStatus(2);
                recordPo.setReason(ImportFailedReason.MODEL_CONFIG_NOT_EXIST.getDesp());
            }

            vehicles.add(recordPo);
            vinToRecord.put(vehicleDto.getVin(), recordPo);
        }

        if (CollectionUtils.isNotEmpty(vehicles)) {
            vehRecordMapper.saveAllInBatch(vehicles);
        }
    }

    /**
     * ????????????device?????????
     *
     * @return ???????????????
     */
    private AbstractSaxReadListener<ExcelDeviceDto> createDeviceExcelListener(String taskNo) {
        DefaultSaxReadListener<ExcelDeviceDto> readListener = new DefaultSaxReadListener<>();
        readListener.setSize(vehDeviceBatchSize);
        readListener.setTaskNo(taskNo);
        return readListener;
    }

    /**
     * ???????????????
     */
    private void setTotalProgress(String taskNo, List<ExcelVehicleDto> vehicles, List<ExcelDeviceDto> devices) {
        ProgressVo progressVo = obtainCacheObj(generateProgressCacheKey(taskNo), ProgressVo.class);
        if (progressVo == null) {
            progressVo = new ProgressVo();
            progressVo.setCurrent(0);
            progressVo.setTotal(0);
        }
        progressVo.setTotal(progressVo.getTotal() + vehicles.size() + devices.size());

        Set<String> vins = new HashSet<>();
        vins.addAll(vehicles.stream().map(ExcelVehicleDto::getVin).collect(Collectors.toSet()));
        vins.addAll(devices.stream().map(ExcelDeviceDto::getVin).collect(Collectors.toSet()));
        progressVo.setVins(vins);
        cacheObj(generateProgressCacheKey(taskNo), progressVo);
    }

    /**
     * ????????????
     */
    private void updateProgress(String taskNo) {
        String cacheKey = generateProgressCacheKey(taskNo);
        ProgressVo temp = obtainCacheObj(cacheKey, ProgressVo.class);
        if (temp != null) {
            temp.addCurrent();
            cacheObj(cacheKey, temp);
        }
    }

    /**
     * ??????????????????
     */
    private void updateExceptionProgress(String taskNo, Integer current, Integer failedNum) {
        String cacheKey = generateProgressCacheKey(taskNo);
        ProgressVo temp = obtainCacheObj(cacheKey, ProgressVo.class);
        if (temp != null) {
            temp.setCurrent(current);
            temp.setSuccessed(0);
            temp.setFailed(failedNum);
            cacheObj(cacheKey, temp);
        }
    }

    /**
     * ????????????
     */
    private void processException(String taskNo, List<ExcelVehicleDto> vehicles, List<ExcelDeviceDto> devices) {
        vehRecordMapper.deleteByTaskNo(taskNo);
        devRecordMapper.deleteByTaskNo(taskNo);
        Set<String> vins = new HashSet<>();
        vins.addAll(vehicles.stream().map(ExcelVehicleDto::getVin).collect(Collectors.toSet()));
        vins.addAll(devices.stream().map(ExcelDeviceDto::getVin).collect(Collectors.toSet()));
        updateExceptionProgress(taskNo, vehicles.size() + devices.size(), vins.size());
    }

    /**
     * ??????????????????
     */
    private <T> T obtainCacheObj(String cacheKey, Class<T> tClass) {
        try {
            return redisClient.getObject(cacheKey, tClass);
        } catch (Exception e) {
            log.error("redis????????????:{0}", e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * ????????????
     *
     * @param cacheKey ??????key
     * @param obj      ????????????
     */
    private void cacheObj(String cacheKey, Object obj) {
        try {
            redisClient.setObject(cacheKey, obj, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis????????????:{0}", e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param dtos   ????????????
     * @param taskNo ?????????
     */
    private void checkAndImportDevTemp(List<ExcelDeviceDto> dtos, Map<String, List<ExcelDeviceDto>> snToDto, String taskNo) {
        List<VehicleDeviceImportRecordPo> inserts = new ArrayList<>();

        Set<String> vins = dtos.stream().map(ExcelDeviceDto::getVin).collect(Collectors.toSet());
        List<VehiclePo> vehicles = vehicleService.listVehicle(vins);
        Set<String> vehVins = vehicles.stream().map(VehiclePo::getVin).collect(Collectors.toSet());

        List<VehicleImportRecordPo> importedVehRecords = vehRecordMapper.listByTaskNoAndStatus(vins, taskNo, CheckStatus.SUCCESSED.getType(), null, null);
        Set<String> recordVins = importedVehRecords.stream().map(VehicleImportRecordPo::getVin).collect(Collectors.toSet());

        Map<String, Map<String, VehicleDeviceImportRecordPo>> vinToSnRecord = new HashMap<>();
        Map<String, VehicleDeviceImportRecordPo> vinToRecord = new HashMap<>();
        Map<String, VehicleDeviceImportRecordPo> simToRecord = new HashMap<>();
        for (ExcelDeviceDto deviceDto : dtos) {
            deviceDto.backEscapeName();
            VehicleDeviceImportRecordPo recordPo = transformDevDtoToRecord(taskNo, deviceDto);
            inserts.add(recordPo);

            // 1.?????????????????????,
            List<DevicePo> devices = deviceService.listDeviceBySn(deviceDto.getProductSn());
            if (CollectionUtils.isNotEmpty(devices) || snToDto.get(deviceDto.getProductSn()).size() > 1) {
                recordPo.setCheckStatus(2);
                recordPo.setReason(ImportFailedReason.DEVICE_EXIST.getDesp());
            }

            if (vinToSnRecord.containsKey(deviceDto.getVin())) {
                Map<String, VehicleDeviceImportRecordPo> temp = vinToSnRecord.get(deviceDto.getVin());
                if (temp.containsKey(deviceDto.getProductSn())) {
                    VehicleDeviceImportRecordPo vehDevTemp = temp.get(deviceDto.getProductSn());
                    vehDevTemp.setCheckStatus(2);
                    vehDevTemp.setReason(ImportFailedReason.DEVICE_DUB.getDesp());
                    recordPo.setCheckStatus(2);
                    recordPo.setReason(ImportFailedReason.DEVICE_DUB.getDesp());
                }
            }

            // 2.??????????????????
            if ((!vehVins.contains(deviceDto.getVin())) && (!recordVins.contains(deviceDto.getVin()))) {
                recordPo.setCheckStatus(2);
                recordPo.setReason(BusinessExceptionEnums.VEHICLE_NOT_EXIST.getDescription());
            }

            // 3.?????????????????????????????????????????????
            List<VehicleDeviceVo> vehDevices = vehDeviceService.listDevice(deviceDto.getVin(), recordPo.getDeviceType());
            if (CollectionUtils.isNotEmpty(vehDevices)) {
                recordPo.setCheckStatus(2);
                recordPo.setReason(ImportFailedReason.VEH_DEVICE_SAME_TYPE_BIND.getDesp());
            }
            String vinType = deviceDto.getDeviceType() + deviceDto.getVin();
            if (vinToRecord.containsKey(vinType)) {
                VehicleDeviceImportRecordPo temp = vinToRecord.get(vinType);
                temp.setCheckStatus(2);
                temp.setReason(ImportFailedReason.VEH_DEVICE_SAME_TYPE_DUB.getDesp());
                recordPo.setCheckStatus(2);
                recordPo.setReason(ImportFailedReason.VEH_DEVICE_SAME_TYPE_DUB.getDesp());
            }

            // 4.????????????tbox???iccid??????;sim??????????????????
/*            if (recordPo.getDeviceType() == 2 && StringUtils.isEmpty(recordPo.getIccid())) {
                recordPo.setCheckStatus(2);
                recordPo.setReason(ImportFailedReason.ICCID_NOT_EXIST.getDesp());
            }*/
            if (!StringUtils.isEmpty(recordPo.getIccid())) {
                simCheck(simToRecord, recordPo);
                simToRecord.put(recordPo.getIccid(), recordPo);
            }

            Map<String, VehicleDeviceImportRecordPo> snToImportDev = vinToSnRecord.computeIfAbsent(deviceDto.getVin(), k -> new HashMap<>());
            snToImportDev.put(deviceDto.getProductSn(), recordPo);
            vinToRecord.put(vinType, recordPo);
        }

        if (CollectionUtils.isNotEmpty(inserts)) {
            devRecordMapper.saveAllInBatch(inserts);
        }
    }

    /**
     * ???excel dto????????????????????????????????????
     *
     * @param taskNo    ?????????
     * @param deviceDto excel????????????
     * @return ?????????????????????????????????
     */
    private VehicleDeviceImportRecordPo transformDevDtoToRecord(String taskNo, ExcelDeviceDto deviceDto) {
        VehicleDeviceImportRecordPo recordPo = new VehicleDeviceImportRecordPo();
        recordPo.setDeviceId(IdGenerator.getDeviceIdByUUId());
        BeanUtils.copyProperties(deviceDto, recordPo);
        recordPo.setTaskNo(taskNo);
        Integer deviceType = vehLocalCache.getDeviceTypeId(deviceDto.getDeviceType());
        recordPo.setDeviceType(deviceType);
        recordPo.setDeviceName(deviceDto.getDeviceType());

        CacheDeviceModelInfo infoPo = deviceService.getDeviceModelInfo(deviceType, deviceDto.getDeviceModel());
        if (infoPo != null) {
            recordPo.setSupplierName(infoPo.getSupplierName());
        }
        recordPo.setCheckStatus(1);
        recordPo.setCreateBy(getCurrentUser());
        recordPo.setCreateTime(LocalDateTime.now());
        recordPo.setDelFlag(0);
        return recordPo;
    }

    /**
     * sim ?????????
     */
    private void simCheck(Map<String, VehicleDeviceImportRecordPo> simToRecord, VehicleDeviceImportRecordPo recordPo) {
        RestResponse<CmpSimVo> cmpResp = cmpFeignService.getSimByIccid(recordPo.getIccid());
        if (!cmpResp.isSuccess() || cmpResp.getRespData() == null) {
            recordPo.setCheckStatus(2);
            recordPo.setReason(ImportFailedReason.ICCID_NOT_EXIST.getDesp());
        }

        DevicePo devicePo = deviceService.getDeviceByIccid(recordPo.getIccid());
        if (devicePo != null) {
            recordPo.setCheckStatus(2);
            recordPo.setReason(ImportFailedReason.ICCID_HAS_BINDED.getDesp());
        }
        if (simToRecord.containsKey(recordPo.getIccid())) {
            VehicleDeviceImportRecordPo temp = simToRecord.get(recordPo.getIccid());
            temp.setCheckStatus(2);
            temp.setReason(ImportFailedReason.ICCID_DUB.getDesp());
            recordPo.setCheckStatus(2);
            recordPo.setReason(ImportFailedReason.ICCID_DUB.getDesp());
        }
    }

    /**
     * ????????????????????????
     *
     * @return ??????????????????????????????System
     */
    private String getCurrentUser() {
        return ReqContext.getUid() != null ? String.valueOf(ReqContext.getUid()) : "System";
    }

    @Override
    @Transactional
    public void controlTask(String taskNo, ControlType type) {
        String cacheKey = generateControlCacheKey(taskNo);
        switch (type) {
            case DELETE:
                cacheObj(cacheKey, type.getType());
                vehRecordMapper.deleteByTaskNo(taskNo);
                devRecordMapper.deleteByTaskNo(taskNo);
                break;
            case PAUSE_CHECK:
                cacheObj(cacheKey, type.getType());
                break;
            case RESUME_CHECK:
                redisClient.delete(cacheKey);
                break;
            case IMPORT:
                importVehAndDeviceData(taskNo);
            default:
                break;
        }
    }

    @Override
    public List<ExportVehicleVo> listCheckSuccessed(String taskNo) {
        Set<String> vins = obtainSuccessed(taskNo);

        List<VehicleImportRecordPo> records = vehRecordMapper.listByTaskNoAndStatus(vins, taskNo, CheckStatus.SUCCESSED.getType(), null, null);
        if (CollectionUtils.isEmpty(records)) {
            return vins.stream().map(vin -> {
                ExportVehicleVo vehicleVo = new ExportVehicleVo();
                vehicleVo.setVin(vin);
                return vehicleVo;
            }).collect(Collectors.toList());
        }

        Map<String, List<VehicleImportRecordPo>> vinToRecord = records.stream().collect(Collectors.groupingBy(VehicleImportRecordPo::getVin));
        return vins.stream().map(o -> {
            ExportVehicleVo vehicleVo = new ExportVehicleVo();
            List<VehicleImportRecordPo> recordPos = vinToRecord.get(o);
            VehicleImportRecordPo recordPo = CollectionUtils.isNotEmpty(recordPos) ? recordPos.get(0) : null;
            if (recordPo == null) {
                vehicleVo.setVin(o);
                return vehicleVo;
            }

            BeanUtils.copyProperties(recordPo, vehicleVo);
            vehicleVo.setProductTime(localDateTimeToDateStr(recordPo.getProductTime()));
            vehicleVo.setDownlineDate(localDateTimeToDateStr(recordPo.getDownlineDate()));
            vehicleVo.setCertificateDate(localDateTimeToDateStr(recordPo.getCertificateDate()));
            vehicleVo.setOutFactoryTime(localDateTimeToDateStr(recordPo.getOutFactoryTime()));
            return vehicleVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ExportFailedVo> listCheckFailed(String taskNo) {
        List<String> vins = obtainfailed(taskNo);
        if (CollectionUtils.isEmpty(vins)) {
            return Collections.emptyList();
        }

        // ??????????????????????????????
        List<VehicleImportRecordPo> vehRecords = vehRecordMapper.listByTaskNoAndStatus(vins, taskNo, CheckStatus.FAILED.getType(), null, null);
        Map<String, VehicleImportRecordPo> failedVeh = vehRecords.stream().collect(Collectors.toMap(VehicleImportRecordPo::getVin, Function.identity(), (o1, o2) -> o1));

        // ??????????????????????????????
        List<VehicleDeviceImportRecordPo> deviceRecords = devRecordMapper.listByTaskNoAndStatus(vins, taskNo, CheckStatus.FAILED.getType(), null, null);
        Map<String, VehicleDeviceImportRecordPo> failedDev = deviceRecords.stream().collect(Collectors.toMap(VehicleDeviceImportRecordPo::getVin, Function.identity(), (o1, o2) -> o1));

        List<ExportFailedVo> faileds = new ArrayList<>(vins.size());
        for (String vin : vins) {
            ExportFailedVo vehicleVo = new ExportFailedVo();
            vehicleVo.setVin(vin);
            faileds.add(vehicleVo);
            if (failedVeh.containsKey(vin)) {
                VehicleImportRecordPo recordPo = failedVeh.get(vin);
                vehicleVo.setReason(recordPo.getReason());
            } else if (failedDev.containsKey(vin)) {
                VehicleDeviceImportRecordPo recordPo = failedDev.get(vin);
                vehicleVo.setReason(recordPo.getReason());
            } else {
                vehicleVo.setReason(RespCode.UNKNOWN_ERROR.getDescription());
            }
        }
        return faileds;
    }

    /**
     * ??????????????????
     */
    private String localDateTimeToDateStr(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().toString() : null;
    }

    /**
     * @param taskNo ?????????
     */
    private void importVehAndDeviceData(String taskNo) {
        // ?????????????????????
        Set<String> vins = obtainSuccessed(taskNo);
        if (CollectionUtils.isEmpty(vins)) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }

        // ????????????
        List<VehicleImportRecordPo> vehRecords = vehRecordMapper.listByTaskNoAndStatus(vins, taskNo, CheckStatus.SUCCESSED.getType(), 0L, LIMIT);
        if (CollectionUtils.isNotEmpty(vehRecords)) {
            transformAndImportVeh(vehRecords);

            Long fromId = vehRecords.get(vehRecords.size() - 1).getId();
            while (vehRecords.size() >= LIMIT) {
                vehRecords = vehRecordMapper.listByTaskNoAndStatus(null, taskNo, CheckStatus.SUCCESSED.getType(), fromId, LIMIT);
                if (CollectionUtils.isNotEmpty(vehRecords)) {
                    fromId = vehRecords.get(vehRecords.size() - 1).getId();
                    transformAndImportVeh(vehRecords);
                }
            }
        }

        List<VehicleDeviceImportRecordPo> devRecords = devRecordMapper.listByTaskNoAndStatus(vins, taskNo, CheckStatus.SUCCESSED.getType(), 0L, LIMIT);
        if (CollectionUtils.isNotEmpty(devRecords)) {
            transformAndImportDev(devRecords);

            Long fromId = devRecords.get(devRecords.size() - 1).getId();
            while (devRecords.size() >= LIMIT) {
                devRecords = devRecordMapper.listByTaskNoAndStatus(vins, taskNo, CheckStatus.SUCCESSED.getType(), fromId, LIMIT);

                if (CollectionUtils.isNotEmpty(devRecords)) {
                    fromId = devRecords.get(devRecords.size() - 1).getId();
                    transformAndImportDev(devRecords);
                }
            }
        }
    }

    /**
     * ???????????????????????????
     */
    private void transformAndImportVeh(List<VehicleImportRecordPo> vehRecords) {
        List<VehiclePo> vehicles = vehRecords.stream().map(this::vehRecordToVehiclePo).collect(Collectors.toList());
        vehicleService.saveVehicles(vehicles);
    }

    /**
     * ????????????
     */
    private VehiclePo vehRecordToVehiclePo(VehicleImportRecordPo record) {
        OrgLocalCacheVo relationPo = vehLocalCache.getVehOrgFromCache(record.getVehModelName(), record.getYearStyleName(), record.getConfigName());
        VehiclePo vehiclePo = new VehiclePo();
        BeanUtils.copyProperties(record, vehiclePo);
        BeanUtils.copyProperties(relationPo, vehiclePo);
        vehiclePo.setId(null);
        vehiclePo.setOrgId(relationPo.getId());
        vehiclePo.setVehStatus(1); // ?????????
        vehiclePo.setVehLifecircle(0);
        vehiclePo.setCertificationStatus(0);
        vehiclePo.setBindStatus(0);
        vehiclePo.setActivationStatus(0);
        LocalDateTime dateTime = LocalDateTime.now();
        vehiclePo.setCreateBy(getCurrentUser());
        vehiclePo.setPublishDate(dateTime);
        vehiclePo.setCreateTime(dateTime);
        vehiclePo.setVersion(1);
        VehicleModelPo modelPo = vehLocalCache.getModel(relationPo.getVehModelId());
        if (modelPo != null) {
            vehiclePo.setVehType(modelPo.getVehType());
        }
        if (!StringUtils.isEmpty(record.getColorName())) {
            VehicleConfigCommonPo commonPo = relationService.getCommonPo(relationPo.getConfigurationId(), record.getColorName());
            if (commonPo != null) {
                vehiclePo.setColorId(commonPo.getId());
            }
        }
        return vehiclePo;
    }

    /**
     * ????????????????????????
     *
     * @param devRecords ?????????????????????
     */
    private void transformAndImportDev(List<VehicleDeviceImportRecordPo> devRecords) {
        List<DevicePo> devicePos = new ArrayList<>();
        List<VehicleDevicePo> vehDevicePos = new ArrayList<>();
        for (VehicleDeviceImportRecordPo record : devRecords) {
            DevicePo devicePo = transformDevRecordToDevciePo(record);
            devicePos.add(devicePo);
            VehicleDevicePo vehDevicePo = transformDevRecordToVehDevice(record);
            vehDevicePos.add(vehDevicePo);
        }

        deviceService.saveDevices(devicePos);
        vehicleService.saveVehDevices(vehDevicePos);
    }

    /**
     * ??????excel?????????????????????po
     */
    private DevicePo transformDevRecordToDevciePo(VehicleDeviceImportRecordPo record) {
        DevicePo devicePo = new DevicePo();
        BeanUtils.copyProperties(record, devicePo);
        devicePo.setDeviceType(record.getDeviceType());
        devicePo.setCreateBy(getCurrentUser());
        devicePo.setCreateTime(LocalDateTime.now());
        devicePo.setDelFlag(0);
        devicePo.setId(null);
        devicePo.setEnrollStatus(1);
        devicePo.setVersion(1);
        return devicePo;
    }

    private VehicleDevicePo transformDevRecordToVehDevice(VehicleDeviceImportRecordPo record) {
        VehicleDevicePo vehDevice = new VehicleDevicePo();
        BeanUtils.copyProperties(record, vehDevice);
        vehDevice.setDeviceType(vehLocalCache.getDeviceTypeId(record.getDeviceName()));
        vehDevice.setCreateBy(getCurrentUser());
        vehDevice.setVersion(1);
        vehDevice.setDelFlag(0);
        return vehDevice;
    }

    /**
     * ????????????????????????
     */
    private boolean isDelete(String taskNo) {
        Integer taskType = obtainCacheObj(generateControlCacheKey(taskNo), Integer.class);
        if (taskType == null) {
            return false;
        }
        return taskType.equals(ControlType.DELETE.getType());
    }

    /**
     * ????????????????????????
     */
    private void pauseCheck(String taskNo) {
        while (true) {
            Integer taskType = obtainCacheObj(generateControlCacheKey(taskNo), Integer.class);
            if (taskType == null) {
                return;
            }

            if (taskType.equals(PAUSE_CHECK.getType())) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.error("??????????????????" + e.getMessage());
                }
            }
        }
    }
}
