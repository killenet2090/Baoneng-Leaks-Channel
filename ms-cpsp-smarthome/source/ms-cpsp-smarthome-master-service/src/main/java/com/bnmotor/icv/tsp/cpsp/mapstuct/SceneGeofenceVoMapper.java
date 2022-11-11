package com.bnmotor.icv.tsp.cpsp.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import com.bnmotor.icv.tsp.cpsp.domain.entity.SceneGeofencePo;
import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SceneGeofenceVoMapper extends AdamMybatisMapMapper<SceneGeofencePo, SceneGeofenceVo> {
    SceneGeofenceVoMapper INSTANCE = Mappers.getMapper(SceneGeofenceVoMapper.class);
}
