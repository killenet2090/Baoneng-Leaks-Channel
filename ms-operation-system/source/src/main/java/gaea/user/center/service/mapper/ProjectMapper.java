package gaea.user.center.service.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.ProjectPo;
import gaea.user.center.service.model.request.ProjectQuery;
import gaea.user.center.service.model.response.ProjectVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: PrivilegeMapper
 * 项目信息Mapper接口类
 * @author: jiangchangyuan1
 * @date: 2020/8/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public interface ProjectMapper extends AdamMapper<ProjectPo> {
    /**
     * 分页查询项目信息
     * @param iPage
     * @param query
     * @return
     */
    Page<ProjectPo> queryProjectPage(IPage iPage, @Param("query") ProjectQuery query);

    /**
     * 根据项目id查询项目信息
     * @param id
     * @return
     */
    ProjectPo queryProjectById(Long id);

    /**
     * 根据项目code查询项目信息
     * @param code
     * @return
     */
    ProjectPo queryProjectByCode(String code);

    /**
     * 查询项目列表
     * @param query
     * @return
     */
    List<ProjectPo> queryProjectList(@Param("query") ProjectVo query);

    /**
     * 根据用户id查询项目列表
     * @param userId
     * @return
     */
    List<ProjectPo> queryListByUserId(Long userId);
}
