package com.bnmotor.icv.tsp.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.device.common.*;
import com.bnmotor.icv.tsp.device.common.enums.veh.VehicleCategoryType;
import com.bnmotor.icv.tsp.device.common.enums.veh.VehicleType;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.BindStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.CertificationStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.QualityInspectStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.VehicleStatus;
import com.bnmotor.icv.tsp.device.job.dispatch.AsynQueryStatus;
import com.bnmotor.icv.tsp.device.job.dispatch.QueryType;
import com.bnmotor.icv.tsp.device.mapper.*;
import com.bnmotor.icv.tsp.device.model.entity.*;
import com.bnmotor.icv.tsp.device.model.request.feign.*;
import com.bnmotor.icv.tsp.device.model.request.vehicle.QueryVehicleDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleCerStatusUpdateDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleLicPlateDto;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.VehInfoVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.CondenseVehicleVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.OrgLocalCacheVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleListVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleVo;
import com.bnmotor.icv.tsp.device.model.response.tag.TagVo;
import com.bnmotor.icv.tsp.device.model.response.tag.VehicleTagVo;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.VehicleDetailVo;
import com.bnmotor.icv.tsp.device.service.IVehicleService;
import com.bnmotor.icv.tsp.device.service.feign.BluetoothKeyFeignService;
import com.bnmotor.icv.tsp.device.service.feign.UserFeignService;
import com.bnmotor.icv.tsp.device.util.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: VehicleServiceImpl
 * @Description: 车辆信息实现类, 提供车辆信息录入，导入导出，车辆信息查询等接口
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class VehicleServiceImpl implements IVehicleService {
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private VehicleDeviceMapper vehicleDeviceMapper;
    @Resource
    private VehicleModelMapper modelMapper;
    @Resource
    private VehicleConfigCommonMapper commonMapper;
    @Resource
    private VehicleTagMapper tagMapper;
    @Resource
    private VehicleSaleInfoMapper saleInfoMapper;
    @Resource
    private VehicleAsynQueryMapper asynQueryMapper;

    @Resource
    private UserFeignService userFeignService;
    @Resource
    private BluetoothKeyFeignService bluetoothKeyFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private VehLocalCache vehLocalCache;
    @Resource
    private Enviroment env;

    @Override
    public IPage<VehicleListVo> list(QueryVehicleDto vehicleDto) {
        //车辆属性查询
        List<VehicleTagVo> vehicleTags = tagMapper.getVehicleTagsByCat(vehicleDto.getCategoryId(), CollectionUtils.isEmpty(vehicleDto.getVins())
                ? new ArrayList<>() : vehicleDto.getVins(), vehicleDto.getTagIds());
        //分页查询
        Page page = new Page(vehicleDto.getCurrent(), vehicleDto.getPageSize());
        IPage pos = Optional.ofNullable(vehicleMapper.selectAllByCondition(page, vehicleDto))
                .orElse(new Page(vehicleDto.getCurrent(), vehicleDto.getPageSize()));
        List<VehiclePo> vehicles = CollectionUtils.isEmpty(pos.getRecords()) ? new ArrayList<>() : pos.getRecords();
        if (CollectionUtils.isEmpty(vehicles)) {
            return page;
        }
        BeanUtils.copyProperties(pos, page);

        //数据组装
        List<VehicleTagVo> finalVehicleTags = vehicleTags;
        List<VehicleListVo> vehicleVos = vehicles.stream().distinct().map(vehicle -> {
            VehicleListVo vehicleVo = new VehicleListVo();
            BeanUtils.copyProperties(vehicle, vehicleVo);
            //车辆属性
            String categoryName = org.springframework.util.CollectionUtils.isEmpty(finalVehicleTags) ? "" :
                    finalVehicleTags.stream().filter(tagVo -> vehicle.getVin().equals(tagVo.getVin()))
                            .map(VehicleTagVo::getTagName).collect(Collectors.joining(","));
            vehicleVo.setVehAttribute(categoryName);
            //发布日期
            if (vehicle.getPublishDate() != null) {
                vehicleVo.setPublishDate(vehicle.getPublishDate().format(Constant.FORMATTER));
            }

            return vehicleVo;
        }).collect(Collectors.toList());
        page.setRecords(vehicleVos);
        return page;
    }

    /**
     * 获取车辆详情
     * 1.获取车辆基本信息
     * 2.获取车辆设备信息
     * 3.获取车辆绑定信息
     * 4.获取车主信息
     *
     * @param vin 车辆唯一标识
     * @return 车辆详细信息
     */
    @Override
    public VehicleDetailVo getVehDetail(String vin) {
        // 1.获取车辆基本信息
        VehiclePo vehiclePo = vehicleMapper.selectByVin(vin);
        if (vehiclePo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }

        VehicleDetailVo detailVo = new VehicleDetailVo();
        BeanUtils.copyProperties(vehiclePo, detailVo);

        // 设置车辆各种状态
        VehicleType type = VehicleType.valueOf(vehiclePo.getVehType());
        detailVo.setVehTypeName(type != null ? type.getDesp() : null);
        CertificationStatus certificationStatus = CertificationStatus.valueOf(vehiclePo.getCertificationStatus());
        detailVo.setCertificationStatus(certificationStatus != null ? certificationStatus.getDesp() : null);
        BindStatus bindStatus = BindStatus.valueOf(vehiclePo.getBindStatus());
        detailVo.setBindStatus(bindStatus != null ? bindStatus.getDesp() : null);
        VehicleStatus vehicleStatus = VehicleStatus.valueOf(vehiclePo.getVehStatus());
        detailVo.setVehStatus(vehicleStatus != null ? vehicleStatus.getDesp() : null);
        QualityInspectStatus inspectStatus = QualityInspectStatus.valueOf(vehiclePo.getQualityInspectStatus());
        detailVo.setQualityInspectStatus(inspectStatus != null ? inspectStatus.getDesp() : null);

        // 查询车辆级别和类别
        VehicleModelPo modelPo = modelMapper.selectById(vehiclePo.getVehModelId());
        if (modelPo != null) {
            VehicleCategoryType categoryType = VehicleCategoryType.valueOf(modelPo.getVehCategory());
            detailVo.setVehCategory(categoryType != null ? categoryType.getDesp() : null);
            detailVo.setVehLevel(modelPo.getVehLevel());
        }

        // 查询车辆颜色
        if (vehiclePo.getColorId() != null) {
            VehicleConfigCommonPo commonPo = commonMapper.selectById(vehiclePo.getColorId());
            detailVo.setColor(commonPo != null ? commonPo.getColorName() : null);
        }

        // 设置各种时间
        detailVo.setCreateTime(vehiclePo.getCreateTime());
        detailVo.setProductTime(vehiclePo.getProductTime());
        detailVo.setOutFactoryTime(vehiclePo.getOutFactoryTime());
        detailVo.setBindTime(vehiclePo.getBindDate());
        detailVo.setCertificateTime(vehiclePo.getCertificateDate());
        detailVo.setActiveTime(vehiclePo.getActivationDate());

        // 查询销售信息表
        VehicleSaleInfoPo infoPo = saleInfoMapper.selectByVin(vehiclePo.getVin());
        if (infoPo != null) {
            detailVo.setSaleDate(infoPo.getSaleDate());
            detailVo.setDealer(infoPo.getDealer());
        }

        // 5.获取label信息,设置车辆属性和车辆标签
        List<String> vins = new ArrayList<>();
        vins.add(vehiclePo.getVin());
        List<VehicleTagPo> relationPos = tagMapper.listByVinsAndCategoryId(vins, null);
        if (CollectionUtils.isEmpty(relationPos)) {
            return detailVo;
        }

        StringBuilder sb = new StringBuilder();
        detailVo.setLabels(relationPos.stream().map(relation -> {
            if ("车辆属性".equals(relation.getCategoryName())) {
                sb.append(relation.getTagName()).append(",");
            }
            TagVo tagVo = new TagVo();
            tagVo.setCategoryId(String.valueOf(relation.getCategoryId()));
            tagVo.setCategoryName(relation.getCategoryName());
            tagVo.setTagId(String.valueOf(relation.getTagId()));
            tagVo.setTagName(relation.getTagName());
            return tagVo;
        }).collect(Collectors.toList()));

        String attributes = sb.toString();
        if (!StringUtils.isEmpty(attributes)) {
            detailVo.setAttributes(attributes.substring(0, attributes.length() - 1));
        }

        return detailVo;
    }

    @Override
    public List<VehiclePo> listVehicle(Collection<String> vins) {
        return vehicleMapper.selectAllByVins(vins);
    }

    @Override
    public VehicleVo getVehicleVo(String vin) {
        String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_VEHICLE, vin);
        try {
            String strJson = stringRedisTemplate.opsForValue().get(cacheKey);
            if (!StringUtils.isEmpty(strJson)) {
                return JsonUtil.toObject(strJson, VehicleVo.class);
            }
        } catch (IOException e) {
            log.error("queryVehicle redis exception -> Message: [{}]", e.getMessage());
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }

        VehiclePo vehiclePo = vehicleMapper.selectByVin(vin);
        if (vehiclePo == null) {
            log.warn("车辆不存在:" + vin);
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }
        VehicleType type = VehicleType.valueOf(vehiclePo.getVehType());
        if (type == null) {
            log.warn("车辆动力类型数据错误:" + vin);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }

        VehicleVo vehicleVo = new VehicleVo();
        BeanUtils.copyProperties(vehiclePo, vehicleVo);
        vehicleVo.setId(String.valueOf(vehiclePo.getId()));
        vehicleVo.setOrgId(String.valueOf(vehiclePo.getOrgId()));
        vehicleVo.setVehType(type.getDesp());
        OrgLocalCacheVo cacheVo = vehLocalCache.getOrgById(vehiclePo.getOrgId());
        if (cacheVo != null) {
            vehicleVo.setYearStyleName(cacheVo.getYearStyleName());
            vehicleVo.setVehConfigName(cacheVo.getConfigName());
        }
        if (vehiclePo.getColorId() != null) {
            VehicleConfigCommonPo commonPo = commonMapper.selectById(vehiclePo.getColorId());
            vehicleVo.setColor(commonPo.getColorName());
        }

        // 缓存车辆基本信息
        try {
            stringRedisTemplate.opsForValue().set(cacheKey, JsonUtil.toJson(vehicleVo));
        } catch (Exception e) {
            log.error("queryVehicle redis exception -> Message: [{}]", e.getMessage());
        }
        return vehicleVo;
    }

    /**
     * 删除车辆缓存操作
     *
     * @param vin 车架号
     */
    private void deleteCacheVehicle(String vin) {
        String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_VEHICLE, vin);
        // 缓存车辆基本信息
        try {
            stringRedisTemplate.delete(cacheKey);
        } catch (Exception e) {
            log.error("queryVehicle redis exception -> Message: [{}]", e.getMessage());
        }
    }

    @Override
    public List<String> listVinsByOrgId(Long orgId) {
        return vehicleMapper.selectAllVinsByOrgId(orgId);
    }

    /**
     * 判断车辆是否有效(售出并且未报废登记)
     */
    @Override
    public boolean checkValid(String vin) {
        VehiclePo vehiclePo = vehicleMapper.checkValid(vin, VehicleStatus.SOLED.getStatus());
        return vehiclePo != null && !StringUtils.isEmpty(vehiclePo.getId());
    }

    @Override
    public void aysnQuery(Long uid, List<Long> configIds, List<Long> labelIds) {
        // 根据uid更新原来任务状态
        asynQueryMapper.updateByUid(uid, Constant.SYSTEM);

        List<VehicleAsynQueryPo> inserts = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(configIds)) {
            inserts = configIds.stream().map(id -> assembleQueryPo(uid, QueryType.CONFIG_ID.getType(), id)).collect(Collectors.toList());
        }

        if (CollectionUtils.isNotEmpty(labelIds)) {
            inserts = labelIds.stream().map(id -> assembleQueryPo(uid, QueryType.TAG_ID.getType(), id)).collect(Collectors.toList());

        }

        if (CollectionUtils.isNotEmpty(inserts)) {
            asynQueryMapper.saveAllInBatch(inserts);
        }
    }

    /**
     * 更新车牌号
     */
    @Override
    public int updateDrivingLicPlate(VehicleLicPlateDto vehicleLicPlateDto) {
        int result = vehicleMapper.updateLicPlate(vehicleLicPlateDto.getVin(), vehicleLicPlateDto.getDrivingLicPlate());
        UpdateVehInfoDto updateVehInfoDto = new UpdateVehInfoDto();
        updateVehInfoDto.setDrivingCicPlate(vehicleLicPlateDto.getDrivingLicPlate());
        updateVehInfoDto.setVin(vehicleLicPlateDto.getVin());
        userFeignService.updateVehInfo(updateVehInfoDto);

        UpdateVehInfoBluDto updateVehInfoBluDto = new UpdateVehInfoBluDto();
        updateVehInfoBluDto.setDeviceId(vehicleLicPlateDto.getVin());
        updateVehInfoBluDto.setDeviceName(vehicleLicPlateDto.getDrivingLicPlate());
        bluetoothKeyFeignService.update(updateVehInfoBluDto);

        deleteCacheVehicle(vehicleLicPlateDto.getVin());
        return result;
    }

    /**
     * 检查车牌号码是否存在
     */
    @Override
    public boolean checkDrivingLicPlate(VehicleLicPlateDto vehicleLicPlateDto) {
        int result = vehicleMapper.existByLicPlate(vehicleLicPlateDto.getDrivingLicPlate());
        return result > 0;
    }

    private VehicleAsynQueryPo assembleQueryPo(Long uid, Integer queryType, Long queryValue) {
        VehicleAsynQueryPo queryPo = new VehicleAsynQueryPo();
        queryPo.setUid(uid);
        queryPo.setQueryType(queryType);
        queryPo.setQueryValue(queryValue);
        queryPo.setStatus(AsynQueryStatus.UNEXECUTED.getStatus());
        queryPo.setQueryCursor(0L);
        queryPo.setVersion(1);
        queryPo.setCreateBy("Admin");
        return queryPo;
    }

    @Override
    public List<CondenseVehicleVo> listByCombinedId(List<Long> modelIds, Long fromId, Integer size) {
        List<VehiclePo> vehiclePos = vehicleMapper.listByFromId(null, modelIds, fromId, size);
        if (CollectionUtils.isEmpty(vehiclePos)) {
            return Collections.emptyList();
        }

        return vehiclePos.stream().map(o -> {
            CondenseVehicleVo vehicleVo = new CondenseVehicleVo();
            vehicleVo.setId(o.getId());
            vehicleVo.setVin(o.getVin());
            return vehicleVo;
        }).collect(Collectors.toList());
    }

    @Override
    public void saveVehicles(List<VehiclePo> vehicles) {
        vehicleMapper.saveAllInBatch(vehicles);
    }

    @Override
    public void saveVehDevices(List<VehicleDevicePo> vehDevices) {
        vehicleDeviceMapper.saveAllInBatch(vehDevices);
    }

    @Override
    public VehiclePo getVehicle(String vin) {
        return vehicleMapper.selectByVin(vin);
    }

    @Override
    public VehInfoVo getVehInfo(String vin) {
        VehiclePo po = vehicleMapper.selectByVin(vin);
        if (po == null) {
            return null;
        }
        VehInfoVo vo = new VehInfoVo();
        vo.setVin(po.getVin());
        vo.setDrivingLicPlate(po.getDrivingLicPlate());
        vo.setEngineNo(po.getEngineNo());
        vo.setDownlineDate(po.getDownlineDate());
        return vo;
    }

    /**
     * 更新实名状态
     * @param vehicleCerStatusUpdateDto
     */
    @Override
    public void updateCertificationStatus(VehicleCerStatusUpdateDto vehicleCerStatusUpdateDto) {
        VehiclePo vehiclePo = new VehiclePo();
        vehiclePo.setVin(vehicleCerStatusUpdateDto.getVin());
        vehiclePo.setCertificationStatus(vehicleCerStatusUpdateDto.getCertificationStatus());
        vehiclePo.setOldCertificationStatus(vehicleCerStatusUpdateDto.getOldCertificationStatus());
        vehiclePo.setUpdateTime(LocalDateTime.now());
        vehiclePo.setUpdateBy((vehicleCerStatusUpdateDto.getUpdateUserId() != null ? vehicleCerStatusUpdateDto.getUpdateUserId().toString() : Constant.SYSTEM));
        vehicleMapper.compareCertificationStatusAndUpdate(vehiclePo);
    }

}
