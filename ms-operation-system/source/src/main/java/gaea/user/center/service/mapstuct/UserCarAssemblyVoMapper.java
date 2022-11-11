package gaea.user.center.service.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import gaea.user.center.service.model.entity.UserCarAssemblyPo;
import gaea.user.center.service.model.response.UserCarAssemblyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserCarAssemblyVoMapper extends AdamMybatisMapMapper<UserCarAssemblyPo, UserCarAssemblyVO> {
    UserCarAssemblyVoMapper INSTANCE = Mappers.getMapper(UserCarAssemblyVoMapper.class);
}
