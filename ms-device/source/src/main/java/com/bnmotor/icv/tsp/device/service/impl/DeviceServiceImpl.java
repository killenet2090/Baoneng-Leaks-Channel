package com.bnmotor.icv.tsp.device.service.impl;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.adam.data.mysql.utils.PageUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.common.Enviroment;
import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.model.response.feign.CmpSimVo;
import com.bnmotor.icv.tsp.device.service.feign.CmpFeignService;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.VehicleDevice;
import com.bnmotor.icv.tsp.device.mapper.*;
import com.bnmotor.icv.tsp.device.mapstuct.ReplacementRecordVoMapper;
import com.bnmotor.icv.tsp.device.mapstuct.DeviceModelVoMapper;
import com.bnmotor.icv.tsp.device.mapstuct.DeviceTypeVoMapper;
import com.bnmotor.icv.tsp.device.model.entity.*;
import com.bnmotor.icv.tsp.device.model.response.device.*;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.MgtUserVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleVo;
import com.bnmotor.icv.tsp.device.service.IDeviceService;
import com.bnmotor.icv.tsp.device.service.IVehicleService;
import com.bnmotor.icv.tsp.device.service.feign.UserFeignService;
import com.bnmotor.icv.tsp.device.util.JsonUtils;
import com.bnmotor.icv.tsp.device.util.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: DeviceServiceImpl
 * @Description: 车辆设备服务实现
 * @author: zhangwei2
 * @date: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service
@RefreshScope
public class DeviceServiceImpl implements IDeviceService {
    @Resource
    private VehicleDeviceMapper vehDeviceMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private DeviceModelInfoMapper deviceModelInfoMapper;
    @Resource
    private DeviceTypeMapper deviceTypeMapper;
    @Resource
    private DeviceReplacementMapper replacementMapper;

    @Resource
    private IVehicleService vehicleService;
    @Resource
    private UserFeignService feighService;
    @Resource
    private CmpFeignService cmpFeignService;

    @Resource
    private Enviroment env;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private VehLocalCache localCache;

    @Override
    public Page<DeviceVo> listAllDevices(Pageable pageable, String searchKey, String searchValue) {
        // 1.查询设备基本信息
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<DeviceVo> deviceVos = deviceMapper.listAll(PageUtil.map(pageable), searchKey, searchValue);
        Page<DeviceVo> pageResp = pageTransform(deviceVos);
        List<DeviceVo> devices = deviceVos.getRecords();
        if (CollectionUtils.isEmpty(devices)) {
            return pageResp;
        }

        List<String> deviceIds = devices.stream().map(DeviceVo::getDeviceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deviceIds)) {
            return pageResp;
        }

        // 3.查询换件历史
        List<ReplaceTimesVo> replaces = replacementMapper.countBindedRecords(deviceIds);
        Map<String, Integer> deviceIdToNum = new HashMap<>();
        if (CollectionUtils.isNotEmpty(replaces)) {
            deviceIdToNum = replaces.stream().collect(Collectors.toMap(ReplaceTimesVo::getDeviceId, ReplaceTimesVo::getNum));
        }

