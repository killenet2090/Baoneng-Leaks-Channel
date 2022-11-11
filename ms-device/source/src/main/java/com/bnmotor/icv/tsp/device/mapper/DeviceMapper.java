package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.device.model.entity.DevicePo;
import com.bnmotor.icv.tsp.device.model.response.device.DeviceVo;
import com.bnmotor.icv.tsp.device.model.response.device.ModelDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: DevicePo
 * @Description: 设备基本信息 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-18
 */

@Mapper
public interface DeviceMapper extends AdamMapper<DevicePo> {
    /**
     * 分页查询设备信息
     */
    Page<DeviceVo> listAll(IPage page, @Param("searchKey") String searchKey, @Param("searchValue") String searchValue);

    /**
     * 根据deviceId查询设备列表
     *
     * @param deviceIds 设备列表集合
     * @return 设备列表
     */
    List<DevicePo> listByDeviceIds(@Param("deviceIds") List<String> deviceIds);

    /**
     * 根据配置ID获取零件设备列表
     *
     * @param deviceModels 设备型号
     */
    List<ModelDeviceVo> listDevicesByModel(@Param("deviceModels") List<String> deviceModels);

    /**
     * 根据起始条目查询设备列表
     *
     * @param fromId 开始条目
     * @param limit  每页条目
     * @return 设备列表
     */
    List<DevicePo> listByFromId(@Param("createTime") LocalDateTime createTime, @Param("fromId") Long fromId, @Param("limit") Integer limit);

    /**
     * 根据deviceId查询设备
     *
     * @param deviceId 设备id
     * @return 设备实体
     */
    DevicePo getByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 根据deviceId查询设备
     *
     * @param sn 设备生成序列号
     * @return 设备实体
     */
    DevicePo getNotBindingBySn(@Param("sn") String sn);

    /**
     * 根据iccid查询设备
     *
     * @param iccid sim卡iccid
     * @return 设备实体
     */
    DevicePo getByIccidId(@Param("iccid") String iccid);

    /**
     * 根据设备生产序列号获取设备列表
     * @param sn 生产序列号
     */
    List<DevicePo> listBySn(@Param("sn") String sn);
}
