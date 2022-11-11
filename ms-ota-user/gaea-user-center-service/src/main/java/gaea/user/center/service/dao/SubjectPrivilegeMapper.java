package gaea.user.center.service.dao;

import java.util.List;

import gaea.user.center.service.models.domain.SubjectPrivilege;

/**
 * @ClassName: SubjectPrivilegeMapper
 * 角色权限关系信息mapper类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SubjectPrivilegeMapper {

	/**
	 * 新增角色权限关系
	 * 
	 * @param subjectPrivilege
	 * @return
	 */
	public Integer batchInsertSubjectPrivilege(List<SubjectPrivilege> subjectPrivilege);
	
	/**
	 * 查询角色权限关系
	 * 
	 * @param subjectPrivilege 
	 * @return
	 */
	public List<SubjectPrivilege> querySubjectPrivilegeBySubjectId(Integer subjectId);
	
	/**
	 * 根据subjectId删除角色权限关系
	 * 
	 * @param subjectIds
	 * @return
	 */
	public Integer deleteSubjectPrivilegeBySubjectId(List<Integer> subjectIds);
	
}
