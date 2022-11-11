package com.bnmotor.icv.tsp.common.data.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoRelationshipPo;
import com.bnmotor.icv.tsp.common.data.model.dto.GeoRelationshipDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: GeoRelationshipMapper
 * @Description: 地理位置关联关系表 dao层
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Component
public interface GeoRelationshipMapper extends AdamMapper<GeoRelationshipPo> {

    /**
     * 查询列表
     * @param relationshipDto
     * @return
     */
    List<GeoRelationshipPo> findAll(@Param("query") GeoRelationshipDto relationshipDto);

    /**
     * 查询列表
     * @param relationshipDto
     * @return
     */
    GeoRelationshipPo selectOne(@Param("query") GeoRelationshipDto relationshipDto);
}
