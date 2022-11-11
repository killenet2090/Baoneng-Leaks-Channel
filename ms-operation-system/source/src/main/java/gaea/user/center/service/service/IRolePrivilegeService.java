package gaea.user.center.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gaea.user.center.service.model.dto.RolePrivilege;
import gaea.user.center.service.model.entity.RolePrivilegePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: ISubjectPrivilegeService
 * 权限关系信息Service接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IRolePrivilegeService {

    /**
     * 根据角色ID查询权限角色关联关系信息
     * @param roleList
     * @return
     */
    List<RolePrivilege> queryPrivilegeListByRoleIds(@Param("list") List<String> roleList);
    /**
     * 批量插入权限角色关联关系信息
     *
     * @param rolePrivilegePos
     * @return
     */
    public boolean executeBatch(List<RolePrivilegePo> rolePrivilegePos);
    /**
     * 根据条件查询角色权限信息
     *
     * @param query
     * @return
     */
    List<RolePrivilege> queryRolePrivilegeByCondition(RolePrivilegePo query);
    //Integer queryRolePrivilegeByCondition(RolePrivilegePo query);
}
