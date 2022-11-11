package com.bnmotor.icv.tsp.common.data.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoPo;
import com.bnmotor.icv.tsp.common.data.model.request.GeoQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 dao层
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Component
public interface GeoMapper extends AdamMapper<GeoPo> {
    /**
     * 分页查询地理位置信息
     * @param page
     * @param query
     * @return
     */
    Page<GeoPo> findAll(IPage page, @Param("query") GeoQueryVo query);

    /**
     * 根据GEO编码查询地理位置信息
     * @param code
     * @return
     */
    GeoPo findByCode(@Param("code")String code);

    /**
     * 根据GEO编码查询地理位置信息
     * @param id
     * @return
     */
    GeoPo findById(@Param("id")Long id);

    /**
     * 根据关键字查询地理位置信息
     * @param keyword
     * @return
     */
    List<GeoPo> findByKeyword(@Param("keyword")String keyword);

    /**
     * 查询GEO列表
     * @param codes
     * @return
     */
    List<GeoPo> findByCodes(@Param("codes") List<String> codes);
}
