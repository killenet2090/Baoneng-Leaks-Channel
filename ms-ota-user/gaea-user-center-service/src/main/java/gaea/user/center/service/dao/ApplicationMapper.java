package gaea.user.center.service.dao;

import java.util.List;

import gaea.user.center.service.models.domain.Application;


/**
 * @ClassName: ApplicationMapper
 * 应用信息Mapper接口类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ApplicationMapper {

	/**
	 * 根据条件查询应用信息
	 * 
	 * @param app
	 * @return
	 */
	public List<Application> queryApplicationByCondiction(Application app);
}
