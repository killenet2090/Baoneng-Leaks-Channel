package gaea.user.center.service.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.dto.Role;
import gaea.user.center.service.model.dto.UserRoleRel;
import gaea.user.center.service.model.entity.RolePo;
import gaea.user.center.service.model.request.RoleQuery;
import gaea.user.center.service.model.response.RoleResp;
import gaea.user.center.service.model.response.RoleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: RoleMapper
 * 角色信息mapper类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public interface RoleMapper extends AdamMapper<RolePo> {
	/**
	 * 按条件查询总记录数
	 * 
	 * @param role
	 * @return
	 */
    int queryRoleCount(Role role);
	/**
	 * 查询同名角色记录数
	 * 
	 * @param role
	 * @return
	 */
    int queryRoleCountByName(Role role);

	/**
	 * 分页角色信息查询
	 * @param iPage
	 * @param roleQuery
	 * @return
	 */
	Page<RoleVo> queryRolePageList(IPage iPage, @Param("query") RoleQuery roleQuery);
	
	/**
	 * 角色信息查询
	 * 
	 * @param query
	 * @return
	 */
	List<RolePo> queryRole(RoleQuery query);
	
//	/**
//	 * 根据组织ID查询角色信息
//	 *
//	 * @param ogranizationId 组织ID
//	 * @return
//	 */
//	public List<RoleResp> queryRoleByOgranizationId(Integer ogranizationId);
	
	/**
	 * 查询所有角色信息
	 * 
	 * @param delFlag 是否删除
	 * @return
	 */
	List<RoleResp> queryAllRole(Integer delFlag);
	
	/**
	 * 新增角色
	 * 
	 * @param role
	 * @return
	 */
	Integer insertRole(RolePo role);
	
	/**
	 * 删除角色
	 * 
	 * @param listIds 角色ID集合
	 * @return
	 */
	Integer deleteRoleById(List<Integer> listIds);
	
	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	Integer updateRole(RolePo role);

	/**
	 * 新增用户角色
	 * @param userRoleRel
	 * @return
	 */
	Integer insertUserRelRole(UserRoleRel userRoleRel);

	/**
	 * 删除角色关联
	 * @param userRoleRel
	 * @return
	 */
	Integer deleteUserRelRoleByUserId(UserRoleRel userRoleRel);
	/**
	 * 根据用户ID删除其下的所有角色
	 * 
	 * @param userId
	 * @return
	 */
	List<Long> queryRoleIdsByUserId(Long userId);
	/**
	 * 根据ids更新角色
	 * 
	 * @param ids 角色ID
	 * @return
	 */
	Integer batchUpdateRoleById(List<Integer> ids);
	
	/**
	 * 根据角色ID查询已经使用的角色信息
	 * 
	 * @param ids 角色ID
	 * @return
	 */
	List<RoleResp> queryUsedRoleByRoleIds(List<Integer> ids);

	/**
	 * 根据角色ID查询已经使用的角色信息
	 *
	 * @param id 角色ID
	 * @return
	 */
	List<RoleResp> queryUsedRolesByRoleId(Long id);
	/**
	 * 根据ID查询角色
	 *
	 * @param id
	 * @return
	 */
	public RolePo queryRoleById(Long id);
	/**
	 * 根据用户ID查询角色信息
	 *
	 * @return
	 */
	public List<RoleResp> queryRoleListByUserId(Long userId);
}
