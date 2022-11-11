package gaea.user.center.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gaea.user.center.service.models.domain.Role;
import gaea.user.center.service.models.response.Resp;
import gaea.user.center.service.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * @ClassName: RoleController
 * 角色信息Controller类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@AllArgsConstructor
@RestController
@Api(value="角色管理",tags={"角色管理"})
@RequestMapping("role")
public class RoleController {


	@Autowired
	private IRoleService roleService;
	
	/**
	 * 分页查询角色信息
	 * 
	 * @param role 角色信息
	 * @return
	 */
	@ApiOperation("分页角色列表")
	@PostMapping(value = "/queryRolePageList")
	public Resp<List<Role>> queryRolePageList(@RequestBody Role role) {
		return new Resp<List<Role>>(roleService.queryRolePageList(role));
	}
	/**
	 * 查询角色信息
	 * 
	 * @param role 角色信息
	 * @return
	 */
	@ApiOperation("查询角色列表")
	@PostMapping(value = "/queryRole")
	public Resp<List<Role>> queryRole(@RequestBody Role role) {
		return new Resp<List<Role>>(roleService.queryRole(role));
	}
	/**
	 * 根据组织ID查询角色信息
	 * 
	 * @param ogranizationId 组织ID
	 * @return
	 */
	@ApiOperation("根据组织ID查询角色信息")
	@GetMapping(value = "/queryRoleByOrgId")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orgId", value = "组织ID", required = true, paramType = "query", dataType = "int")})
	public Resp<List<Role>> queryRoleByOgranizationId(int orgId) {
		return new Resp<List<Role>>(roleService.queryRoleByOgranizationId(Integer.valueOf(orgId)));
	}
	/**
	 * 查询角色信息
	 * 
	 * @param role 角色信息
	 * @return
	 */
	@ApiOperation("新增角色")
	@PostMapping(value = "/addRole")
	public Resp<Integer> addRole(@RequestBody Role role) {
		return new Resp<Integer>(roleService.addRole(role));
	}
	
	/**
	 * 查询角色信息
	 * 
	 * @param role 角色信息
	 * @return
	 */
	@ApiOperation("根据ID删除角色")
	@PostMapping(value = "/deleteRoleById")
	public Resp<Integer> deleteRoleById(@RequestBody List<Integer> ids) {
		return new Resp<Integer>(roleService.deleteRoleById(ids));
	}
	
	/**
	 * 更新角色信息
	 * 
	 * @param role 更新角色信息
	 * @return
	 */
	@ApiOperation("更新角色")
	@PostMapping(value = "/updateRole")
	public Resp<Integer> updateRole(@RequestBody Role role) {
		return new Resp<Integer>(roleService.updateRole(role));
	}
}
