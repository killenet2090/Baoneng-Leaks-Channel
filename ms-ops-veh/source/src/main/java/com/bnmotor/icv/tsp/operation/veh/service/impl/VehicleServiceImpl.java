package com.bnmotor.icv.tsp.operation.veh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.EditVehLabelDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.VehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.*;
import com.bnmotor.icv.tsp.operation.veh.service.IVehicleService;
import com.bnmotor.icv.tsp.operation.veh.service.feign.DeviceOnlineFeignService;
import com.bnmotor.icv.tsp.operation.veh.service.feign.UserFeighService;
import com.bnmotor.icv.tsp.operation.veh.service.feign.VehicleFeignService;
import com.bnmotor.icv.tsp.operation.veh.util.DesensitizedUtils;
import com.bnmotor.icv.tsp.operation.veh.util.RestResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhoulong1
 * @ClassName: VehicleServiceImpl
 * @Description: 车辆服务接口实现类
 * @since: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class VehicleServiceImpl implements IVehicleService {

    @Autowired
    VehicleFeignService vehicleFeignService;

    @Autowired
    UserFeighService userFeighService;

    @Autowired
    DeviceOnlineFeignService deviceOnlineFeignService;

    @Override
    public VehicleDetailVo getVehDetail(String vin) {
        VehicleDetailVo vehicleDetailVo = new VehicleDetailVo();
        // 调用车辆服务，获取车辆信息
        RestResponse<VehicleDetailVo> restResponse = vehicleFeignService.getVehicleDetail(vin);
        if (RestResponseUtil.isSuccessResult(restResponse)) {
            vehicleDetailVo = JSON.parseObject(JSON.toJSONString(restResponse.getRespData()), VehicleDetailVo.class);
        }
        // 获取车主信息
        RestResponse<MgtUserVo> userResponse = userFeighService.getUserInfo(vin);
        if (RestResponseUtil.isSuccessResult(userResponse)) {
            MgtUserVo mgtUserVo = JSON.parseObject(JSON.toJSONString(userResponse.getRespData()), MgtUserVo.class);
            vehicleDetailVo.setUserVo(mgtUserVo);
        }
        // 获取绑定记录信息
        RestResponse<List<VehicleBindVo>> bindVoRestResponse = userFeighService.listBinding(vin);
        if (RestResponseUtil.isSuccessResult(bindVoRestResponse)) {
            List<VehicleBindVo> vehicleBindVos = bindVoRestResponse.getRespData();
            vehicleDetailVo.setVehicleBindVos(vehicleBindVos);
        }
        // 获取车辆Tbox版本信息
        ArrayList<VehicleDeviceVo> deviceVos = new ArrayList<>();
        RestResponse<List<VehicleDeviceVo>> tboxResponse = vehicleFeignService.queryDevice(vin, 1);
        if (RestResponseUtil.isSuccessResult(tboxResponse)) {
            deviceVos.addAll(tboxResponse.getRespData());
        }
        // 获取车辆ECU 版本信息
        RestResponse<List<VehicleDeviceVo>> ecuResponse = vehicleFeignService.queryDevice(vin, 2);
        if (RestResponseUtil.isSuccessResult(ecuResponse)) {
            deviceVos.addAll(ecuResponse.getRespData());
        }
        VehicleWarranty vehicleWarranty = new VehicleWarranty();
        VehicleLicenseVo vehicleLicenseVo = new VehicleLicenseVo();
        VehicleSalesVo vehicleSalesVo = new VehicleSalesVo();
        vehicleDetailVo.setVehicleLicenseVo(vehicleLicenseVo);
        vehicleDetailVo.setVehicleSalesVo(vehicleSalesVo);
        vehicleDetailVo.setVehicleWarranty(vehicleWarranty);
        return vehicleDetailVo;
    }

    @Override
    public IPage<VehicleVo> getPagedVehicles(QueryVehicleDto vehicleDto) {

        //车主信息过滤
        List<String> vins;
        if (!StringUtils.isEmpty(vehicleDto.getPhone()) || !StringUtils.isEmpty(vehicleDto.getUserName())
                || !StringUtils.isEmpty(vehicleDto.getIdCardNum())) {
            MgtUserVo mgtUserDto = new MgtUserVo();
            BeanUtils.copyProperties(vehicleDto, mgtUserDto);
            List<MgtUserVo> mgtUserVoList = Optional.ofNullable(userFeighService.getUserInfoList(mgtUserDto)).orElse(new ArrayList<>());
            if(mgtUserVoList == null || mgtUserVoList.isEmpty()) {
                return new Page<VehicleVo>();
            }
            vins = mgtUserVoList.stream().map(MgtUserVo::getVin).collect(Collectors.toList());
            vehicleDto.setVins(vins);
        }

        //分页查询
        IPage<VehicleVo> pos = Optional.ofNullable(vehicleFeignService.queryVehicles(vehicleDto).getRespData())
                .orElseThrow(() -> new AdamException("车辆列表查询失败，请稍后重试！"));

        List<VehicleVo> vehicles = org.springframework.util.CollectionUtils.isEmpty(pos.getRecords())
                ? new ArrayList<>() : pos.getRecords();

        if (CollectionUtils.isEmpty(vehicles)) {
            return pos;
        }

        //获取车辆VIN列表
        vins = vehicles.stream().map(VehicleVo::getVin).distinct().collect(Collectors.toList());
        //数据组装
        List<MgtUserVo> userInfoList = Optional.ofNullable(userFeighService.getUserInfoList(vins)).orElse(new ArrayList<>());
        vehicles.forEach(vehicle -> {
            //车主信息
            List<MgtUserVo> userVoList = userInfoList.stream().filter(mgtUserVo -> Objects.equals(vehicle.getVin(), mgtUserVo.getVin())).collect(Collectors.toList());
            if(!org.springframework.util.CollectionUtils.isEmpty(userVoList)) {
                String userName = userVoList.get(0).getUserName();
                String phone = userVoList.get(0).getPhone();
                vehicle.setUserName(DesensitizedUtils.desensitizedName(userName));
                vehicle.setPhone(DesensitizedUtils.desensitizedPhone(phone));
            }
            vehicle.setDrivingLicPlate(DesensitizedUtils.desensitizedLicensePlate(vehicle.getDrivingLicPlate()));

        });
        pos.setRecords(vehicles);
        return pos;
    }

    @Override
    public void editVehLabel(EditVehLabelDto labelDto) {
        RestResponse restResponse = vehicleFeignService.editVehLabel(labelDto);
        if (restResponse == null || !RespCode.SUCCESS.getValue().equals(restResponse.getRespCode())) {
            log.info("更新车辆 信息 {}", JSON.toJSONString(restResponse));
            throw new AdamException("更新车辆信息异常");
        }
    }

    @Override
    public Page<UserBindDetailVo> getUserBindVehicleInfo(VehicleDto vehicleDto) {
        List<UserBindDetailVo> userBindDetailVos;
        RestResponse<Page<UserBindDetailVo>> restResponse = userFeighService.listVehicle(vehicleDto);
        log.info("车辆绑定用户信息{}", JSON.toJSONString(restResponse));
        if (!RestResponseUtil.isSuccessResult(restResponse)) {
            return null;
        }
        Page<UserBindDetailVo> result = restResponse.getRespData();
        List<UserBindDetailVo> records = result.getRecords();
        List<String> vins = records.stream().map(UserBindDetailVo::getVin).collect(Collectors.toList());
        RestResponse<List<UserBindDetailVo>> vehicle = vehicleFeignService.getVehicles(vins);
        log.info("车辆绑定设备信息{}", JSON.toJSONString(vehicle));
        if (!RestResponseUtil.isSuccessResult(vehicle)) {
            return result;
        }
        userBindDetailVos = vehicle.getRespData();
        for (UserBindDetailVo record : records) {
            for (UserBindDetailVo userBindDetailVo : userBindDetailVos) {
                if (record.getVin().equals(userBindDetailVo.getVin())) {
                    BeanUtil.copyProperties(userBindDetailVo, record, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                }
            }
        }
        RestResponse onlineResponse = deviceOnlineFeignService.onlineStatusBatch(vins);
        log.info("车辆绑定上下线信息{}", JSON.toJSONString(onlineResponse));
        if (!RestResponseUtil.isSuccessResult(onlineResponse)) {
            return result;
        }
        Object respData = onlineResponse.getRespData();
        JSONArray resultArray = JSON.parseArray(JSON.toJSONString(respData));
        for (UserBindDetailVo userBindDetailVo : records) {
            for (Object o : resultArray) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(o));
                if (userBindDetailVo.getVin().equals(jsonObject.getString("vin"))) {
                    userBindDetailVo.setOnlineStatus(jsonObject.getBoolean("onlineStatus"));
                }
            }
        }
        return result;
    }
}