        for (DeviceVo deviceVo : devices) {
            Integer num = deviceIdToNum.get(deviceVo.getDeviceId());
            deviceVo.setReplaceTimes(num);
            deviceVo.setDeviceTypeName(localCache.getDeviceTypeName(deviceVo.getDeviceType()));
        }
        return pageResp;
    }

    private <R> Page pageTransform(com.baomidou.mybatisplus.extension.plugins.pagination.Page<R> iPage) {
        Page page = new Page();
        page.setCurrent((int) iPage.getCurrent());
        page.setPageSize((int) iPage.getPages());
        page.setTotal(iPage.getTotal());
        if (page.getTotal() == 0L) {
            page.setPages(0L);
        } else {
            Long pages = page.getTotal() / (long) page.getPageSize();
            page.setPages(page.getTotal() % (long) page.getPageSize() == 0L ? (long) pages.intValue() : (long) (pages.intValue() + 1));
        }

        page.setList(iPage.getRecords());
        return page;
    }

    @Override
    public DeviceDetailVo getDetail(Long id) {
        // 1.查询设备基本信息
        DevicePo devicePo = deviceMapper.selectById(id);
        if (devicePo == null) {
            return null;
        }
        DeviceDetailVo detailVo = new DeviceDetailVo();
        BeanUtils.copyProperties(devicePo, detailVo);
        DeviceTypePo typePo = deviceTypeMapper.selectByDeviceType(devicePo.getDeviceType());
        detailVo.setDeviceTypeName(typePo.getTypeName());

        // 2.查询车辆相关信息
        VehicleDevicePo vehDevicePo = vehDeviceMapper.getByDeviceId(devicePo.getDeviceId());
        if (vehDevicePo != null) {
            detailVo.setVin(vehDevicePo.getVin());
            VehicleVo vehicle = vehicleService.getVehicleVo(vehDevicePo.getVin());
            if (vehicle != null) {
                detailVo.setDrivingLicPlate(vehicle.getDrivingLicPlate());
                detailVo.setVehModelName(vehicle.getVehModelName());
            }

            RestResponse<MgtUserVo> userResult = feighService.getUserInfo(vehDevicePo.getVin());
            if (userResult.isSuccess() && userResult.getRespData() != null) {
                detailVo.setPhone(userResult.getRespData().getPhone());
            }
        }

        // 3.查询sim相关信息
        RestResponse<CmpSimVo> cmpResp = cmpFeignService.getSimByIccid(devicePo.getIccid());
        if (cmpResp.isSuccess() && cmpResp.getRespData() != null) {
            CmpSimVo cmpSimVo = cmpResp.getRespData();
            detailVo.setSimVo(CmpSimVo.transfromToSimVo(cmpSimVo));
        }

        return detailVo;
    }

    @Override
    public Page<DeviceTypeVo> listDeviceTypes(Pageable pageable) {
        return DeviceTypeVoMapper.INSTANCE.map(deviceTypeMapper.selectAllByCondition(PageUtil.map(pageable), pageable.getSearchKey()));
    }

    @Override
    public Page<DeviceModelVo> listDeviceModels(Pageable pageable, Integer deviceType) {
        Page<DeviceModelVo> modelVos = DeviceModelVoMapper.INSTANCE.map(deviceModelInfoMapper.selectPage(PageUtil.map(pageable), pageable.getSearchKey(), deviceType));
        return modelVos;
    }

    @Override
    public List<DeviceModelInfoPo> listDeviceModel(Integer deviceType) {
        return deviceModelInfoMapper.selectAll(deviceType);
    }

    @Override
    public List<VehicleDevice> listVehDevices(List<String> vins) {
        return vehDeviceMapper.listVehDeviceByVins(vins);
    }

    /**
     * 根据零件类型和零件型号获取车型零部件型号
     */
    public CacheDeviceModelInfo getDeviceModelInfo(Integer deviceType, String deviceModel) {
        String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_DEVICE_MODEL_INFO, String.valueOf(deviceType), deviceModel);
        try {
            Object object = redisTemplate.opsForValue().get(cacheKey);
            if (object instanceof CacheDeviceModelInfo) {
                return (CacheDeviceModelInfo) object;
            }

            DeviceModelInfoPo infoPo = deviceModelInfoMapper.selectByDeviceTypeAndModel(deviceType, deviceModel);
            if (infoPo != null) {
                CacheDeviceModelInfo info = new CacheDeviceModelInfo();
                BeanUtils.copyProperties(infoPo, info);
                redisTemplate.opsForValue().set(cacheKey, info, 24 * 60L, TimeUnit.SECONDS);
                return info;
            }
        } catch (Exception e) {
            log.error("获取车型零部件型号信息错误,deviceType:{},deviceModel:{}", deviceType, deviceModel);
        }

        return null;
    }

    @Override
    public Page<BindVehicleRecordVo> listBindRecords(Pageable pageable, String deviceId) {
        Page<BindVehicleRecordVo> vehDevices = ReplacementRecordVoMapper.INSTANCE.map(replacementMapper.selectPageBindedRecords(PageUtil.map(pageable), deviceId));
        List<BindVehicleRecordVo> recordVos = vehDevices.getList();
        if (CollectionUtils.isEmpty(recordVos)) {
            return vehDevices;
        }

        Map<String, BindVehicleRecordVo> vinToRecord = new HashMap<>();
        List<String> vins = new ArrayList<>();
        for (BindVehicleRecordVo recordVo : recordVos) {
            String vin = recordVo.getVin();
            vinToRecord.put(vin, recordVo);
            vins.add(vin);
        }

        List<VehiclePo> vehiclePos = vehicleService.listVehicle(vins);
        if (CollectionUtils.isEmpty(vehiclePos)) {
            return vehDevices;
        }

        Map<String, VehiclePo> vinToVehicle = vehiclePos.stream().collect(Collectors.toMap(VehiclePo::getVin, vehiclePo -> vehiclePo));
        for (BindVehicleRecordVo recordVo : recordVos) {
            VehiclePo vehiclePo = vinToVehicle.get(recordVo.getVin());
            if (vehiclePo == null) {
                continue;
            }
            recordVo.setVehModelName(vehiclePo.getVehModelName());
            recordVo.setUnbindTime(recordVo.getCreateTime());
        }
        return vehDevices;
    }

    @Override
    public List<DeviceModelInfoVo> listAllDeviceModelInfoVo() {
        return deviceModelInfoMapper.selectAllDeviceInfoVos();
    }

    @Override
    public int saveDevices(List<DevicePo> devices) {
        return deviceMapper.saveAllInBatch(devices);
    }

    @Override
    public DevicePo getDeviceByIccid(String iccId) {
        String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_DEVICE, iccId);
        String json = (String) redisTemplate.opsForValue().get(cacheKey);
        if (!StringUtils.isEmpty(json)) {
            return JsonUtils.jsonStringToObject(json, DevicePo.class);
        }

        DevicePo devicePo = deviceMapper.getByIccidId(iccId);
        if (devicePo != null) {
            redisTemplate.opsForValue().set(cacheKey, JsonUtils.objectToJsonString(devicePo));
        }
        return devicePo;
    }

    @Override
    public List<DevicePo> listDeviceBySn(String sn) {
        return deviceMapper.listBySn(sn);
    }
}
