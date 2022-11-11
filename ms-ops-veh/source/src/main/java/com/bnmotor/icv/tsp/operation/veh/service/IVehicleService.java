package com.bnmotor.icv.tsp.operation.veh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.operation.veh.model.request.EditVehLabelDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.VehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.UserBindDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehicleDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehicleVo;

/**
 * @author zhoulong1
 * @ClassName: IVehicleService
 * @Description: 车辆管理接口
 * @since: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IVehicleService {

    /**
     * 查询车辆的详细信息,包括车辆基本信息，车辆零部件信息;车辆标签等信息
     *
     * @param vin 车辆唯一标识
     * @return 车辆详情信息
     */
    VehicleDetailVo getVehDetail(String vin);

    IPage<VehicleVo> getPagedVehicles(QueryVehicleDto vehicleDto);

    void editVehLabel(EditVehLabelDto labelDto);

    IPage<UserBindDetailVo> getUserBindVehicleInfo(VehicleDto vehicleDto);
}
