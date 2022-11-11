package gaea.user.center.service.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import gaea.user.center.service.model.entity.UserVehiclePo;
import gaea.user.center.service.model.response.UserVehicleVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserVehicleVoMapper extends AdamMybatisMapMapper<UserVehiclePo, UserVehicleVo> {
    UserVehicleVoMapper INSTANCE = Mappers.getMapper(UserVehicleVoMapper.class);
}
