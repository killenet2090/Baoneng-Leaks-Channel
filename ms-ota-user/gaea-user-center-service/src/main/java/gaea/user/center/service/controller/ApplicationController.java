package gaea.user.center.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gaea.user.center.service.models.domain.Application;
import gaea.user.center.service.models.response.Resp;
import gaea.user.center.service.service.IApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
/**
 * @ClassName: ApplicationController
 * 应用信息Controller类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@AllArgsConstructor
@RestController
@Api(value="应用管理",tags={"应用管理"})
@RequestMapping("application")
public class ApplicationController {

	@Autowired
	private IApplicationService applicationService;
	
	/**
	 * 查询应用信息
	 * @param appName 应用名称
	 * @return
	 */
	@ApiOperation("应用列表")
	@GetMapping(value = "/queryApplicationList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "appName", value = "app名称", required = false, paramType = "query", dataType = "String")})
	public Resp<List<Application>> queryApplicationList(@RequestParam(value = "appName", required = false) String appName) {
		return new Resp<List<Application>>(applicationService.queryApplicationList(appName));
	}
}
