package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.device.model.entity.DeviceModelInfoPo;
import com.bnmotor.icv.tsp.device.model.response.device.DeviceModelInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: DeviceModelInfoPo
 * @Description: 定义不同设备型号的相关信息 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-12
 */

@Mapper
public interface DeviceModelInfoMapper extends BaseMapper<DeviceModelInfoPo> {
    /**
     * 根据设备类型,关键字搜索设备列表
     *
     * @param page       分页请求参数
     * @param deviceType 设备类型
     * @return 设备分页列表
     */
    Page<DeviceModelInfoPo> selectPage(IPage page, @Param("searchKey") String serchKey, @Param("deviceType") Integer deviceType);

    /**
     * 根据设备类型,关键字搜索设备列表
     *
     * @param deviceType 设备类型
     * @return 设备分页列表
     */
    List<DeviceModelInfoPo> selectAll(@Param("deviceType") Integer deviceType);


    List<DeviceModelInfoVo> selectAllDeviceInfoVos();

    /**
     * 设备型号信息
     *
     * @param deviceModels 设备型号
     * @return 设备型号列表
     */
    List<DeviceModelInfoPo> selectAllByModel(@Param("deviceModels") List<String> deviceModels);

    /**
     * 根据设备类型和设备型号查询设备型号信息
     *
     * @param deviceType  设备类型
     * @param deviceModel 设备型号
     * @return 设备型号信息
     */
    DeviceModelInfoPo selectByDeviceTypeAndModel(@Param("deviceType") Integer deviceType, @Param("deviceModel") String deviceModel);
}
