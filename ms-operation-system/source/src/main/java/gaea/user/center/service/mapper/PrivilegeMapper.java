package gaea.user.center.service.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.PrivilegePo;
import gaea.user.center.service.model.request.PrivilegeQuery;
import gaea.user.center.service.model.request.PrivilegeQueryReq;
import gaea.user.center.service.model.response.PrivilegeResp;
import gaea.user.center.service.model.response.PrivilegeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: PrivilegeMapper
 * 权限信息Mapper接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public interface PrivilegeMapper extends AdamMapper<PrivilegePo> {

	/**
	 * 分页查询权限信息
	 * @param iPage
	 * @param query
	 * @return
	 */
	Page<PrivilegePo> queryPrivilegePageList(IPage iPage, @Param("query")PrivilegeQuery query) ;
	
	/**
	 * 根据条件查询权限信息
	 * 
	 * @param query
	 * @return
	 */
	List<PrivilegePo> queryPrivilegeList(@Param("query") PrivilegeQuery query);
	/**
	 * 根据条件查询权限信息
	 *
	 * @param query
	 * @return
	 */
	List<PrivilegePo> queryPrivilegeByCondition(@Param("query") PrivilegeQuery query);
	/**
	 * 根据角色ID查询此角色下权限
	 * 
	 * @param roleId
	 * @return
	 */
	List<PrivilegePo> queryPrivilegeByRoleId(Integer roleId);
	
	/**
	 * 查询用户的权限
	 * 
	 * @param query
	 * @return
	 */
	List<PrivilegeResp> queryUserPrivilege(@Param("query") PrivilegeQueryReq query);

	/**
	 * 查询权限信息
	 * @param query
	 * @return
	 */
	PrivilegePo queryPrivilege(@Param("query") PrivilegeVo query);
}
