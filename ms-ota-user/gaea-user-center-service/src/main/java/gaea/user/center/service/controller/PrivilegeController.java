package gaea.user.center.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gaea.user.center.service.models.domain.Privilege;
import gaea.user.center.service.models.response.Resp;
import gaea.user.center.service.service.IPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
/**
 * @ClassName: PrivilegeController
 * 权限信息Controller类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@AllArgsConstructor
@RestController
@Api(value="权限管理",tags={"权限管理"})
@RequestMapping("privilege")
public class PrivilegeController {

	@Autowired
	private IPrivilegeService privilegeService;
	
	/**
	 * 分页查询权限信息
	 * @param appName 应用名称
	 * @return
	 */
	@ApiOperation("分页查询权限列表")
	@PostMapping(value = "/queryPrivilegePageList")
	public Resp<List<Privilege>> queryPrivilegePageList(@RequestBody Privilege privilege) {
		return new Resp<List<Privilege>>(privilegeService.queryPrivilegePageList(privilege));
	}
	
	/**
	 * 查询权限信息
	 * @param appName 应用名称
	 * @return
	 */
	@ApiOperation("查询权限列表")
	@PostMapping(value = "/queryPrivilege")
	public Resp<List<Privilege>> queryPrivilege(@RequestBody Privilege privilege) {
		return new Resp<List<Privilege>>(privilegeService.queryPrivilege(privilege));
	}
	
	
	/**
	 * 根据角色ID查询此角色下权限
	 * 
	 * @param roleId
	 * @return
	 */
	@ApiOperation("根据角色ID查询此角色下权限")
	@GetMapping(value = "/queryPrivilegeByRoleId")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "query", dataType = "int")})
	public Resp<List<Privilege>> queryPrivilegeByRoleId(int roleId) {
		return new Resp<List<Privilege>>(privilegeService.queryPrivilegeByRoleId(Integer.valueOf(roleId)));
	}

	/**
	 * 根据用户ID查询该用户的权限
	 * 
	 * @param userId
	 * @return
	 */
	@ApiOperation("根据用户ID查询该用户的权限")
	@GetMapping(value = "/queryPrivilegeByUserId")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "int")})
	public Resp<List<Privilege>> queryPrivilegeByUserId(int userId) {
		return new Resp<List<Privilege>>(privilegeService.queryPrivilegeByUserId(Integer.valueOf(userId)));
	}
}
