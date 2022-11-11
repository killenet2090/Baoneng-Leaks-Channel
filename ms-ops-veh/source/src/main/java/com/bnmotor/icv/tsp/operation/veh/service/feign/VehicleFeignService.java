package com.bnmotor.icv.tsp.operation.veh.service.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.EditVehLabelDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehProjectDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.*;
import com.bnmotor.icv.tsp.operation.veh.service.feign.fallback.VehicleFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhoulong1
 * @ClassName: VehicleFeignService
 * @Description: feign调用车辆服务接口
 * @since: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-device", fallback = VehicleFeignServiceFallbackFactory.class)
public interface VehicleFeignService {

    /**
     * 查询车辆基本信息详情
     * @param vin 车辆VIN
     * @return
     */
    @GetMapping("/inner/vehicle/detail")
    RestResponse getVehicleDetail(@RequestParam("vin") String vin);

    /**
     *获取车辆型号统计数据
     * @param projectDto
     * @param pageDto
     * @return
     */
    @GetMapping("/inner/vehProject/listStatistics")
    RestResponse<Page<VehProjectStatisticsVo>> queryVehicleProjects(@SpringQueryMap QueryVehProjectDto projectDto);

    /**
     *获取某个项目全部车辆型号
     * @param projectCode
     * @return
     */
    @GetMapping("/inner/vehProject/listModel")
    RestResponse<List<VehProjectDetailVo>> queryVehicleModels(@RequestParam("code") String projectCode,@RequestParam("vehType") Integer vehType,@RequestParam("searchKey")String searchKey);

    /**
     * @param vin 车辆Vin
     * @param deviceType  设备类型1-tbox,2-ecu,3-车机,4-sim
     * @return  设备关联记录
     */
    @GetMapping("/inner/vehicle/device")
    RestResponse<List<VehicleDeviceVo>> queryDevice(@RequestParam("vin") String vin, @RequestParam("deviceType") Integer deviceType);

    /**
     * 车辆列表查询
     * @param vehicleDto
     * @return
     */
    @PostMapping(value = "/inner/vehicle/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    RestResponse<Page<VehicleVo>> queryVehicles(@RequestBody QueryVehicleDto vehicleDto);

    /**
     * 车辆信息编辑
     * @param labelDto 修改信息对象
     * @return
     */
    @PostMapping(value = "/inner/label/editVehLabel")
    RestResponse editVehLabel(@RequestBody EditVehLabelDto labelDto);

    /**
     * 根据vins集合查询车辆信息
     * @param vins
     * @return
     */
    @GetMapping(value = "/inner/vehicle/vehicles")
    RestResponse<List<UserBindDetailVo>> getVehicles(@RequestParam("vins") List<String> vins);
}
