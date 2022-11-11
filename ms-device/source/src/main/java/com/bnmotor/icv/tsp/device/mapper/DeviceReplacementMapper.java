package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.device.model.entity.DeviceReplacementPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleDevicePo;
import com.bnmotor.icv.tsp.device.model.response.device.ReplaceTimesVo;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.ReplacementTimesVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DeviceReplacementMapper
 * @ClassName: DeviceReplacementPo
 * @Description: 车辆ECU信息
 * 定义车辆装配的ECU，来源于车辆分类树ECU清单，或者后续的换件更新 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface DeviceReplacementMapper extends BaseMapper<DeviceReplacementPo> {
    /**
     * 根据车架号和设备类型查询设备换绑历史记录
     *
     * @param vin        车架号
     * @param deviceType 设备类型
     * @return 列表
     */
    List<DeviceReplacementPo> listReplacements(@Param("vin") String vin, @Param("deviceType") Integer deviceType);

    /**
     * 统计每种设备类型换件次数
     *
     * @param vin 车架号
     */
    List<ReplacementTimesVo> countReplacementTimes(@Param("vin") String vin);

    /**
     * 统计设备被更换次数
     */
    List<ReplaceTimesVo> countBindedRecords(@Param("deviceIds") List<String> deviceIds);

    /**
     * 根据设备id查询设备绑定列表
     *
     * @param page     分页参数
     * @param deviceId 设备id
     * @return 设备列表
     */
    Page<DeviceReplacementPo> selectPageBindedRecords(IPage page, @Param("deviceId") String deviceId);
}
