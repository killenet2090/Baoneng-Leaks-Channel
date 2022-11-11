package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleTagPo;
import com.bnmotor.icv.tsp.device.model.response.tag.VehicleTagVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleLabelRelationMapper
 * @Description: 车辆标签关系维护 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleTagMapper extends AdamMapper<VehicleTagPo> {
    /**
     * 根据vehId和labelId删除对应条件标签
     *
     * @param vin 车辆id
     * @return 影响条目
     */
    int deleteByVin(@Param("vin") String vin);

    /**
     * 根据车架号和分组id获取车辆分组对应的标签
     *
     * @param vins       车架号
     * @param categoryId 分组id
     * @return 分组对应的标签
     */
    List<VehicleTagPo> listByVinsAndCategoryId(@Param("vins") List<String> vins, @Param("categoryId") Long categoryId);

    List<VehicleTagVo> getVehicleTagsByCat(@Param("categoryId") String categoryId, @Param("vins") List<String> vins, @Param("tagIds") List<String> tagIds);

    /**
     * 根据标签id查询车辆标签列表
     *
     * @param from  起始条目
     * @param tagId 标签id
     * @param limit 查询条目
     * @return 车辆标签集合
     */
    List<VehicleTagPo> listByTagId(@Param("from") Long from, @Param("tagId") Long tagId, @Param("limit") Integer limit);

    /**
     * 查询车辆标签
     *
     * @param vin 车辆标签
     * @return 车辆标签集合
     */
    List<VehicleTagPo> listByVin(@Param("vin") String vin);
}
