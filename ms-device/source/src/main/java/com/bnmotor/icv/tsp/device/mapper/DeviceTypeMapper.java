package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.device.model.entity.DeviceTypePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: DeviceTypeDo
 * @Description: 车型设备类型 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-12
 */

@Mapper
public interface DeviceTypeMapper extends BaseMapper<DeviceTypePo> {
    /**
     * 根据条件查询设备类型
     */
    Page<DeviceTypePo> selectAllByCondition(IPage page, @Param("searchKey") String searchKey);

    /**
     * 获取全量设备类型
     *
     * @return 设备列表
     */
    List<DeviceTypePo> selectAll();

    /**
     * 根据设备类型查询设备类型实体
     *
     * @param deviceType 设备类型
     * @return 设备类型实体
     */
    DeviceTypePo selectByDeviceType(@Param("deviceType") Integer deviceType);

    /**
     * 根据设备类型集合批量查询设备类型
     *
     * @param deviceTypes 设备类型集合
     * @return 设备类型
     */
    List<DeviceTypePo> selectByDeviceTypes(@Param("deviceTypes") List<Integer> deviceTypes);

    /**
     * 根据设备类型名称查询设备类型
     *
     * @param typeName 类型名称
     * @return 设备类型实体
     */
    DeviceTypePo selectByTypeName(@Param("typeName") String typeName);
}
