package gaea.user.center.service.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import gaea.user.center.service.dao.PrivilegeMapper;
import gaea.user.center.service.models.domain.Privilege;
import gaea.user.center.service.service.IPrivilegeService;
/**
 * @ClassName: PrivilegeServiceImpl
 * 权限信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class PrivilegeServiceImpl implements IPrivilegeService{

	@Autowired
	private PrivilegeMapper privilegeMapper;
	/**
	 * 分页查询权限信息
	 */
	public List<Privilege> queryPrivilegePageList(Privilege privilege) {
		return privilegeMapper.queryPrivilegePageList(privilege);
	}
	/**
	 * 查询权限信息
	 */
	public List<Privilege> queryPrivilege(Privilege privilege) {
		return privilegeMapper.queryPrivilege(privilege);
	}
	
	/**
	 * 根据角色ID查询此角色下权限
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Privilege> queryPrivilegeByRoleId(Integer roleId) {
		return privilegeMapper.queryPrivilegeByRoleId(roleId);
	}
	
	/**
	 * 根据用户ID查询该用户的权限
	 * 
	 * @param userId
	 * @return
	 */
	public List<Privilege> queryPrivilegeByUserId(Integer userId) {
		return privilegeMapper.queryPrivilegeByUserId(userId);
	}
}
