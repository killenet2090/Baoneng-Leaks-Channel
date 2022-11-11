package gaea.user.center.service.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import gaea.user.center.service.model.entity.ProjectPo;
import gaea.user.center.service.model.request.ProjectQuery;
import gaea.user.center.service.model.response.ProjectVo;

import java.util.List;

/**
 * @ClassName: ProjectService
 * @Description: 项目信息接口
 * @author: jiangchangyuan1
 * @date: 2020/8/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IProjectService {
    /**
     * <pre>
     * 分页查询数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 数据集合
     */
    Page<ProjectVo> queryProjectPage(Pageable pageable, ProjectQuery query);

    /**
     * 新增项目信息
     * @param projectVo
     * @return
     * @throws AdamException
     */
    Long insert(ProjectVo projectVo);

    /**
     * 更新项目信息
     * @param projectVo
     * @return
     */
    ProjectPo updateProject(String id, ProjectVo projectVo);

    /**
     * 删除项目信息
     * @param id
     * @return
     */
    Long deleteProject(String id);

    /**
     * 查询项目信息列表
     * @param projectVo
     * @return
     */
    List<ProjectVo> queryList(ProjectVo projectVo);

    /**
     * 根据id查询项目信息
     * @param id
     * @return
     */
    ProjectVo queryById(Long id);

    /**
     * 根据用户id查询项目列表
     * @param userId
     * @return
     */
    List<ProjectVo> queryListByUserId(Long userId);

    /**
     * 批量保存项目拥有者列表
     * @param projectVo
     * @return
     */
    Integer saveBatchProjectUsers(ProjectVo projectVo);
}
