package com.bnmotor.icv.tsp.common.data.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.adam.data.mysql.utils.PageUtil;
import com.bnmotor.icv.adam.data.redis.RedisProvider;
import com.bnmotor.icv.adam.data.redis.StringRedisProvider;
import com.bnmotor.icv.tsp.common.data.common.Constants;
import com.bnmotor.icv.tsp.common.data.mapstuct.GeoReqVoMapMapper;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoPo;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoRelationshipPo;
import com.bnmotor.icv.tsp.common.data.model.dto.GeoRelationshipDto;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoTypePo;
import com.bnmotor.icv.tsp.common.data.model.request.GeoQueryVo;
import com.bnmotor.icv.tsp.common.data.model.request.GeoReqVo;
import com.bnmotor.icv.tsp.common.data.model.response.GeoCityVo;
import com.bnmotor.icv.tsp.common.data.model.response.GeoVo;
import com.bnmotor.icv.tsp.common.data.mapper.GeoMapper;
import com.bnmotor.icv.tsp.common.data.mapper.GeoRelationshipMapper;
import com.bnmotor.icv.tsp.common.data.mapstuct.GeoVoMapMapper;
import com.bnmotor.icv.tsp.common.data.service.IGeoService;
import com.bnmotor.icv.tsp.common.data.service.IGeoTypeService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author zhangjianghua1
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-10
 */

@Service
public class GeoServiceImpl extends ServiceImpl<GeoMapper, GeoPo> implements IGeoService {
    private final IGeoTypeService geoTypeService;
    private final GeoRelationshipMapper relationshipMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProvider redisProvider;
    private final Cache<String, Object> caffeineCache;

    public GeoServiceImpl(IGeoTypeService geoTypeService, GeoRelationshipMapper relationshipMapper,
                          RedisProvider redisProvider,
                          RedisTemplate<String, Object> redisTemplate, Cache<String, Object> caffeineCache) {
        this.geoTypeService = geoTypeService;
        this.relationshipMapper = relationshipMapper;
        this.redisTemplate = redisTemplate;
        this.redisProvider = redisProvider;
        this.caffeineCache = caffeineCache;
    }

    @Override
    public List<GeoVo> getTreeList(Long parentId) {
        if (null == parentId || parentId <= 0) {
            return new ArrayList<>();
        }
        GeoRelationshipDto relationshipDto = new GeoRelationshipDto();
        relationshipDto.setAncestor(parentId);
        List<Long> descendants = relationshipMapper.findAll(relationshipDto)
                .stream().map(GeoRelationshipPo::getDescendant).collect(toList());
        List<GeoPo> geoPoList = baseMapper.selectBatchIds(descendants);
        return generatorGeoTreeList(parentId, geoPoList);
    }

    @Override
    public List<GeoVo> getTreeList(String code) {
        GeoPo geo = baseMapper.findByCode(code);
        if (null == geo) {
            return new ArrayList<>();
        }
        return getTreeList(geo.getId());
    }

    @Override
    public GeoVo getByCode(String code) {
        GeoPo geo = baseMapper.findByCode(code);
        if (null != geo) {
            GeoRelationshipDto relationshipDto = new GeoRelationshipDto();
            relationshipDto.setDescendant(geo.getId());
            List<String> ancestors = relationshipMapper.findAll(relationshipDto)
                    .stream()
                    .sorted(Comparator.comparing(GeoRelationshipPo::getDistance))
                    .map(ancestor -> String.valueOf(ancestor.getAncestor()))
                    .collect(toList());
            GeoVo geoVo = GeoVoMapMapper.INSTANCE.map(geo);
            geoVo.setPath(ancestors.toArray(new String[ancestors.size()]));
            return geoVo;
        }
        return null;
    }

    public GeoCityVo getGeoCityVo(GeoCityVo vo, Long parentId, Long descendant) {
        if (null == parentId || parentId <= 0) {
            return vo;
        }
        GeoPo ancestorGeoPo = baseMapper.findById(parentId);
        GeoPo descendantGeoPo = baseMapper.findById(descendant);
        vo.setProvince(ancestorGeoPo.getName());
        vo.setDistrict(descendantGeoPo.getName());
        return vo;
    }

