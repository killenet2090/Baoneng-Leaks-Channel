package com.bnmotor.icv.tsp.device.service.impl;

import com.bnmotor.icv.tsp.device.common.Enviroment;
import com.bnmotor.icv.tsp.device.mapper.VehicleConfigCommonMapper;
import com.bnmotor.icv.tsp.device.mapper.VehicleOrgRelationMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleConfigCommonPo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleOrgRelationPo;
import com.bnmotor.icv.tsp.device.model.request.vehModel.VehModelListQueryDto;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.*;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehicleModelVo;
import com.bnmotor.icv.tsp.device.service.IOrgRelationService;
import com.bnmotor.icv.tsp.device.util.JsonUtils;
import com.bnmotor.icv.tsp.device.util.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: OrgRelationServiceImpl
 * @Description: 车型配置层级关系服务实现
 * @author: zhangwei2
 * @date: 2020/11/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service
public class OrgRelationServiceImpl implements IOrgRelationService {
    @Resource
    private VehicleOrgRelationMapper orgRelationMapper;
    @Resource
    private VehicleConfigCommonMapper commonMapper;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private Enviroment env;

    @Override
    public VehAllLevelVo getVehAllLevelVo() {
        List<VehicleOrgRelationPo> allPos = orgRelationMapper.selectAll();

        List<VehBrandVo> brandVos = new ArrayList<>();
        List<VehSeriesVo> seriesVos = new ArrayList<>();
        List<VehModelVo> modelVos = new ArrayList<>();
        List<VehYearStyleVo> yearStyleVos = new ArrayList<>();
        List<VehConfigurationVo> configurationVos = new ArrayList<>();

        for (VehicleOrgRelationPo po : allPos) {
            brandVos.add(toBrandVo(po));
            seriesVos.add(toSeriesVo(po));
            modelVos.add(toModelVo(po));
            yearStyleVos.add(toYearStyleVo(po));
            configurationVos.add(toConfigurationVo(po));
        }

        VehAllLevelVo vo = new VehAllLevelVo();
        vo.setBrands(brandVos);
        vo.setSeries(seriesVos);
        vo.setModels(modelVos);
        vo.setYearStyles(yearStyleVos);
        vo.setConfigurations(configurationVos);

        return vo;
    }

    private VehBrandVo toBrandVo(VehicleOrgRelationPo po) {
        VehBrandVo vo = new VehBrandVo();
        vo.setId(po.getBrandId());
        vo.setName(po.getBrandName());
        return vo;
    }

    private VehSeriesVo toSeriesVo(VehicleOrgRelationPo po) {
        VehSeriesVo vo = new VehSeriesVo();
        vo.setId(po.getVehSeriesId());
        vo.setName(po.getVehSeriesName());
        vo.setBrandId(po.getBrandId());
        return vo;
    }

    private VehModelVo toModelVo(VehicleOrgRelationPo po) {
        VehModelVo vo = new VehModelVo();
        vo.setId(po.getVehModelId());
        vo.setName(po.getVehModelName());
        vo.setSeriesId(po.getVehSeriesId());
        return vo;
    }

    private VehYearStyleVo toYearStyleVo(VehicleOrgRelationPo po) {
        VehYearStyleVo vo = new VehYearStyleVo();
        vo.setId(po.getYearStyle());
        vo.setName(po.getYearStyleName());
        vo.setModelId(po.getVehModelId());
        return vo;
    }

    private VehConfigurationVo toConfigurationVo(VehicleOrgRelationPo po) {
        VehConfigurationVo vo = new VehConfigurationVo();
        vo.setId(po.getConfigurationId());
        vo.setName(po.getConfigName());
        vo.setOrgId(po.getId());
        vo.setYearStyleId(po.getYearStyle());
        return vo;
    }

    @Override
    public List<VehicleConfigCommonPo> listByConfigIds(List<Long> configIds) {
        return commonMapper.listByConfigIds(configIds);
    }

    @Override
    public VehicleConfigCommonPo getCommonPo(Long configId, String colorName) {
        String cacheKey = RedisHelper.generateKey(env.getAppName(), "commonConfig", String.valueOf(configId));
        String commonPoStr = (String) redisTemplate.opsForHash().get(cacheKey, colorName);
        if (!StringUtils.isEmpty(commonPoStr)) {
            try {
                return JsonUtils.jsonStringToObject(commonPoStr, VehicleConfigCommonPo.class);
            } catch (Exception e) {
                log.error("获取车型配置异常configId:{0},colorName:{1}", configId, colorName);
            }
        }

        VehicleConfigCommonPo commonPo = commonMapper.getByConfigIdAndColorName(configId, colorName);
        try {
            redisTemplate.opsForHash().put(cacheKey, colorName, JsonUtils.objectToJsonString(commonPo));
        } catch (Exception e) {
            log.error("获取车型配置异常configId:{0},colorName:{1}", configId, colorName);
        }

        return commonPo;
    }

    @Override
    public List<VehicleModelVo> getVehModelList(VehModelListQueryDto dto) {
        List<VehicleModelVo> vehicleModelVoList=orgRelationMapper.getVehModelList(dto);
        return vehicleModelVoList;
    }
}
