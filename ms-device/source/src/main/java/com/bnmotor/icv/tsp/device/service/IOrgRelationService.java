package com.bnmotor.icv.tsp.device.service;

import com.bnmotor.icv.tsp.device.model.entity.VehicleConfigCommonPo;
import com.bnmotor.icv.tsp.device.model.request.vehModel.VehModelListQueryDto;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.VehAllLevelVo;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehicleModelVo;

import java.util.List;

/**
 * @ClassName: IOrgRelationService
 * @Description: 车辆组织关系服务
 * @author: zhangjianghua1
 * @date: 2020/11/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IOrgRelationService {
    /**
     * 获取车辆组织关系平铺列表,所有层
     */
    VehAllLevelVo getVehAllLevelVo();

    /**
     * 根据配置id查询车型配置参数
     */
    List<VehicleConfigCommonPo> listByConfigIds(List<Long> configIds);

    /**
     * 根据配置id和颜色获取车型通用配置
     *
     * @param configId  配置id
     * @param colorName 颜色名称
     * @return 车型通用配置
     */
    VehicleConfigCommonPo getCommonPo(Long configId, String colorName);

    List<VehicleModelVo> getVehModelList(VehModelListQueryDto dto);
}
