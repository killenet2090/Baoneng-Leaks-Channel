package gaea.user.center.service.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import gaea.user.center.service.model.entity.RolePo;
import gaea.user.center.service.model.response.RoleVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleVoMapper extends AdamMybatisMapMapper<RolePo, RoleVo> {
    RoleVoMapper INSTANCE = Mappers.getMapper(RoleVoMapper.class);

}
