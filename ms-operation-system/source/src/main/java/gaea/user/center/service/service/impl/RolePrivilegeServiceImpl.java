package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gaea.user.center.service.mapper.RolePrivilegeMapper;
import gaea.user.center.service.model.dto.RolePrivilege;
import gaea.user.center.service.model.entity.RolePrivilegePo;
import gaea.user.center.service.service.IRolePrivilegeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SubjectPrivilegeServiceImpl
 * 权限关系信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class RolePrivilegeServiceImpl extends ServiceImpl<RolePrivilegeMapper, RolePrivilegePo> implements IRolePrivilegeService {

    private final RolePrivilegeMapper rolePrivilegeMapper;

    public RolePrivilegeServiceImpl(RolePrivilegeMapper rolePrivilegeMapper) {
        this.rolePrivilegeMapper = rolePrivilegeMapper;
    }

    /**
     * 根据角色ID查询权限角色关联关系信息
     * @param roleList
     * @return
     */
    public List<RolePrivilege> queryPrivilegeListByRoleIds(List<String> roleList) {
        return rolePrivilegeMapper.queryPrivilegeListByRoleIds(roleList);
    }
    /**
     * 批量插入权限角色关联关系信息
     *
     * @param rolePrivilegePos
     * @return
     */
    public boolean executeBatch(List<RolePrivilegePo> rolePrivilegePos) {
        return this.saveBatch(rolePrivilegePos);
    }
    /**
     * 根据条件查询角色权限信息
     *
     * @param query
     * @return
     */
    public List<RolePrivilege> queryRolePrivilegeByCondition(RolePrivilegePo query) {
        return rolePrivilegeMapper.queryRolePrivilegeByCondition(query);
    }
}
