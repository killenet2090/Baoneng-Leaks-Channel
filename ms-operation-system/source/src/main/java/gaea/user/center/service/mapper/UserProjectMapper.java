package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.dto.UserProjectRel;
import gaea.user.center.service.model.dto.UserRoleRel;
import gaea.user.center.service.model.entity.UserProjectPo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: UserProjectMapper
 * @Description: 用户项目中间表Mapper类
 * @author: jiangchangyuan1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public interface UserProjectMapper extends AdamMapper<UserProjectPo> {
    /**
     * 根据用户id批量删除原有项目信息
     * @param userId
     */
    void deleteBatchByUserId(Long userId);

    /**
     * 根据项目id批量删除原有管理者信息
     * @param projectId
     */
    void deleteBatchByProjectId(String projectId);

    /**
     * 根据项目id查询项目的分配列表
     * @param id
     * @return
     */
    List<UserProjectPo> queryUserProjectListByProjectId(Long id);

    /**
     * 新增用户项目
     * @param userProjectRel
     * @return
     */
    Integer insertUserRelProject(UserProjectRel userProjectRel);
    /**
     * 删除项目关联
     * @param userProjectRel
     * @return
     */
    Integer deleteUserRelProjectByUserId(UserProjectRel userProjectRel);
    /**
     * 根据用户id查询项目的分配列表
     * @param useId
     * @return
     */
    List<UserProjectPo> queryUserProjectListByUserId(Long useId);
}
