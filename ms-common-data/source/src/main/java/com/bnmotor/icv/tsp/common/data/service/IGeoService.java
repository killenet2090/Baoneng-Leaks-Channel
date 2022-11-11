package com.bnmotor.icv.tsp.common.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoPo;
import com.bnmotor.icv.tsp.common.data.model.request.GeoQueryVo;
import com.bnmotor.icv.tsp.common.data.model.request.GeoReqVo;
import com.bnmotor.icv.tsp.common.data.model.response.GeoCityVo;
import com.bnmotor.icv.tsp.common.data.model.response.GeoVo;

import java.util.List;

/**
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 服务类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IGeoService extends IService<GeoPo> {

    /**
     * 获取地理位置树形结构信息
     * @param parentId
     * @return
     */
    List<GeoVo> getTreeList(Long parentId);

    /**
     * 获取地理位置树形结构信息
     * @param code
     * @return
     */
    List<GeoVo> getTreeList(String code);

    /**
     * 根据code查询地理位置信息详情
     * @param code
     * @return
     */
    GeoVo getByCode(String code);

    /**
     * 根据code查询地理位置信息详情
     * @param Keyword
     * @return
     */
    List<GeoCityVo> getByKeyword(String Keyword);

    /**
     * 查询GEO列表
     * @param code
     * @return
     */
    List<GeoVo> getByCode(List<String> code);

    /**
     * 查询地理位置信息列表
     * @param pageable
     * @param query
     * @return
     */
    Page<GeoVo> findAll(Pageable pageable, GeoQueryVo query);

    /**
     * 获取GEO最近一次更新时间
     * @return
     */
    Long getLastModified();

    /**
     * 更新GEO信息
     * @param id
     * @param geoReqVo
     */
    boolean update(Long id, GeoReqVo geoReqVo);
}
