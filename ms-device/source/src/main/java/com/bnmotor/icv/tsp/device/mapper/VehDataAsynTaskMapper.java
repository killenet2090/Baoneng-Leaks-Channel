package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehDataAsynTaskPo
 * @Description: dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-11-18
 */
@Mapper
public interface VehDataAsynTaskMapper extends BaseMapper<VehDataAsynTaskPo> {
    /**
     * 查询已经被删除但还没被执行的
     *
     * @param limit 查询条目
     * @return 集合
     */
    List<VehDataAsynTaskPo> listNoExecute(@Param("taskType") Integer taskType, @Param("limit") Integer limit, @Param("delFlag") Integer delFlag);
}
