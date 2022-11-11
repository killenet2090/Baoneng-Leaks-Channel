package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gaea.user.center.service.mapper.OrganizationMapper;
import gaea.user.center.service.mapstuct.OrganizationVoMapper;
import gaea.user.center.service.model.entity.OrganizationPo;
import gaea.user.center.service.model.request.OrganizationQuery;
import gaea.user.center.service.model.response.OrganizationResp;
import gaea.user.center.service.model.response.OrganizationVo;
import gaea.user.center.service.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @ClassName: OrganizationServiceImpl
 * 组织信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationPo> implements IOrganizationService{

	@Autowired
	private OrganizationMapper organizationMapper;

	/**
	 * 查询组织信息
	 * @param query
	 * @return
	 */
	@Override
	public List<OrganizationVo> queryOrganization(OrganizationQuery query){
			List<OrganizationPo> organizationPoList = organizationMapper.queryOrganization(query);
			List<OrganizationVo> organizationVoList = OrganizationVoMapper.INSTANCE.map(organizationPoList);
			return organizationVoList;
	}
	
	/**
	 * 根据用户ID查询该用户所属组织信息
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	@Override
	public List<OrganizationResp> queryOrganizationByUserId(Integer userId){
		return organizationMapper.queryOrganizationByUserId(userId);
	}
}
