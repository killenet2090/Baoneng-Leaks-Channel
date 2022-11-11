package gaea.user.center.service.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import gaea.user.center.service.model.response.UserPageVo;
import gaea.user.center.service.model.response.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserVoMapper extends AdamMybatisMapMapper<UserPageVo, UserVo> {
    UserVoMapper INSTANCE = Mappers.getMapper(UserVoMapper.class);
}
