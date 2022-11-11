package gaea.user.center.service.controller;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import gaea.user.center.service.model.dto.Role;
import gaea.user.center.service.model.request.RoleQuery;
import gaea.user.center.service.model.response.RoleVo;
import gaea.user.center.service.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;

	/**
	 * 分页查询角色信息
	 * @param pageRequest
	 * @param query
	 * @return
	 */
	@ApiOperation("分页角色列表")
	@GetMapping(value = "/queryRolePageList")
	public ResponseEntity queryRolePageList(PageRequest pageRequest,RoleQuery query) {
		Page<RoleVo> rolePageList = roleService.queryRolePageList(pageRequest, query);
		return RestResponse.ok(rolePageList);
	}
	/**
	 * 查询角色信息
	 * 
	 * @param query 角色信息
	 * @return
	 */
	@ApiOperation("查询角色信息")
	@GetMapping(value = "/queryRole")
	public ResponseEntity queryRole(RoleQuery query) {
		return RestResponse.ok(roleService.queryRole(query));
	}
	/**
	 * 查询所有角色信息
	 *
	 * @param query 查询条件
	 * @return
	 */
	@ApiOperation("查询所有角色信息")
	@GetMapping(value = "/queryAllRole")
	public ResponseEntity queryAllRole(RoleQuery query) {
		return RestResponse.ok(roleService.queryAllRole(query));
	}
	/**
	 * 根据组织ID查询角色信息
	 * @param orgId
	 * @return
	 */
//	@ApiOperation("根据组织ID查询角色信息")
//	@GetMapping(value = "/queryRoleByOrgId")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "orgId", value = "组织ID", required = true, paramType = "query", dataType = "int")})
//	public ResponseEntity queryRoleByOgranizationId(int orgId) {
//		return RestResponse.ok(roleService.queryRoleByOgranizationId(Integer.valueOf(orgId)));
//	}
	/**
	 * 查询角色信息
	 * 
	 * @param role 角色信息
	 * @return
	 */
	@ApiOperation("新增角色")
	@PostMapping(value = "/addRole")
	public ResponseEntity addRole(@RequestBody Role role) {
		return RestResponse.ok(roleService.addRole(role));
	}
	/**
	 * 查询已经被使用的角色信息
	 * 
	 * @param ids 角色ID
	 * @return
	 */
/*	@ApiOperation("根据角色ID查询被使用的角色")
	@GetMapping(value = "/queryUsedRoleByRoleIds")
	public ResponseEntity queryUsedRoleByRoleIds(@RequestBody List<Integer> ids) {
		return RestResponse.ok(roleService.queryUsedRoleByRoleIds(ids));
	}*/
	/**
	 * 查询已经被使用的角色信息
	 *
	 * @param id 角色ID
	 * @return
	 */
	@ApiOperation("根据角色ID查询被使用的角色")
	@GetMapping(value = "/queryUsedRolesByRoleId")
	public ResponseEntity queryUsedRolesByRoleId(String id) {
		return RestResponse.ok(roleService.queryUsedRolesByRoleId(id));
	}
	/**
	 * 根据组织ID查询角色信息
	 * @param ids
	 * @return
	 */
	@ApiOperation("根据ID删除角色")
	@PostMapping(value = "/deleteRoleById")
	public ResponseEntity deleteRoleById(@RequestBody List<Integer> ids) {
		return RestResponse.ok(roleService.deleteRoleById(ids));
	}
	
	/**
	 * 更新角色信息
	 * 
	 * @param role 更新角色信息
	 * @return
	 */
	@ApiOperation("更新角色")
	@PostMapping(value = "/updateRole")
	public ResponseEntity updateRole(@RequestBody Role role) {
		return RestResponse.ok(roleService.updateRole(role));
	}

	/**
	 * 查询角色数量
	 *
	 * @param name
	 * @param id
	 * @return
	 */
	@ApiOperation("查询角色数量")
	@GetMapping(value = "/queryRoleCount")
	public ResponseEntity queryRoleCount(String name, Integer id) {
		return RestResponse.ok(roleService.queryRoleCountByName(name, id));
	}
	/**
	 * 查询角色列表信息
	 *
	 * @return
	 */
	@ApiOperation("查询角色列表信息")
	@GetMapping(value = "/queryRoleList")
	public ResponseEntity queryRoleList() {
		return RestResponse.ok(roleService.queryRoleList());
	}
	/**
	 * 根据用户ID查询角色信息
	 *
	 * @return
	 */
	@ApiOperation("根据用户ID查询角色信息")
	@GetMapping(value = "/queryRoleListByUserId")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户id", required = false, dataType = "Integer")
	})
	public ResponseEntity queryRoleListByUserId(Long userId) {
		return RestResponse.ok(roleService.queryRoleListByUserId(userId));
	}
}
