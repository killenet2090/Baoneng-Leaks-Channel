package gaea.user.center.service.dao;

import java.util.List;

import gaea.user.center.service.models.domain.Privilege;

/**
 * @ClassName: PrivilegeMapper
 * 权限信息Mapper接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface PrivilegeMapper {

	/**
	 * 分页查询权限信息
	 * 
	 * @param privilege
	 * @return
	 */
	public List<Privilege> queryPrivilegePageList(Privilege privilege);
	
	/**
	 * 根据条件查询权限信息
	 * 
	 * @param privilege
	 * @return
	 */
	public List<Privilege> queryPrivilege(Privilege privilege);
	
	/**
	 * 根据角色ID查询此角色下权限
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Privilege> queryPrivilegeByRoleId(Integer roleId);
	
	/**
	 * 根据用户ID查询该用户的权限
	 * 
	 * @param userId
	 * @return
	 */
	public List<Privilege> queryPrivilegeByUserId(Integer userId);
}