    @Override
    public List<GeoCityVo> getByKeyword(String Keyword) {
        List<GeoPo> geo = baseMapper.findByKeyword(Keyword);
        List<GeoCityVo> geoCityVos = new ArrayList<>();
        geo.forEach(geoPo -> {
            GeoCityVo vo = new GeoCityVo();
            vo.setAdCode(geoPo.getCode());
            GeoRelationshipDto relationshipDto = new GeoRelationshipDto();
            relationshipDto.setDescendant(geoPo.getId());
            relationshipDto.setAncestor(geoPo.getParentId());
            GeoRelationshipPo ancestor = relationshipMapper.selectOne(relationshipDto);
            GeoPo ancestorGeoPo = baseMapper.findById(ancestor.getAncestor());
            //省/直辖市
            if(geoPo.getTypeId().equals(Constants.GEO_TYPE_PROVINCE_ID) || geoPo.getTypeId().equals(Constants.GEO_TYPE_MUNICIPALITY_ID)){
                vo.setProvince(geoPo.getName());
            }else if(geoPo.getTypeId().equals(Constants.GEO_TYPE_CITY_ID)){
                //市
                vo.setCity(geoPo.getName());
                vo.setProvince(ancestorGeoPo.getName());
            }else if(geoPo.getTypeId().equals(Constants.GEO_TYPE_DISTRICT_ID)){
                //县区
                vo.setDistrict(geoPo.getName());
                vo.setCity(ancestorGeoPo.getName());
                GeoPo parentGeo = baseMapper.findById(ancestorGeoPo.getParentId());
                vo.setProvince(parentGeo.getName());
            }
            geoCityVos.add(vo);
        });
        return geoCityVos;
    }

    @Override
    public List<GeoVo> getByCode(List<String> code) {
        List<GeoPo> getList = baseMapper.findByCodes(code);
        return GeoVoMapMapper.INSTANCE.map(getList);
    }

    @Override
    public Page<GeoVo> findAll(Pageable pageable, GeoQueryVo query) {
        IPage iPage = PageUtil.map(pageable);
        Page<GeoVo> page = GeoVoMapMapper.INSTANCE.map(baseMapper.findAll(iPage, query));
        return page;
    }

    @Override
    public Long getLastModified() {
        Long lastModified = (Long) caffeineCache.getIfPresent(Constants.GEO_LAST_MODIFIED_KEY);
        if (null == lastModified) {
            lastModified = redisProvider.getObject(Constants.GEO_LAST_MODIFIED_KEY, Long.class);
            if (null == lastModified) {
                lastModified = System.currentTimeMillis();
                redisTemplate.opsForValue().set(Constants.GEO_LAST_MODIFIED_KEY, lastModified);
            }
            caffeineCache.put(Constants.GEO_LAST_MODIFIED_KEY, lastModified);
        }
        return lastModified;
    }

    @Override
    public boolean update(Long id, GeoReqVo geoReqVo) {
        GeoPo geoPo = GeoReqVoMapMapper.INSTANCE.revertMap(geoReqVo);
        geoPo.setId(id);
        if (null != geoPo.getTypeId()) {
            GeoTypePo geoType = geoTypeService.getById(geoPo.getId());
            if (null == geoType) {
                geoPo.setTypeId(null);
                geoPo.setType(null);
            } else {
                geoPo.setType(geoType.getName());
            }
        }
        boolean result = this.updateById(geoPo);
        if (result) {
            Long lastModified = System.currentTimeMillis();
            caffeineCache.put(Constants.GEO_LAST_MODIFIED_KEY, lastModified);
            redisTemplate.opsForValue().set(Constants.GEO_LAST_MODIFIED_KEY, lastModified);
        }
        return result;
    }

    /**
     * 构造树形结构List
     *
     * @param rootId
     * @param geoPoList
     * @return
     */
    private List<GeoVo> generatorGeoTreeList(Long rootId, List<GeoPo> geoPoList) {
        List<GeoVo> geoVoList = geoPoList.stream()
                .filter(geo -> Objects.equals(rootId, geo.getParentId()))
                .map(geo -> {
                    GeoVo geoVo = GeoVoMapMapper.INSTANCE.map(geo);
                    if (Objects.equals(Constants.GEO_TYPE_MUNICIPALITY_ID, geo.getTypeId()) || Objects.equals(Constants.GEO_TYPE_SPECIAL_REGION_ID, geo.getTypeId())) {
                        GeoVo child = GeoVoMapMapper.INSTANCE.map(geo);
                        child.setChildren(getChildren(geo, geoPoList));
                        geoVo.getChildren().add(child);
                    } else {
                        geoVo.setChildren(getChildren(geo, geoPoList));
                    }
                    return geoVo;
                })
                .sorted(Comparator.comparing(GeoVo::getFirstLetter))
                .collect(toList());
        return geoVoList;
    }

    /**
     * 递归构造子集
     *
     * @param root
     * @param all
     * @return
     */
    private List<GeoVo> getChildren(GeoPo root, List<GeoPo> all) {
        List<GeoVo> children = all.stream()
                .filter(geo -> Objects.equals(root.getId(), geo.getParentId()))
                .map(geo -> {
                    GeoVo geoVo = GeoVoMapMapper.INSTANCE.map(geo);
                    geoVo.setChildren(getChildren(geo, all));
                    return geoVo;
                }).sorted(Comparator.comparing(GeoVo::getFirstLetter))
                .collect(toList());
        return children;
    }
}
