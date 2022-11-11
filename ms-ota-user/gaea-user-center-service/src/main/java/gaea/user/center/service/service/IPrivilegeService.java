package gaea.user.center.service.service;

import java.util.List;

import gaea.user.center.service.models.domain.Privilege;


/**
 * @ClassName: IPrivilegeService
 * 权限信息Service接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IPrivilegeService {

	/**
	 * 分页查询权限信息
	 */
	public List<Privilege> queryPrivilegePageList(Privilege privilege);
	/**
	 * 查询权限信息
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
