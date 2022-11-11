package gaea.user.center.service.service;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import gaea.user.center.service.model.entity.PrivilegePo;
import gaea.user.center.service.model.request.PrivilegeQuery;
import gaea.user.center.service.model.request.PrivilegeQueryReq;
import gaea.user.center.service.model.response.PrivilegeResp;
import gaea.user.center.service.model.response.PrivilegeVo;

import java.util.List;

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
	Page<PrivilegeVo> queryPrivilegePageList(Pageable pageable, PrivilegeQuery query);
	/**
	 * 查询权限信息
	 */
	List<PrivilegeVo> queryPrivilegeList(PrivilegeQuery query);
	/**
	 * 根据角色ID查询此角色下权限
	 * 
	 * @param roleId
	 * @return
	 */
	List<PrivilegeVo> queryPrivilegeByRoleId(Integer roleId);
	/**
	 * 查询用户的权限
	 * 
	 * @param params
	 * @return
	 */
	public List<PrivilegeResp> queryUserPrivilege(PrivilegeQueryReq params);

	/**
	 * 新增权限信息
	 * @param privilegeVo
	 * @return
	 */
    Long insert(PrivilegeVo privilegeVo);
	/**
	 * 批量新增权限信息
	 * @param data
	 * @return
	 */
	Integer batchInsert(String data);

	/**
	 * 修改权限信息
	 * @param privilegeVo
	 * @return
	 */
	Long update(String id,PrivilegeVo privilegeVo);

	/**
	 * 修改权限父ID
	 * @param id
	 * @param parentId
	 * @return
	 */
	Long updateParentId(int id,int parentId);
	/**
	 * 删除权限信息
	 * @param id
	 * @return
	 */
	Long delete(String id);

	/**
	 * 查询权限信息
	 * @param privilegeVo
	 * @return
	 */
	PrivilegeVo queryPrivilege(PrivilegeVo privilegeVo);

	/**
	 * 批量保存用户权限信息
	 * @param privilegeVo
	 * @return
	 */
	Integer saveBatchPrivilegeByUserIdAndRoles(PrivilegeVo privilegeVo);
}
