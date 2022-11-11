package gaea.user.center.service.service;

import java.util.List;

import gaea.user.center.service.models.domain.Role;

/**
 * @ClassName: IRoleService
 * 角色信息Service接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IRoleService {

	/**
	 * 分页角色信息查询
	 * 
	 * @param role
	 * @return
	 */
	public List<Role> queryRolePageList(Role role);
	/**
	 * 角色信息查询
	 * 
	 * @param role
	 * @return
	 */
	public List<Role> queryRole(Role role);
	
	/**
	 * 根据组织ID查询角色信息
	 * 
	 * @param ogranizationId 组织ID
	 * @return
	 */
	public List<Role> queryRoleByOgranizationId(Integer ogranizationId);
	
	/**
	 * 新增角色
	 * 
	 * @param role
	 * @return
	 */
	public Integer addRole(Role role);
	
	/**
	 * 删除角色
	 * 
	 * @param role
	 * @return
	 */
	public Integer deleteRoleById(List<Integer> listIds);
	
	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	public Integer updateRole(Role role);
}
