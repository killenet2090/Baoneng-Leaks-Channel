package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.dto.RolePrivilege;
import gaea.user.center.service.model.entity.PrivilegePo;
import gaea.user.center.service.model.entity.RolePo;
import gaea.user.center.service.model.entity.RolePrivilegePo;
import gaea.user.center.service.model.request.PrivilegeQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: SubjectPrivilegeMapper
 * 角色权限关系信息mapper类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public interface RolePrivilegeMapper extends AdamMapper<RolePrivilegePo> {

	/**
	 * 新增角色权限关系
	 * 
	 * @param rolePrivilege
	 * @return
	 */
	Integer batchInsertSubjectPrivilege(List<RolePrivilegePo> rolePrivilege);

	/**
	 * 查询角色权限关系
	 * @param roleId
	 * @return
	 */
	List<RolePrivilege> queryRolePrivilegeByRoleId(Integer roleId);

	/**
	 * 查询角色权限关系
	 * @param roleId
	 * @return
	 */
	List<String> queryPrivilegeIdByRoleId(@Param("roleId") Integer roleId, @Param("selectStatus") Integer selectStatus);
	/**
	 * 根据subjectId删除角色权限关系
	 * 
	 * @param subjectIds
	 * @return
	 */
	Integer deleteSubjectPrivilegeBySubjectId(List<Integer> subjectIds);
	
	/**
	 * 根据roleIds删除角色权限关系
	 * 
	 * @param roleIds
	 * @return
	 */
	Integer deleteSubjectPrivilegeByRoleId(List<Integer> roleIds);

	/**
	 * 根据角色ids查询出所有权限列表
	 * @param roleList
	 * @return
	 */
    List<RolePrivilege> queryPrivilegeListByRoleIds(List<String> roleList);

	/**
	 * 根据条件查询角色权限信息
	 *
	 * @param query
	 * @return
	 */
	List<RolePrivilege> queryRolePrivilegeByCondition(@Param("query") RolePrivilegePo query);
	//Integer queryRolePrivilegeByCondition(@Param("query") RolePrivilegePo query);
}
