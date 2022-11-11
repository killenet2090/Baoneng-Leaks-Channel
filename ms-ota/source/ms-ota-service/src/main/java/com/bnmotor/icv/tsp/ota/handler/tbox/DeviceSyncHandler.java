package com.bnmotor.icv.tsp.ota.handler.tbox;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.req.device.DeviceSyncInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceComponentInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.VehTagDto;
import com.bnmotor.icv.tsp.ota.model.resp.feign.VehicleOrgRelationVo;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncService;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncWrapperService;
import com.bnmotor.icv.tsp.ota.service.feign.MsDeviceFeignService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyValidationUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName: DeviceSyncHandler
 * @Description: 设备信息同步
 * @author: xuxiaochang1
 * @date: 2020/7/27 9:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@OtaMessageAttribute(topics = { "device-ota-all-data", "device-increment-data" }, msgtype = DeviceSyncInfoDto.class)
@Slf4j
public class DeviceSyncHandler implements KafkaHandlerManager.KafkaHandler<DeviceSyncInfoDto> {
    
    @Autowired
    @Qualifier("fotaDeviceSyncServiceV2")
    private IFotaDeviceSyncService fotaDeviceSyncService;

    @Autowired
    private IFotaDeviceSyncWrapperService fotaDeviceSyncWrapperService;

    @Autowired
    private MsDeviceFeignService msDeviceFeignService;

