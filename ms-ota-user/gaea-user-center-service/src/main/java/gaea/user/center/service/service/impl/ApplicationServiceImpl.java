package gaea.user.center.service.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import gaea.user.center.service.dao.ApplicationMapper;
import gaea.user.center.service.models.domain.Application;
import gaea.user.center.service.service.IApplicationService;
/**
 * @ClassName: ApplicationServiceImpl
 * 应用信息Service实现类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class ApplicationServiceImpl implements IApplicationService{

	@Autowired
	private ApplicationMapper applicationMapper;
	
	/**
	 * 查询应用信息
	 * 
	 * @param appName 应用名称
	 * @return
	 */
	public List<Application> queryApplicationList(String appName){
		Application app = new Application();
		app.setAppName(appName);
		return applicationMapper.queryApplicationByCondiction(app);
	}
}
