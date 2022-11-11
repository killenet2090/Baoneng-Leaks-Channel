package gaea.user.center.service.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import gaea.user.center.service.dao.OrganizationMapper;
import gaea.user.center.service.models.domain.Organization;
import gaea.user.center.service.service.IOrganizationService;
/**
 * @ClassName: OrganizationServiceImpl
 * 组织信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class OrganizationServiceImpl implements IOrganizationService{

	@Autowired
	private OrganizationMapper organizationMapper;
	/**
	 * 查询组织信息
	 * 
	 * @param organization
	 * @return
	 */
	public List<Organization> queryOrganization(Organization organization){
		return organizationMapper.queryOrganization(organization);
	}
	
	/**
	 * 根据用户ID查询该用户所属组织信息
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	public List<Organization> queryOrganizationByUserId(Integer userId){
		return organizationMapper.queryOrganizationByUserId(userId);
	}
}
