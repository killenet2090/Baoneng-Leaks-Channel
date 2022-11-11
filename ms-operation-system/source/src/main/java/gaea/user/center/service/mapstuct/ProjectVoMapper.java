package gaea.user.center.service.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import gaea.user.center.service.model.entity.ProjectPo;
import gaea.user.center.service.model.entity.UserPo;
import gaea.user.center.service.model.response.ProjectVo;
import gaea.user.center.service.model.response.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectVoMapper extends AdamMybatisMapMapper<ProjectPo, ProjectVo> {
    ProjectVoMapper INSTANCE = Mappers.getMapper(ProjectVoMapper.class);
}
