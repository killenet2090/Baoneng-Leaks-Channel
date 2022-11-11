package com.bnmotor.icv.tsp.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.common.Enviroment;
import com.bnmotor.icv.tsp.device.common.enums.veh.VehicleType;
import com.bnmotor.icv.tsp.device.mapper.*;
import com.bnmotor.icv.tsp.device.model.entity.*;
import com.bnmotor.icv.tsp.device.model.request.vehModel.ConfigImgDto;
import com.bnmotor.icv.tsp.device.model.response.device.ModelDeviceVo;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.*;
import com.bnmotor.icv.tsp.device.model.response.vehModel.*;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehModelImgVo;
import com.bnmotor.icv.tsp.device.service.IVehModelService;
import com.bnmotor.icv.tsp.device.util.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: VehModelServiceImpl
 * @Description: 车型服务实现服务类，用于车型管理，车型导出等接口
 * @author: zhangwei2
 * @date: 2020/8/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class VehModelServiceImpl implements IVehModelService {
    @Resource
    private VehicleBrandMapper brandMapper;
    @Resource
    private VehicleSeriesMapper seriesMapper;
    @Resource
    private VehicleModelMapper modelMapper;
    @Resource
    private VehicleYearStyleMapper styleMapper;
    @Resource
    private VehicleConfigurationMapper configurationMapper;
    @Resource
    private VehicleOrgRelationMapper orgRelationMapper;
    @Resource
    private VehModelDeviceMapper modelDeviceMapper;
    @Resource
    private DeviceModelInfoMapper infoMapper;
    @Resource
    private DeviceTypeMapper deviceTypeMapper;
    @Resource
    private VehicleConfigImgMapper imgMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private Enviroment env;

    @Override
    public List<com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo> listModels(Long seriesId) {
        List<VehicleModelPo> modelPos = modelMapper.listBySeriesId(seriesId);
        if (CollectionUtils.isEmpty(modelPos)) {
            return Collections.emptyList();
        }

        return modelPos.stream().map(label -> {
            com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo modelVo = new com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo();
            BeanUtils.copyProperties(label, modelVo);
            return modelVo;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<VehModelStatisticsVo> listStatistics(Integer vehType, Pageable pageable) {
        Page page = new Page(pageable.getCurrent(), pageable.getPageSize());
        IPage<VehModelStatisticsVo> pos = orgRelationMapper.selectAllStatistics(page, vehType, pageable.getSearchKey());
        return pos;
    }

    @Override
    public List<VehModelConfigVo> listModelConfig(String modelCode, Integer vehType) {
        List<VehModelConfigVo> modelConfigs = orgRelationMapper.selectByModelCode(modelCode, vehType);
        if (CollectionUtils.isEmpty(modelConfigs)) {
            return Collections.EMPTY_LIST;
        }

        for (VehModelConfigVo vo : modelConfigs) {
            VehicleType vehicleType = VehicleType.valueOf(vo.getVehType());
            vo.setVehTypeValue(vehicleType != null ? vehicleType.getDesp() : null);
        }

        return modelConfigs;
    }

    @Override
    public VehConfigDetailVo queryVehConfigDetail(Long configId) {
        VehConfigDetailVo detailVo = modelMapper.getVehicleModelDetailByConfigId(configId);

        if (detailVo == null) {
            return null;
        }

        VehicleType vehicleType = VehicleType.valueOf(detailVo.getVehType());
        detailVo.setVehTypeValue(vehicleType != null ? vehicleType.getDesp() : null);

        List<VehModelDevicePo> modelDevices = modelDeviceMapper.listByConfigId(configId);
        if (CollectionUtils.isEmpty(modelDevices)) {
            return detailVo;
        }

        List<String> deviceModels = new ArrayList<>(modelDevices.size());
        List<Integer> deviceTypes = new ArrayList<>(modelDevices.size());
        for (VehModelDevicePo devicePo : modelDevices) {
            deviceModels.add(devicePo.getDeviceModel());
            deviceTypes.add(devicePo.getDeviceType());
        }

        // 2.查询设备类型信息
        List<DeviceTypePo> deviceTypePos = deviceTypeMapper.selectByDeviceTypes(deviceTypes);
        Map<Integer, String> deviceTypeToName = deviceTypePos.stream().collect(Collectors.toMap(DeviceTypePo::getDeviceType, DeviceTypePo::getTypeName));


        List<DeviceModelInfoPo> infoPos = infoMapper.selectAllByModel(deviceModels);
        if (CollectionUtils.isEmpty(infoPos)) {
            return detailVo;
        }
        Map<String, DeviceModelInfoPo> modelToInfo = infoPos.stream()
                .filter(o -> deviceTypes.contains(o.getDeviceType())).collect(Collectors.toMap(DeviceModelInfoPo::getDeviceModel, o -> o));

        List<ModelDeviceVo> deviceModelVos = modelDevices.stream().map(o -> {
            ModelDeviceVo deviceVo = new ModelDeviceVo();
            deviceVo.setId(o.getId());
            DeviceModelInfoPo infoPo = modelToInfo.get(o.getDeviceModel());
            deviceVo.setSupplierName(infoPo != null ? infoPo.getSupplierName() : null);
            deviceVo.setDeviceType(deviceTypeToName.get(o.getDeviceType()));
            deviceVo.setDeviceModel(o.getDeviceModel());
            return deviceVo;
        }).collect(Collectors.toList());
        detailVo.setDevices(deviceModelVos);
        return detailVo;
    }

    @Override
    public VehAllLevelVo queryLevel() {
        VehAllLevelVo levelVo = new VehAllLevelVo();
        // 查询品牌
        String brandCacheKey = RedisHelper.generateKey(env.getAppName(), Constant.VEHICLE, Constant.BRAND);
        List<VehBrandVo> brandVos = (List<VehBrandVo>) redisTemplate.opsForList().range(brandCacheKey, 0, -1);
        if (CollectionUtils.isEmpty(brandVos)) {
            List<VehicleBrandPo> brandPos = brandMapper.selectAll();
            if (CollectionUtils.isNotEmpty(brandPos)) {
                brandVos = brandPos.stream().map(brand -> {
                    VehBrandVo brandVo = new VehBrandVo();
                    BeanUtils.copyProperties(brand, brandVo);
                    return brandVo;
                }).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(brandCacheKey, brandVos);
            }
        }
        levelVo.setBrands(brandVos);

        // 车系
        String seriesCacheKey = RedisHelper.generateKey(env.getAppName(), Constant.VEHICLE, Constant.SERIES);
        List<VehSeriesVo> seriesVos = (List<VehSeriesVo>) redisTemplate.opsForList().range(seriesCacheKey, 0, -1);
        if (CollectionUtils.isEmpty(seriesVos)) {
            List<VehicleSeriesPo> seriesPos = seriesMapper.selectAll();
            if (CollectionUtils.isNotEmpty(seriesPos)) {
                seriesVos = seriesPos.stream().map(series -> {
                    VehSeriesVo seriesVo = new VehSeriesVo();
                    BeanUtils.copyProperties(series, seriesVo);
                    return seriesVo;
                }).collect(Collectors.toList());
            }
            redisTemplate.opsForList().leftPushAll(seriesCacheKey, seriesVos);
        }
        levelVo.setSeries(seriesVos);

        // 查询车型
        String modelCacheKey = RedisHelper.generateKey(env.getAppName(), Constant.VEHICLE, Constant.MODEL);
        List<com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo> modelVos = (List<com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo>) redisTemplate.opsForList().range(modelCacheKey, 0, -1);
        if (CollectionUtils.isEmpty(modelVos)) {
            List<VehicleModelPo> modelPos = modelMapper.selectAll();
            if (CollectionUtils.isNotEmpty(modelPos)) {
                modelVos = modelPos.stream().map(model -> {
                    com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo modelVo = new com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo();
                    BeanUtils.copyProperties(model, modelVo);
                    return modelVo;
                }).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(modelCacheKey, modelVos);
            }
        }
        levelVo.setModels(modelVos);

        // 查询年款
        String yealCacheKey = RedisHelper.generateKey(env.getAppName(), Constant.VEHICLE, Constant.YEAL);
        List<VehYearStyleVo> styleVos = (List<VehYearStyleVo>) redisTemplate.opsForList().range(yealCacheKey, 0, -1);
        if (CollectionUtils.isEmpty(styleVos)) {
            List<VehicleYearStylePo> yearPos = styleMapper.selectAll();
            if (CollectionUtils.isNotEmpty(yearPos)) {
                styleVos = yearPos.stream().map(year -> {
                    VehYearStyleVo styleVo = new VehYearStyleVo();
                    BeanUtils.copyProperties(year, styleVo);
                    return styleVo;
                }).collect(Collectors.toList());
            }
            redisTemplate.opsForList().leftPushAll(yealCacheKey, styleVos);
        }
        levelVo.setYearStyles(styleVos);

        // 查询配置
        String configCacheKey = RedisHelper.generateKey(env.getAppName(), Constant.VEHICLE, Constant.CONFIG);
        List<VehConfigurationVo> configVos = (List<VehConfigurationVo>) redisTemplate.opsForList().range(configCacheKey, 0, -1);
        if (CollectionUtils.isEmpty(configVos)) {
            List<VehicleConfigurationPo> configPos = configurationMapper.selectAll();
            if (CollectionUtils.isNotEmpty(configPos)) {
                configVos = configPos.stream().map(config -> {
                    VehConfigurationVo configVo = new VehConfigurationVo();
                    BeanUtils.copyProperties(config, configVo);
                    return configVo;
                }).collect(Collectors.toList());
            }
            redisTemplate.opsForList().leftPushAll(configCacheKey, configVos);
        }
        levelVo.setConfigurations(configVos);

        return levelVo;
    }

    @Override
    public VehModelPicVo listAllConfigPic(ConfigImgDto imgDto) {
        VehModelPicVo picVo = new VehModelPicVo();
        List<VehicleOrgRelationPo> relations = orgRelationMapper.listConfigByModelId(imgDto.getModelId(), imgDto.getYearId(), imgDto.getConfigId(), imgDto.getOffset(), imgDto.getLimit());
        if (CollectionUtils.isEmpty(relations)) {
            picVo.setTotalConfig(0);
            return picVo;
        }

        Map<Long, VehicleOrgRelationPo> configIdToOrg = new HashMap<>(relations.size());
        Set<Long> configIds = new HashSet<>(relations.size());
        for (VehicleOrgRelationPo relationPo : relations) {
            configIdToOrg.put(relationPo.getConfigurationId(), relationPo);
            configIds.add(relationPo.getConfigurationId());
        }

        int count = imgMapper.sumConfigs(configIds, imgDto.getImgCategory());
        picVo.setTotalConfig(count);
        List<VehConfigPicVo> configPics = new ArrayList<>(relations.size());
        for (Long configId : configIds) {
            List<PicVo> configPic = imgMapper.listConfigPic(configId, imgDto.getImgCategory(), imgDto.getImgType(), imgDto.getPicNum());
            if (CollectionUtils.isEmpty(configPic)) {
                continue;
            }

            VehicleOrgRelationPo relationPo = configIdToOrg.get(configId);
            Integer total = imgMapper.countConfigPic(configId, imgDto.getImgCategory(), imgDto.getImgType());
            VehConfigPicVo configPicVo = new VehConfigPicVo();
            configPicVo.setConfigId(String.valueOf(configId));
            configPicVo.setConfigName(relationPo.getConfigName());
            configPicVo.setYearStyleName(relationPo.getYearStyleName());
            configPicVo.setTotalPics(total);
            configPicVo.setPics(configPic);
            configPics.add(configPicVo);
        }

        picVo.setConfigs(configPics);
        return picVo;
    }

    @Override
    public List<PicVo> listConfigPic(Long configId, Integer imgCategory, Integer imgType) {
        return imgMapper.listConfigPic(configId, imgCategory, imgType, null);
    }

    @Override
    public List<VehModelImgVo> listAllModel() {
        List<VehicleModelPo> models = modelMapper.selectAll();
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }

        List<VehModelImgVo> resps = new ArrayList<>();
        for (VehicleModelPo modelPo : models) {
            VehModelImgVo modelYearVo = new VehModelImgVo();
            resps.add(modelYearVo);
            modelYearVo.setId(String.valueOf(modelPo.getId()));
            modelYearVo.setModelName(modelPo.getName());
            modelYearVo.setImgUrl("http://10.210.100.17:9000/tsp/timg.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin%2F20200610%2F%2Fs3%2Faws4_request&X-Amz-Date=20200610T025735Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=336b137db71878651e48d971f6b3016d8980314772b844ac1f41807cbb791fb9");
        }
        return resps;
    }

    @Override
    public List<YearVo> listYearConfig(Long modelId) {
        List<VehicleYearStylePo> years = styleMapper.listByModelId(modelId);
        if (CollectionUtils.isEmpty(years)) {
            return Collections.emptyList();
        }

        List<Long> yearIds = years.stream().map(o -> o.getId()).collect(Collectors.toList());
        List<VehicleConfigurationPo> configs = configurationMapper.listByYearIds(yearIds);
        Map<Long, List<Long>> yearIdToConfigs = new HashMap<>();
        Map<Long, VehicleConfigurationPo> configIdToPo = new HashMap<>();
        for (VehicleConfigurationPo configPo : configs) {
            configIdToPo.put(configPo.getId(), configPo);
            List<Long> configList = yearIdToConfigs.get(configPo.getYearStyleId());
            if (configList == null) {
                configList = new ArrayList<>();
                yearIdToConfigs.put(configPo.getYearStyleId(), configList);
            }
            configList.add(configPo.getId());
        }

        List<YearVo> resps = new ArrayList<>();
        for (VehicleYearStylePo stylePo : years) {
            YearVo yearVo = new YearVo();
            resps.add(yearVo);
            yearVo.setId(String.valueOf(stylePo.getId()));
            yearVo.setYearName(stylePo.getName());

            List<Long> configIds = yearIdToConfigs.get(stylePo.getId());
            List<VehConfigVo> configVos = new ArrayList<>();
            yearVo.setConfigs(configVos);
            for (Long configId : configIds) {
                VehicleConfigurationPo configurationPo = configIdToPo.get(configId);
                if (configurationPo == null) {
                    continue;
                }
                VehConfigVo configVo = new VehConfigVo();
                configVos.add(configVo);
                configVo.setId(String.valueOf(configId));
                configVo.setConfigName(configurationPo.getName());
            }
        }

        return resps;
    }

    @Override
    public PicVo getRelationImg(Long id) {
        VehicleConfigImgPo imgPo = imgMapper.selectById(id);
        if (imgPo == null) {
            return null;
        }

        Long relationId = imgPo.getRelationId();
        if (relationId != null) {
            imgPo = imgMapper.selectById(relationId);
        }

        if (imgPo == null) {
            return null;
        }

        PicVo picVo = new PicVo();
        BeanUtils.copyProperties(imgPo, picVo);
        picVo.setId(String.valueOf(imgPo.getId()));
        return picVo;
    }
}