    /**
     * 定义机构本地缓存
     */
    private final LoadingCache<Long, VehicleOrgRelationVo> orgCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<>() {
                        @Override
                        public VehicleOrgRelationVo load(Long ordId) throws Exception {
                            RestResponse<VehicleOrgRelationVo> restResponse = msDeviceFeignService.getOrgRelationById(ordId);
                            return Objects.nonNull(restResponse) ? restResponse.getRespData() : null;
                        }
                    });

    @Override
    public void onMessage(DeviceSyncInfoDto deviceSyncInfoDto) {
        log.debug("------从TSP设备管理系统平台接受到消息, start---.deviceSyncInfoDto={}", deviceSyncInfoDto);
        handle(deviceSyncInfoDto);
        log.debug("------从TSP设备管理系统平台接受到消息, end---.deviceSyncInfoDto", deviceSyncInfoDto);
    }

    /**
     * 处理上行消息实际业务逻辑
     * @param deviceSyncInfoDto
     */
    private void handle(DeviceSyncInfoDto deviceSyncInfoDto) {
        Integer type = deviceSyncInfoDto.getType();
        /*Integer businessType = deviceSyncInfoDto.getBusinessType();*/

        Enums.DeviceSyncTypeEnum deviceSyncTypeEnum = Enums.DeviceSyncTypeEnum.getByType(type);
        if(Objects.isNull(deviceSyncTypeEnum)){
            log.warn("参数异常.deviceSyncInfoDto={}", deviceSyncInfoDto);
            return;
        }

        try {
            //车辆同步
            if (deviceSyncTypeEnum.equals(Enums.DeviceSyncTypeEnum.CAR_INFO_ALL)) {
                FotaCarInfoDto fotaCarInfoDto = getT(deviceSyncInfoDto, FotaCarInfoDto.class);

                log.info("orgId={}", fotaCarInfoDto.getOrgId());
                wrapFotaCarInfoDto(fotaCarInfoDto);
                fotaCarInfoDto.setAction(deviceSyncInfoDto.getAction());
                log.info("操作类型={}, 操作对象={}", deviceSyncInfoDto.getAction(), fotaCarInfoDto);
                MyValidationUtil.validate(fotaCarInfoDto);
                DeviceTreeNodePo deviceTreeNodePo = getDeviceTreeNodeDo(fotaCarInfoDto);
                log.info("deviceTreeNodePo={}", deviceTreeNodePo);
                MyAssertUtil.notNull(deviceTreeNodePo, "当前设备树节点不存在, 请检查数据");
                fotaDeviceSyncWrapperService.syncFotaCar(fotaCarInfoDto, deviceTreeNodePo);
            }
            //零件同步
            else if (deviceSyncTypeEnum.equals(Enums.DeviceSyncTypeEnum.COMPONNET_INFO_ALL)) {
                //只处理新增消息
                FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto = getT(deviceSyncInfoDto, FotaDeviceComponentInfoDto.class);
                wrapFotaDeviceComponentInfoDto(fotaDeviceComponentInfoDto);
                fotaDeviceComponentInfoDto.setAction(deviceSyncInfoDto.getAction());
                MyValidationUtil.validate(fotaDeviceComponentInfoDto);

                DeviceTreeNodePo deviceTreeNodePo = getDeviceTreeNodeDo(fotaDeviceComponentInfoDto);
                log.info("deviceTreeNodePo={}", deviceTreeNodePo);
                MyAssertUtil.notNull(deviceTreeNodePo, "当前设备树节点不存在, 请检查数据");
                fotaDeviceSyncWrapperService.syncFotaDeviceComponentInfo(fotaDeviceComponentInfoDto, deviceTreeNodePo);
            } else if (deviceSyncTypeEnum.equals(Enums.DeviceSyncTypeEnum.TAG_INFO_ALL)) {
                VehTagDto vehTagDto = getT(deviceSyncInfoDto, VehTagDto.class);
                MyValidationUtil.validate(vehTagDto);
                vehTagDto.setAction(deviceSyncInfoDto.getAction());
                if(Objects.isNull(vehTagDto.getAction())){
                    vehTagDto.setAction(Enums.DeviceSyncActionTypeEnum.ADD.getType());
                }
                fotaDeviceSyncService.syncVehTagInfo(vehTagDto);            }
        }catch (Exception e){
            log.error("处理同步消息异常.", e);
        }
    }

    /**
     * 包装车辆信息
     * @param fotaCarInfoDto
     * @throws ExecutionException
     */
    private void wrapFotaCarInfoDto(final FotaCarInfoDto fotaCarInfoDto) throws ExecutionException {
        VehicleOrgRelationVo vehicleOrgRelationVo = orgCache.get(fotaCarInfoDto.getOrgId());
        fotaCarInfoDto.setBrand(vehicleOrgRelationVo.getBrandName());
        fotaCarInfoDto.setBrandCode(Long.toString(vehicleOrgRelationVo.getBrandId()));
        fotaCarInfoDto.setSeries(vehicleOrgRelationVo.getVehSeriesName());
        fotaCarInfoDto.setSeriesCode(Long.toString(vehicleOrgRelationVo.getVehSeriesId()));
        fotaCarInfoDto.setModel(vehicleOrgRelationVo.getVehModelName());
        fotaCarInfoDto.setModelCode(Long.toString(vehicleOrgRelationVo.getVehSeriesId()));
        fotaCarInfoDto.setYear(vehicleOrgRelationVo.getYearStyleName());
        fotaCarInfoDto.setYearCode(Long.toString(vehicleOrgRelationVo.getYearStyle()));
        fotaCarInfoDto.setConf(vehicleOrgRelationVo.getConfigName());
        fotaCarInfoDto.setConfCode(Long.toString(vehicleOrgRelationVo.getConfigurationId()));
    }

    /**
     * 包装零件代码
     * @param fotaDeviceComponentInfoDto
     * @throws ExecutionException
     */
    private void wrapFotaDeviceComponentInfoDto(final FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto) throws ExecutionException {
        VehicleOrgRelationVo vehicleOrgRelationVo = orgCache.get(fotaDeviceComponentInfoDto.getOrgId());
        fotaDeviceComponentInfoDto.setBrand(vehicleOrgRelationVo.getBrandName());
        fotaDeviceComponentInfoDto.setBrandCode(Long.toString(vehicleOrgRelationVo.getBrandId()));
        fotaDeviceComponentInfoDto.setSeries(vehicleOrgRelationVo.getVehSeriesName());
        fotaDeviceComponentInfoDto.setSeriesCode(Long.toString(vehicleOrgRelationVo.getVehSeriesId()));
        fotaDeviceComponentInfoDto.setModel(vehicleOrgRelationVo.getVehModelName());
        fotaDeviceComponentInfoDto.setModelCode(Long.toString(vehicleOrgRelationVo.getVehSeriesId()));
        fotaDeviceComponentInfoDto.setYear(vehicleOrgRelationVo.getYearStyleName());
        fotaDeviceComponentInfoDto.setYearCode(Long.toString(vehicleOrgRelationVo.getYearStyle()));
        fotaDeviceComponentInfoDto.setConf(vehicleOrgRelationVo.getConfigName());
        fotaDeviceComponentInfoDto.setConfCode(Long.toString(vehicleOrgRelationVo.getConfigurationId()));
    }

    /**
     * 获取目标设备树节点对象
     * @param fotaCarInfoDto
     * @return
     */
    private DeviceTreeNodePo getDeviceTreeNodeDo(FotaCarInfoDto fotaCarInfoDto){
        /*Supplier<FotaDeviceTreeNodeDto> supplier = () -> DeviceInfoSyncMapper.INSTANCE.fotaCarInfoDto2FotaDeviceTreeNodeDto(fotaCarInfoDto);*/
        DeviceTreeNodePo deviceTreeNodePo =  fotaDeviceSyncService.getConfLevelDeviceTreeNode(fotaCarInfoDto, FotaCarInfoDto.class/*, supplier*/);
        return deviceTreeNodePo;
    }

    /**
     * 获取目标设备树节点对象
     * @param fotaDeviceComponentInfoDto
     * @return
     */
    private DeviceTreeNodePo getDeviceTreeNodeDo(FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto){
        /*Supplier<FotaDeviceTreeNodeDto> supplier = () -> DeviceInfoSyncMapper.INSTANCE.fotaDeviceComponentInfoDto2FotaDeviceTreeNodeDto(fotaDeviceComponentInfoDto);*/
        //该方法的调用，需要在后续的事务操作完成之前执行
        DeviceTreeNodePo deviceTreeNodePo =  fotaDeviceSyncService.getConfLevelDeviceTreeNode(fotaDeviceComponentInfoDto, FotaDeviceComponentInfoDto.class);
        return deviceTreeNodePo;
    }

    /**
     * 获取实际要处理的类信息
     *
     * @param deviceSyncInfoDto
     * @param clz
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> T getT(DeviceSyncInfoDto deviceSyncInfoDto, Class<T> clz) throws IOException {
        return JsonUtil.toObject(JsonUtil.toJson(deviceSyncInfoDto.getData()), clz);
    }
}