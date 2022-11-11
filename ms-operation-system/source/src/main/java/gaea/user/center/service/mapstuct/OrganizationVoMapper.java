package gaea.user.center.service.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import gaea.user.center.service.model.entity.OrganizationPo;
import gaea.user.center.service.model.entity.UserPo;
import gaea.user.center.service.model.response.OrganizationVo;
import gaea.user.center.service.model.response.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationVoMapper extends AdamMybatisMapMapper<OrganizationPo, OrganizationVo> {
    OrganizationVoMapper INSTANCE = Mappers.getMapper(OrganizationVoMapper.class);
}
