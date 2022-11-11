package gaea.user.center.service.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gaea.user.center.service.common.SubjectTypeEnum;
import gaea.user.center.service.dao.RoleMapper;
import gaea.user.center.service.dao.SubjectPrivilegeMapper;
import gaea.user.center.service.models.domain.Role;
import gaea.user.center.service.models.domain.SubjectPrivilege;
import gaea.user.center.service.service.IRoleService;
/**
 * @ClassName: RoleServiceImpl
 * 角色信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class RoleServiceImpl implements IRoleService{

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private SubjectPrivilegeMapper subjectPrivilegeMapper;
	
	/**
	 * 分页角色信息查询
	 * 
	 * @param role
	 * @return
	 */
	public List<Role> queryRolePageList(Role role) {
		List<Role> listRole = roleMapper.queryRolePageList(role);
		for(Role r:listRole){
			r.setSubjectPrivilege(subjectPrivilegeMapper.querySubjectPrivilegeBySubjectId(r.getId()));
		}
		return listRole;
	}
	/**
	 * 角色信息查询
	 * 
	 * @param role
	 * @return
	 */
	public List<Role> queryRole(Role role) {
		List<Role> listRole = roleMapper.queryRole(role);
		for(Role r:listRole){
			r.setSubjectPrivilege(subjectPrivilegeMapper.querySubjectPrivilegeBySubjectId(r.getId()));
		}
		return listRole;
	}
	/**
	 * 根据组织ID查询角色信息
	 * 
	 * @param ogranizationId 组织ID
	 * @return
	 */
	public List<Role> queryRoleByOgranizationId(Integer ogranizationId) {
		List<Role> listRole = roleMapper.queryRoleByOgranizationId(ogranizationId);
		for(Role r:listRole){
			r.setSubjectPrivilege(subjectPrivilegeMapper.querySubjectPrivilegeBySubjectId(r.getId()));
		}
		return listRole;
	}
	/**
	 * 新增角色
	 * 
	 * @param role
	 * @return
	 */
	public Integer addRole(Role role) {
		Date createTime = new Date();
		String createBy = "zhangsan";
		role.setCreateTime(createTime);
		role.setUpdateTime(createTime);
		role.setCreateBy(createBy);
		role.setUpdateBy(createBy);
		role.setIsActive(1);
		Integer count = roleMapper.insertRole(role);
		//权限角色信息
		List<SubjectPrivilege> subjectPrivilege = role.getSubjectPrivilege();
		for(SubjectPrivilege sub:subjectPrivilege){
			sub.setCreateTime(createTime);
			sub.setUpdateTime(createTime);
			sub.setSubjectType(SubjectTypeEnum.ROLE.getKey());
			sub.setSubjectId(role.getId());
		}
		subjectPrivilegeMapper.batchInsertSubjectPrivilege(subjectPrivilege);
		return count;
	}
	
	/**
	 * 删除角色
	 * 
	 * @param role
	 * @return
	 */
	public Integer deleteRoleById(List<Integer> listIds){
		subjectPrivilegeMapper.deleteSubjectPrivilegeBySubjectId(listIds);
		return roleMapper.deleteRoleById(listIds);
	}
	
	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	public Integer updateRole(Role role) {
		Date createTime = new Date();
		String createBy = "zhangsan";
		//删除掉权限角色关系
		List<Integer> listIds = new ArrayList<Integer>();
		listIds.add(role.getId());
		subjectPrivilegeMapper.deleteSubjectPrivilegeBySubjectId(listIds);
		//权限角色信息
		List<SubjectPrivilege> subjectPrivilege = role.getSubjectPrivilege();
		for(SubjectPrivilege sub:subjectPrivilege){
			sub.setCreateTime(createTime);
			sub.setUpdateTime(createTime);
			sub.setSubjectType(SubjectTypeEnum.ROLE.getKey());
			sub.setSubjectId(role.getId());
		}
		subjectPrivilegeMapper.batchInsertSubjectPrivilege(subjectPrivilege);
		//更新角色信息
		role.setUpdateTime(createTime);
		role.setUpdateBy(createBy);
		return roleMapper.updateRole(role);
	}
}
