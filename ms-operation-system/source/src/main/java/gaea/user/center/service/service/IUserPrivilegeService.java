package gaea.user.center.service.service;

import gaea.user.center.service.model.dto.Privilege;
import gaea.user.center.service.model.dto.RolePrivilege;
import gaea.user.center.service.model.entity.UserPrivilegePo;

import java.util.List;

/**
 * @ClassName: IUserPrivilegeService
 * @Description: 用户权限(资源)接口
 * @author: jiangchangyuan1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IUserPrivilegeService {
    /**
     * 批量保存用户权限关系列表
     * @param userId 用户id
     * @param privilegePoList 权限列表
     * @return
     */
    Integer saveBatchUserPrivilege(Long userId, List<RolePrivilege> privilegePoList);

    /**
     * 根据权限id查询权限分配用户列表
     * @param id 权限id
     * @return
     */
    List<UserPrivilegePo> queryUserPrivilegeByPrivilegeId(Long id);
}
