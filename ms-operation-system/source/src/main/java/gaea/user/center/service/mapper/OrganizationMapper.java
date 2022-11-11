package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.OrganizationPo;
import gaea.user.center.service.model.request.OrganizationQuery;
import gaea.user.center.service.model.response.OrganizationResp;

import java.util.List;

/**
 * @ClassName: OrganizationMapper
 * 组织信息Mapper接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface OrganizationMapper extends AdamMapper<OrganizationPo> {

	/**
	 * 根据条件查询组织信息
	 * @param query
	 * @return
	 */
	List<OrganizationPo> queryOrganization(OrganizationQuery query);
	/**
	 * 根据组织ID查询组织信息
	 * 
	 * @param id 组织ID
	 * @return
	 */
	OrganizationPo queryOrganizationById(Integer id);
	
	/**
	 * 根据用户ID查询该用户所属组织信息
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<OrganizationResp> queryOrganizationByUserId(Integer userId);
}
