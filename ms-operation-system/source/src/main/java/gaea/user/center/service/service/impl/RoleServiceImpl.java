package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.adam.data.mysql.utils.PageUtil;
import gaea.user.center.service.common.CurrentUser;
import gaea.user.center.service.common.CurrentUserUtils;
import gaea.user.center.service.common.SubjectTypeEnum;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.mapper.OrganizationMapper;
import gaea.user.center.service.mapper.RoleMapper;
import gaea.user.center.service.mapper.RolePrivilegeMapper;
import gaea.user.center.service.mapstuct.RoleVoMapper;
import gaea.user.center.service.model.dto.Role;
import gaea.user.center.service.model.dto.RolePrivilege;
import gaea.user.center.service.model.dto.UserRoleRel;
import gaea.user.center.service.model.entity.RolePo;
import gaea.user.center.service.model.entity.RolePrivilegePo;
import gaea.user.center.service.model.request.RoleQuery;
import gaea.user.center.service.model.response.Resp;
import gaea.user.center.service.model.response.RoleResp;
import gaea.user.center.service.model.response.RoleVo;
import gaea.user.center.service.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName: RoleServiceImpl
 * 角色信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePo> implements IRoleService{

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private OrganizationMapper organizationMapper;
	
	@Autowired
	private RolePrivilegeMapper rolePrivilegeMapper;

	/**
	 * 分页角色信息查询
	 * @param pageable 分页属性
	 * @param roleQuery 角色查询参数
	 * @return
	 */
	@Override
	public Page<RoleVo> queryRolePageList(Pageable pageable, RoleQuery roleQuery) {
		IPage iPage = PageUtil.map(pageable);
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<RoleVo> listRolePage = roleMapper.queryRolePageList(iPage,roleQuery);
		Page<RoleVo> page = new Page();
		page.setCurrent(Long.valueOf(listRolePage.getCurrent()).intValue());
		page.setPageSize(Long.valueOf(listRolePage.getSize()).intValue());
		page.setTotal(listRolePage.getTotal());
		if (page.getTotal() == 0L) {
			page.setPages(0L);
		} else {
			Long pages = page.getTotal() / (long)page.getPageSize();
			page.setPages(page.getTotal() % (long)page.getPageSize() == 0L ? (long)pages.intValue() : (long)(pages.intValue() + 1));
		}
		page.setList(listRolePage.getRecords());
		return page;
	}
	/**
	 * 角色信息查询
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public List<RoleVo> queryRole(RoleQuery query) {
		List<RolePo> listRole = roleMapper.queryRole(query);
		List<RoleVo> result = RoleVoMapper.INSTANCE.map(listRole);
		for(RoleVo role:result){
			role.setSelectAllPrivilege(rolePrivilegeMapper.queryPrivilegeIdByRoleId(Integer.valueOf(role.getId()),1));
			role.setSelectHalfPrivilege(rolePrivilegeMapper.queryPrivilegeIdByRoleId(Integer.valueOf(role.getId()),0));
		}
		return result;
	}
	/**
	 * 查询所有角色信息
	 *
	 * @param query
	 * @return
	 */
	@Override
	public Map<String,Set> queryAllRole(RoleQuery query) {
		List<RolePo> listRole = roleMapper.queryRole(query);
		List<RoleVo> roleVos = RoleVoMapper.INSTANCE.map(listRole);

		Map<String,Set> result = new HashMap<>();
		Set<String> roleNames = new HashSet();
		Set<String> creaters = new HashSet();
		result.put("roleNames",roleNames);
		result.put("creaters",creaters);
		for(RoleVo role:roleVos){
			roleNames.add(role.getName());
			creaters.add(role.getCreateBy());
		}
		return result;
	}
	/**
	 * 查询角色列表信息
	 *
	 * @return
	 */
	public List<RoleResp> queryRoleList() {
		return roleMapper.queryAllRole(0);
	}
	/**
	 * 根据组织ID查询角色信息
	 * 
	 * @param ogranizationId 组织ID
	 * @return
	 */
//	@Override
//	public List<RoleResp> queryRoleByOgranizationId(Integer ogranizationId) {
//		List<RoleResp> listRole = new ArrayList<RoleResp>();
//		//查询组织信息
//		OrganizationPo organization = organizationMapper.queryOrganizationById(ogranizationId);
//		if(null == organization){
//			return listRole;
//		}
//		if(organization.getLevel().equals(1)){
//			listRole = roleMapper.queryAllRole(1);
//		}else{
//			listRole = roleMapper.queryRoleByOgranizationId(ogranizationId);
//		}
	//注：此处原本已注释
////		for(Role r:listRole){
////			r.setSubjectPrivilege(subjectPrivilegeMapper.querySubjectPrivilegeBySubjectId(r.getId()));
////		}
//		return listRole;
//	}
	/**
	 * 新增角色
	 * 
	 * @param role
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Resp<Integer> addRole(Role role) throws AdamException{
		//校验是否重名
		if(!StringUtils.isEmpty(role.getName())){
			Role param = new Role();
			param.setName(role.getName());
			Integer count = roleMapper.queryRoleCountByName(param);
			if(count>0){
				throw new AdamException(BusinessStatusEnum.ROLE_DUPLICATE_NAME.getCode(),BusinessStatusEnum.ROLE_DUPLICATE_NAME.getDescription());
			}
		}
		//新增角色
		Date createTime = new Date();
		String createBy = "";
		if(null!= CurrentUserUtils.getCurrentUser()){
			createBy = CurrentUserUtils.getCurrentUser().getUserName();
		}else{
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}
		RolePo rolePo = new RolePo();
		rolePo.setName(role.getName());
		rolePo.setRemark(role.getRemark());
		rolePo.setCreateTime(createTime);
		rolePo.setUpdateTime(createTime);
		rolePo.setCreateBy(createBy);
		rolePo.setUpdateBy(createBy);
		rolePo.setDelFlag(0);

		Integer count = roleMapper.insertRole(rolePo);
		List<RolePrivilegePo> rolePrivilege = new ArrayList<>();
		//全选权限
		List<Integer> selectAllId = role.getSelectAllId();
		for(Integer id:selectAllId){
			RolePrivilegePo rpp = new RolePrivilegePo();
			rpp.setSubjectType(SubjectTypeEnum.ROLE.getKey());
			rpp.setRoleId(rolePo.getId());
			rpp.setPrivilegeId(id);
			rpp.setSelectStatus(1);
			rolePrivilege.add(rpp);
		}
		//半选权限
		List<Integer> selectHalfId = role.getSelectHalfId();
		for(Integer id:selectHalfId){
			RolePrivilegePo rpp = new RolePrivilegePo();
			rpp.setSubjectType(SubjectTypeEnum.ROLE.getKey());
			rpp.setRoleId(rolePo.getId());
			rpp.setPrivilegeId(id);
			rpp.setSelectStatus(0);
			rolePrivilege.add(rpp);
		}
		if(rolePrivilege.isEmpty()){
			throw new AdamException(BusinessStatusEnum.RESOURCE_NOT_FOUND.getCode(),BusinessStatusEnum.RESOURCE_NOT_FOUND.getDescription());
		}
		rolePrivilegeMapper.batchInsertSubjectPrivilege(rolePrivilege);
		return new Resp<Integer>(count);
	}
	/**
	 * 删除角色
	 * @param listIds
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteRoleById(List<Integer> listIds){
		Integer result = 0;
		//逻辑删除
		for(Integer roleId : listIds){
			//判断该角色是否绑定了用户
			List<RoleResp> roleRespList = queryUsedRolesByRoleId(roleId.toString());
			if(null != roleRespList && roleRespList.size() > 0){
				throw new AdamException(BusinessStatusEnum.ROLE_IS_USING.getCode(),BusinessStatusEnum.ROLE_IS_USING.getDescription());
			}
			rolePrivilegeMapper.deleteSubjectPrivilegeByRoleId(listIds);
			result = roleMapper.batchUpdateRoleById(listIds);
		}

		return result;
	}
	
	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Resp<Integer> updateRole(Role role) throws AdamException{
		if(!StringUtils.isEmpty(role.getName())){
			Role param = new Role();
			param.setName(role.getName());
			param.setId(role.getId());
			Integer count = roleMapper.queryRoleCountByName(param);
			if(count>0){
				throw new AdamException(BusinessStatusEnum.ROLE_DUPLICATE_NAME.getCode(),BusinessStatusEnum.ROLE_DUPLICATE_NAME.getDescription());
			}
		}
		String createBy = "";
		if(null!=CurrentUserUtils.getCurrentUser()){
			createBy = CurrentUserUtils.getCurrentUser().getUserName();
		}else{
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}
		Integer roleId = role.getId();
		//删除掉权限角色关系
		List<Integer> listIds = new ArrayList<Integer>();
		listIds.add(roleId);
		rolePrivilegeMapper.deleteSubjectPrivilegeBySubjectId(listIds);

		List<RolePrivilegePo> rolePrivilege = new ArrayList<>();
		//全选权限
		List<Integer> selectAllId = role.getSelectAllId();
		for(Integer id:selectAllId){
			RolePrivilegePo rpp = new RolePrivilegePo();
			rpp.setSubjectType(SubjectTypeEnum.ROLE.getKey());
			rpp.setRoleId(roleId);
			rpp.setPrivilegeId(id);
			rpp.setSelectStatus(1);
			rolePrivilege.add(rpp);
		}
		//半选权限
		List<Integer> selectHalfId = role.getSelectHalfId();
		for(Integer id:selectHalfId){
			RolePrivilegePo rpp = new RolePrivilegePo();
			rpp.setSubjectType(SubjectTypeEnum.ROLE.getKey());
			rpp.setRoleId(roleId);
			rpp.setPrivilegeId(id);
			rpp.setSelectStatus(0);
			rolePrivilege.add(rpp);
		}
		if(rolePrivilege.isEmpty()){
			throw new AdamException(BusinessStatusEnum.RESOURCE_NOT_FOUND.getCode(),BusinessStatusEnum.RESOURCE_NOT_FOUND.getDescription());
		}
		rolePrivilegeMapper.batchInsertSubjectPrivilege(rolePrivilege);
		//更新角色信息
		RolePo rolePo = roleMapper.selectById(roleId);
		rolePo.setName(role.getName());
		rolePo.setRemark(role.getRemark());
		rolePo.setUpdateTime(new Date());
		rolePo.setUpdateBy(createBy);
		return new Resp<Integer>(roleMapper.updateRole(rolePo));
	}
	
	/**
	 * 查询所有角色信息
	 * 
	 * @param isActive 是否有效
	 * @return
	 */
	public List<RoleResp> queryAllRole(Integer isActive) {
		return roleMapper.queryAllRole(isActive);
	}
	/**
	 * 根据角色ID查询已经使用的角色信息
	 * 
	 * @param ids 角色ID
	 * @return
	 */
	public List<RoleResp> queryUsedRoleByRoleIds(List<Integer> ids){
		List<RoleResp> result = null;
		if(!ids.isEmpty()){
			result = roleMapper.queryUsedRoleByRoleIds(ids);
		}
		return result;
	}

	/**
	 * 根据角色ID查询已经使用的角色信息
	 *
	 * @param id 角色ID
	 * @return
	 */
	public List<RoleResp> queryUsedRolesByRoleId(String id){
		List<RoleResp> result = null;
		if(!StringUtils.isEmpty(id)){
			result = roleMapper.queryUsedRolesByRoleId(Long.valueOf(id));
		}
		return result;
	}

	/**
	 * 查询角色数量
	 *
	 * @param name
	 * @param id
	 *
	 * @return
	 */
	public Integer queryRoleCountByName(String name, Integer id) {
		Integer count = 0;
		Role param = new Role();
		//校验是否重名
		param.setName(name);
		param.setId(id);
		return roleMapper.queryRoleCountByName(param);
	}
	/**
	 * 根据用户ID查询角色信息
	 *
	 * @return
	 */
	public List<RoleResp> queryRoleListByUserId(Long userId){
		return roleMapper.queryRoleListByUserId(userId);
	}
}
