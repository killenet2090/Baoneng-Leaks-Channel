package gaea.user.center.service.service;

import java.util.List;

import gaea.user.center.service.models.domain.Organization;

/**
 * @ClassName: IOrganizationService
 * 组织信息Service接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IOrganizationService {

	/**
	 * 查询组织信息
	 * 
	 * @param organization 组织信息
	 * @return
	 */
	public List<Organization> queryOrganization(Organization organization);
	
	/**
	 * 根据用户ID查询该用户所属组织信息
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	public List<Organization> queryOrganizationByUserId(Integer userId);
}
