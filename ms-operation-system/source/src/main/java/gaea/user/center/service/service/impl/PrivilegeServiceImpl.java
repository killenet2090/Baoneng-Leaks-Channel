package gaea.user.center.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.adam.data.mysql.utils.PageUtil;
import gaea.user.center.service.common.CurrentUser;
import gaea.user.center.service.common.CurrentUserUtils;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.mapper.PrivilegeMapper;
import gaea.user.center.service.mapstuct.PrivilegeVoMapper;
import gaea.user.center.service.model.dto.RolePrivilege;
import gaea.user.center.service.model.entity.PrivilegePo;
import gaea.user.center.service.model.entity.RolePrivilegePo;
import gaea.user.center.service.model.entity.UserPrivilegePo;
import gaea.user.center.service.model.request.PrivilegeQuery;
import gaea.user.center.service.model.request.PrivilegeQueryReq;
import gaea.user.center.service.model.response.PrivilegeResp;
import gaea.user.center.service.model.response.PrivilegeVo;
import gaea.user.center.service.service.IPrivilegeService;
import gaea.user.center.service.service.IRolePrivilegeService;
import gaea.user.center.service.service.IUserPrivilegeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: PrivilegeServiceImpl
 * 权限信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class PrivilegeServiceImpl extends ServiceImpl<PrivilegeMapper, PrivilegePo> implements IPrivilegeService{

	private final PrivilegeMapper privilegeMapper;

	public PrivilegeServiceImpl(PrivilegeMapper privilegeMapper) {
		this.privilegeMapper = privilegeMapper;
	}

	@Autowired
	private IRolePrivilegeService rolePrivilegeService;

	@Autowired
	private IUserPrivilegeService userPrivilegeService;

	/**
	 * 分页查询权限信息
	 */
	public Page<PrivilegeVo> queryPrivilegePageList(Pageable pageable, PrivilegeQuery privilegeQuery) {
		IPage iPage = PageUtil.map(pageable);
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<PrivilegePo> privilegePoPage =  privilegeMapper.queryPrivilegePageList(iPage,privilegeQuery);
		Page<PrivilegeVo> page = PrivilegeVoMapper.INSTANCE.map(privilegePoPage);
		return page;
	}
	/**
	 * 查询权限信息
	 */
	public List<PrivilegeVo> queryPrivilegeList(PrivilegeQuery query) {
		List<PrivilegeVo> privilegeVoList = PrivilegeVoMapper.INSTANCE.map(privilegeMapper.queryPrivilegeList(query));
		return privilegeVoList;
	}
	
	/**
	 * 根据角色ID查询此角色下权限
	 * 
	 * @param roleId
	 * @return
	 */
	public List<PrivilegeVo> queryPrivilegeByRoleId(Integer roleId) {
		return PrivilegeVoMapper.INSTANCE.map(privilegeMapper.queryPrivilegeByRoleId(roleId));
	}
	
	/**
	 * 查询用户的权限
	 * 
	 * @param params
	 * @return
	 */
	public List<PrivilegeResp> queryUserPrivilege(PrivilegeQueryReq params) {
		return privilegeMapper.queryUserPrivilege(params);
	}

	/**
	 * 新增权限信息
	 * @param privilegeVo
	 * @return
	 * @throws AdamException
	 */
	@Override
	public Long insert(PrivilegeVo privilegeVo) throws AdamException {
		Long result = null;
		privilegeVo.setName(StringUtils.isEmpty(privilegeVo.getName())?"":privilegeVo.getName().trim());
		privilegeVo.setResource(StringUtils.isEmpty(privilegeVo.getResource())?"":privilegeVo.getResource().trim());
		privilegeVo.setRemark(StringUtils.isEmpty(privilegeVo.getName())?"":privilegeVo.getRemark().trim());
		privilegeVo.setIsPublic(null!=privilegeVo.getIsPublic()?privilegeVo.getIsPublic():0);
		privilegeVo.setIsVisual(null!=privilegeVo.getIsVisual()?privilegeVo.getIsPublic():0);
		PrivilegePo privilegePo = PrivilegeVoMapper.INSTANCE.revertMap(privilegeVo);
		if(null != CurrentUserUtils.getCurrentUser()){
			privilegePo.setCreateBy(CurrentUserUtils.getCurrentUser().getUserName());
			privilegePo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
		}else{
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}
		privilegePo.setDelFlag(0);
		privilegePo.setCreateTime(LocalDateTime.now());
		privilegePo.setUpdateTime(LocalDateTime.now());
		//相同类型下，“资源名称”或“资源路径”相同的，不能新增/更新
		log.info("ParentId:"+privilegePo.getParentId()+",Type:"+privilegePo.getType()+",Resource:"+privilegePo.getResource());
		PrivilegeQuery query = new PrivilegeQuery();
		query.setParentId(privilegePo.getParentId());
		query.setType(privilegePo.getType());
		query.setName(privilegePo.getName());

		List privilegeNameList = privilegeMapper.queryPrivilegeByCondition(query);

		query.setName(null);
		query.setResource(privilegePo.getResource());
		List privilegePathList = privilegeMapper.queryPrivilegeByCondition(query);

		if(null!=privilegeNameList&&!privilegeNameList.isEmpty()||null!=privilegePathList&&!privilegePathList.isEmpty()){
			throw new AdamException(BusinessStatusEnum.PRIVILEGE_INSERT_REPEAT.getCode(), BusinessStatusEnum.PRIVILEGE_INSERT_REPEAT.getDescription());
		}

		int status = privilegeMapper.insert(privilegePo);
		result = privilegePo.getId();
		return result;
	}
	/**
	 * 批量新增权限信息
	 * @param data
	 * @return
	 */
	@Transactional
	public Integer batchInsert(String data){
		String createBy = "system";
		String name = "";
		String resource ="";
		List<PrivilegePo> privilegePos = new ArrayList<>();
		List list = JSONObject.parseObject(data, List.class);
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			resource =  null!=map.get("path")?(String) map.get("path"):"";
			name = null!=map.get("name")?(String) map.get("name"):"";
			if(StringUtils.isEmpty(name)||StringUtils.isEmpty(resource)){
				continue;
			}
			//查询当前资源是否存在，存在则不插入
			PrivilegeQuery query = new PrivilegeQuery();
			query.setResource(resource);
			query.setCreateBy(createBy);
			List privilegePathList = privilegeMapper.queryPrivilegeList(query);
			if(CollectionUtils.isNotEmpty(privilegePathList)) continue;
			//新增资源信息
			PrivilegePo privilegePo = new PrivilegePo();
			privilegePo.setName(name);
			privilegePo.setRemark(name);
			privilegePo.setResource(resource);
			privilegePo.setType(2);
			privilegePo.setCreateBy(createBy);
			privilegePo.setUpdateBy(createBy);
			privilegePo.setDelFlag(0);
			privilegePo.setCreateTime(LocalDateTime.now());
			privilegePo.setUpdateTime(LocalDateTime.now());
			privilegePos.add(privilegePo);
		}
		//不为空则批量新增
		if(CollectionUtils.isNotEmpty(privilegePos)){
			this.saveBatch(privilegePos);
			List<RolePrivilegePo> rolePrivilegePos = new ArrayList<>();
			for(PrivilegePo privilegePo:privilegePos){
				RolePrivilegePo rolePrivilegePo = new RolePrivilegePo();
				rolePrivilegePo.setPrivilegeId(Integer.valueOf(String.valueOf(privilegePo.getId())));
				rolePrivilegePo.setRoleId(1);
				rolePrivilegePo.setSelectStatus(1);
				rolePrivilegePo.setSubjectType(1);
				rolePrivilegePo.setDelFlag(0);
				if(CollectionUtils.isNotEmpty(rolePrivilegeService.queryRolePrivilegeByCondition(rolePrivilegePo))){
					continue;
				}
				rolePrivilegePos.add(rolePrivilegePo);
			}
			//向管理员角色新增权限
			if(CollectionUtils.isNotEmpty(rolePrivilegePos)) {
				rolePrivilegeService.executeBatch(rolePrivilegePos);
			}
		}
		return privilegePos.size();
	}
	/**
	 * 根据id更新权限信息
	 * @param id
	 * @param privilegeVo
	 * @return
	 */
	@Override
	public Long update(String id,PrivilegeVo privilegeVo) throws AdamException{
		PrivilegePo privilegePo = privilegeMapper.selectById(id);
		if(null == privilegePo) {
			throw new AdamException(BusinessStatusEnum.DATA_NOT_FOUND.getCode(), BusinessStatusEnum.DATA_NOT_FOUND.getDescription());
		}
		Integer parentId = privilegePo.getParentId();
		privilegeVo.setName(StringUtils.isEmpty(privilegeVo.getName())?"":privilegeVo.getName().trim());
		privilegeVo.setResource(StringUtils.isEmpty(privilegeVo.getResource())?"":privilegeVo.getResource().trim());
		privilegeVo.setRemark(StringUtils.isEmpty(privilegeVo.getRemark())?"":privilegeVo.getRemark().trim());
		privilegeVo.setIsPublic(null!=privilegeVo.getIsPublic()?privilegeVo.getIsPublic():0);
		privilegeVo.setIsVisual(null!=privilegeVo.getIsVisual()?privilegeVo.getIsPublic():0);
		privilegePo = PrivilegeVoMapper.INSTANCE.revertMap(privilegeVo);
		privilegePo.setId(Long.valueOf(id));
		privilegePo.setParentId(parentId);
		privilegePo.setUpdateTime(LocalDateTime.now());
		if(null != CurrentUserUtils.getCurrentUser()){
			privilegePo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
		}else{
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}
		log.info("Id:"+id+",ParentId:"+privilegePo.getParentId()+",Type:"+privilegePo.getType()+",Resource:"+privilegePo.getResource());
		List<PrivilegePo> privilegeList = new ArrayList<>();
		//资源类型相同且“资源名称”相同,不能新增/更新
		PrivilegeQuery query = new PrivilegeQuery();
		query.setParentId(privilegePo.getParentId());
		query.setType(privilegePo.getType());
		query.setName(privilegePo.getName());
		List<PrivilegePo> privilegeNameList = privilegeMapper.queryPrivilegeByCondition(query);
		if(null!=privilegeNameList&&!privilegeNameList.isEmpty()){
			for(PrivilegePo po:privilegeNameList){
				if (!id.equals(String.valueOf(po.getId()))){
					privilegeList.add(po);
				}
			}
		}
		//资源类型相同且“资源路径”相同,不能新增/更新
		query.setName(null);
		query.setResource(privilegePo.getResource());
		List<PrivilegePo> privilegePathList = privilegeMapper.queryPrivilegeByCondition(query);
		if(null!=privilegePathList&&!privilegePathList.isEmpty()){
			for(PrivilegePo po:privilegePathList){
				if (!id.equals(String.valueOf(po.getId()))){
					privilegeList.add(po);
				}
			}
		}
		if(null!=privilegeList&&!privilegeList.isEmpty()){
			throw new AdamException(BusinessStatusEnum.PRIVILEGE_UPDATE_REPEAT.getCode(), BusinessStatusEnum.PRIVILEGE_UPDATE_REPEAT.getDescription());
		}
		int status = privilegeMapper.updateById(privilegePo);
		Long result = privilegePo.getId();
		return result;
	}
	/**
	 * 修改权限父ID
	 * @param id
	 * @param parentId
	 * @return
	 */
	public Long updateParentId(int id, int parentId) throws AdamException{
		if(null == CurrentUserUtils.getCurrentUser()){
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}
		PrivilegePo privilegePo = privilegeMapper.selectById(id);
		if(null == privilegePo) {
			throw new AdamException(BusinessStatusEnum.DATA_NOT_FOUND.getCode(), BusinessStatusEnum.DATA_NOT_FOUND.getDescription());
		}

		//资源类型相同且“资源名称”相同,不能新增/更新
		PrivilegeQuery query = new PrivilegeQuery();
		query.setParentId(parentId);
		query.setType(privilegePo.getType());
		query.setName(privilegePo.getName());

		List<PrivilegePo> pList = privilegeMapper
				.queryPrivilegeByCondition(query)
				.stream()
				.filter(e->!String.valueOf(e.getId()).equals(String.valueOf(id)))
				.collect(Collectors.toList());

		//资源类型相同且“资源路径”相同,不能新增/更新
		query.setName(null);
		query.setResource(privilegePo.getResource());
		List<PrivilegePo> tList = privilegeMapper
				.queryPrivilegeByCondition(query)
				.stream()
				.filter(e->!String.valueOf(e.getId()).equals(String.valueOf(id)))
				.collect(Collectors.toList());

		if(CollectionUtils.isNotEmpty(pList)||CollectionUtils.isNotEmpty(tList)){
			throw new AdamException(BusinessStatusEnum.PRIVILEGE_UPDATE_PARENT.getCode(), BusinessStatusEnum.PRIVILEGE_UPDATE_PARENT.getDescription());
		}
		//更新父节点
		privilegePo.setId(Long.valueOf(id));
		privilegePo.setParentId(parentId);
		privilegePo.setUpdateTime(LocalDateTime.now());
		privilegePo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
		log.info("Id:"+id+",ParentId:"+privilegePo.getParentId());
		int status = privilegeMapper.updateById(privilegePo);
		Long result = privilegePo.getId();
		return result;
	}
	/**
	 * 根据id删除权限信息
	 * @param id
	 * @return
	 * @throws AdamException
	 */
	@Override
	public Long delete(String id) throws AdamException {
		//判断资源分配情况，如果有分配信息，则不允许删除
		List<UserPrivilegePo> privilegeList = userPrivilegeService.queryUserPrivilegeByPrivilegeId(Long.valueOf(id));
		if(null != privilegeList && privilegeList.size() > 0){
			throw new AdamException(BusinessStatusEnum.DATA_IS_ALLOCATED.getCode(),BusinessStatusEnum.DATA_IS_ALLOCATED.getDescription());
		}
		Long result = null;
		PrivilegePo privilegePo = new PrivilegePo();
		privilegePo.setId(Long.valueOf(id));
		if(null != CurrentUserUtils.getCurrentUser()){
			privilegePo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
		}else{
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}
		Integer status = privilegeMapper.deleteByIdWithFill(privilegePo);
		result = privilegePo.getId();
		return result;
	}

	/**
	 * 查询单个权限信息
	 * @param privilegeVo
	 * @return
	 * @throws AdamException
	 */
	@Override
	public PrivilegeVo queryPrivilege(PrivilegeVo privilegeVo) throws AdamException {
		PrivilegePo privilegePo = privilegeMapper.queryPrivilege(privilegeVo);
		PrivilegeVo result = PrivilegeVoMapper.INSTANCE.map(privilegePo);
		return result;
	}

	/**
	 * 批量保存用户权限信息
	 * @param privilegeVo
	 * @return
	 */
	@Override
	public Integer saveBatchPrivilegeByUserIdAndRoles(PrivilegeVo privilegeVo) {
/*		List<String> roleList = Arrays.asList(privilegeVo.getRoleIds().split(","));
		//查询出所有角色对应的权限列表，并去重
		List<RolePrivilege> privilegePoList = rolePrivilegeService.queryPrivilegeListByRoleIds(roleList);
		//将用户信息与权限信息批量保存到用户权限表中
		Integer status = userPrivilegeService.saveBatchUserPrivilege(privilegeVo.getUserId(),privilegePoList);
		return status;*/
		return 1;
	}

}
