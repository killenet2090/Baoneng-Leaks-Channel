package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehModelDevicePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HP
 * @ClassName: VehModelDeviceDo
 * @Description: 车型对应零部件型号 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-12
 */

@Mapper
public interface VehModelDeviceMapper extends BaseMapper<VehModelDevicePo> {
    /**
     * 根据配置id查询零件型号
     */
    List<VehModelDevicePo> listByConfigId(@Param("configId") Long configId);

    /**
     * 根据零件类型和零件型号查询车型零部件型号
     *
     * @param deviceType  设备类型
     * @param deviceModel 设备型号
     * @return 车型对应的零部件型号
     */
    VehModelDevicePo selectByDeviceTypeAndModel(@Param("deviceType") Integer deviceType, @Param("deviceModel") String deviceModel);

    /**
     * 根据起始位置查询车型零部件
     *
     * @param fromId 起始条目
     * @param limit  每次查询条目
     * @return 列表
     */
    List<VehModelDevicePo> listByFromId(@Param("fromId") Long fromId, @Param("limit") Integer limit);
}
