package gaea.user.center.service.service;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import gaea.user.center.service.model.dto.Role;
import gaea.user.center.service.model.request.RoleQuery;
import gaea.user.center.service.model.response.Resp;
import gaea.user.center.service.model.response.RoleResp;
import gaea.user.center.service.model.response.RoleVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * @param pageable
	 * @param roleQuery
	 * @return
	 */
	public Page<RoleVo> queryRolePageList(Pageable pageable, RoleQuery roleQuery);
	/**
	 * 角色信息查询
	 * 
	 * @param query
	 * @return
	 */
	public List<RoleVo> queryRole(RoleQuery query);
	/**
	 * 查询所有角色信息
	 *
	 * @param query
	 * @return
	 */
	public Map<String, Set> queryAllRole(RoleQuery query);
	
	/**
	 * 新增角色
	 * 
	 * @param role
	 * @return
	 */
	public Resp<Integer> addRole(Role role);
	
	/**
	 * 删除角色
	 *
	 * @param listIds
	 * @return
	 */
	public Integer deleteRoleById(List<Integer> listIds);
	
	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	public Resp<Integer> updateRole(Role role);
	/**
	 * 根据角色ID查询已经使用的角色信息
	 * 
	 * @param ids 角色ID
	 * @return
	 */
	public List<RoleResp> queryUsedRoleByRoleIds(List<Integer> ids);

	/**
	 * 根据角色ID查询已经使用的角色信息
	 *
	 * @param ids 角色ID
	 * @return
	 */
	public List<RoleResp> queryUsedRolesByRoleId(String ids);
	/**
	 * 查询角色数量
	 *
	 * @param name
	 * @param id
	 * @return
	 */
	public Integer queryRoleCountByName(String name, Integer id);
	/**
	 * 查询角色列表信息
	 *
	 * @return
	 */
	public List<RoleResp> queryRoleList();
	/**
	 * 根据用户ID查询角色信息
	 *
	 * @return
	 */
	public List<RoleResp> queryRoleListByUserId(Long userId);
}
