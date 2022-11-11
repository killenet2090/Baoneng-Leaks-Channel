package gaea.user.center.service.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import gaea.user.center.service.model.entity.PrivilegePo;
import gaea.user.center.service.model.entity.PrivilegeTypePo;
import gaea.user.center.service.model.response.PrivilegeTypeVo;
import gaea.user.center.service.model.response.PrivilegeVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PrivilegeTypeVoMapper extends AdamMybatisMapMapper<PrivilegeTypePo, PrivilegeTypeVo> {
    PrivilegeTypeVoMapper INSTANCE = Mappers.getMapper(PrivilegeTypeVoMapper.class);
}
