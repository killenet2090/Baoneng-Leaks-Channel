package com.bnmotor.icv.tsp.common.data.mapstuct;

import com.bnmotor.icv.adam.core.mapstruct.AdamMapMapper;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoPo;
import com.bnmotor.icv.tsp.common.data.model.request.GeoReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName: GeoReqVoMapMapper
 * @Description:
 * @author: zhangjianghua1
 * @date: 2020/7/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface GeoReqVoMapMapper extends AdamMapMapper<GeoPo, GeoReqVo> {
    GeoReqVoMapMapper INSTANCE = Mappers.getMapper(GeoReqVoMapMapper.class);
}
