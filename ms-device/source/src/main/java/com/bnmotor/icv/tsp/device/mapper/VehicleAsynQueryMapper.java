package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleAsynQueryPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleAsynQueryMapper
 * @Description: 车辆异步查询数据库实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-10-24
 */
@Mapper
public interface VehicleAsynQueryMapper extends AdamMapper<VehicleAsynQueryPo> {
    /**
     * 查询已经被删除但还没被执行的
     *
     * @param limit 查询条目
     * @return 集合
     */
    List<VehicleAsynQueryPo> listNoExecute(@Param("limit") Integer limit, @Param("delFlag") Integer delFlag);

    /**
     * 根据uid更新任务
     *
     * @param uid      用户uid
     * @param updateBy 更新人
     * @return 影响条目
     */
    int updateByUid(@Param("uid") Long uid, @Param("updateBy") String updateBy);
}
