package gaea.user.center.service.service;

import gaea.user.center.service.model.entity.UserProjectPo;

import java.util.List;

/**
 * @ClassName: IUserProjectService
 * @Description: 用户项目接口类
 * @author: jiangchangyuan1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IUserProjectService {
    /**
     * 批量保存某用户下项目列表
     * @param userId
     * @param projectIds
     * @return
     */
    Integer saveBatchUserProjects(Long userId, String projectIds);

    /**
     * 批量保存某项目下管理者列表
     * @param projectId
     * @param userIds
     * @return
     */
    Integer saveBatchProjectUsers(String projectId, String userIds);

    /**
     * 根据项目id查询项目分配列表
     * @param id
     * @return
     */
    List<UserProjectPo> queryUserProjectListByProjectId(Long id);
}
