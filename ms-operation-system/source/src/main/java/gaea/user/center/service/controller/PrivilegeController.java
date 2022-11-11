package gaea.user.center.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import gaea.user.center.service.model.request.PrivilegeQuery;
import gaea.user.center.service.model.request.PrivilegeQueryReq;
import gaea.user.center.service.model.response.PrivilegeVo;
import gaea.user.center.service.service.IPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PrivilegeController
 * 权限信息Controller类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020/8/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@AllArgsConstructor
@NoArgsConstructor
@RestController
@Api(value="权限管理",tags={"权限管理"})
@RequestMapping("privilege")
public class PrivilegeController extends BaseController {

	@Autowired
	private IPrivilegeService privilegeService;

	/**
	 * 分页查询权限信息
	 * @param pageRequest
	 * @param query
	 * @return
	 */
	@ApiOperation("分页查询权限列表")
	@GetMapping(value = "/queryPrivilegePageList")
	public ResponseEntity queryPrivilegePageList(PageRequest pageRequest, PrivilegeQuery query) {
		Page<PrivilegeVo> privilegeVoPage = privilegeService.queryPrivilegePageList(pageRequest, query);
		return RestResponse.ok(privilegeVoPage);
	}

	/**
	 * 查询权限信息
	 * @param query
	 * @return
	 */
	@ApiOperation("查询权限列表")
	@GetMapping(value = "/queryPrivilegeList")
	public ResponseEntity queryPrivilegeList(PrivilegeQuery query) {
		List<PrivilegeVo> privilegeVoList = privilegeService.queryPrivilegeList(query);
		return RestResponse.ok(privilegeVoList);
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
	public ResponseEntity queryPrivilegeByRoleId(int roleId) {
		List<PrivilegeVo> privilegeVoList = privilegeService.queryPrivilegeByRoleId(Integer.valueOf(roleId));
		return RestResponse.ok(privilegeVoList);
	}

	/**
	 * 查询用户的权限
	 * 
	 * @param params
	 * @return
	 */
	@ApiOperation("查询用户的权限")
	@GetMapping(value = "/queryUserPrivilege")
	public ResponseEntity queryUserPrivilege(PrivilegeQueryReq params) {
		return RestResponse.ok(privilegeService.queryUserPrivilege(params));
	}

	/**
	 * 根据用户id查询用户的权限
	 * @param userId
	 * @return
	 */
	@ApiOperation("API查询用户的权限")
	@GetMapping(value = "/queryPrivilegeByUserId")
	public ResponseEntity queryPrivilegeByUserId(Long userId) {
		PrivilegeQueryReq privilegeQueryReq = new PrivilegeQueryReq();
		privilegeQueryReq.setUserId(userId);
		return RestResponse.ok(privilegeService.queryUserPrivilege(privilegeQueryReq));
	}

	/**
	 * 新增权限资源
	 * @param privilegeVo
	 * @return
	 */
	@ApiOperation("新增权限信息")
	@PostMapping("/insert")
	public ResponseEntity insert(@RequestBody @Valid PrivilegeVo privilegeVo) {
		return RestResponse.ok(privilegeService.insert(privilegeVo));
	}

	/**
	 * 修改权限信息
	 * @param privilegeVo
	 * @return
	 */
	@ApiOperation("修改权限信息")
	@PostMapping("/update/{id}")
	public ResponseEntity update(@PathVariable String id,@RequestBody @Valid PrivilegeVo privilegeVo) {
		return RestResponse.ok(privilegeService.update(id,privilegeVo));
	}

	/**
	 * 删除权限信息
	 * @param id 权限id
	 * @return
	 */
	@ApiOperation("删除权限信息")
	@GetMapping("/delete/{id}")
	public ResponseEntity delete(@PathVariable String id) {
		return RestResponse.ok(privilegeService.delete(id));
	}

	/**
	 * 查询权限信息
	 * @param privilegeVo 权限实体
	 * @return
	 */
	@ApiOperation("查询权限信息")
	@GetMapping("/queryPrivilege")
	public ResponseEntity queryPrivilege(PrivilegeVo privilegeVo) {
		return RestResponse.ok(privilegeService.queryPrivilege(privilegeVo));
	}

	/**
	 * 根据用户及角色Ids批量保存用户权限数据
	 * @param privilegeVo 权限实体
	 * @return
	 */
	@ApiOperation("批量保存用户权限信息")
	@GetMapping("/saveBatchPrivilegeByUserIdAndRoles")
	public ResponseEntity saveBatchPrivilegeByUserIdAndRoles(PrivilegeVo privilegeVo) {
		return RestResponse.ok(privilegeService.saveBatchPrivilegeByUserIdAndRoles(privilegeVo));
	}
	/**
	 * 修改权限父ID
	 * @param id
	 * @param parentId
	 * @return
	 */
	@ApiOperation("修改权限信息")
	@GetMapping("/updateParentId")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "权限ID", required = true, paramType = "query", dataType = "int"),
			@ApiImplicitParam(name = "parentId", value = "父权限ID", required = true, paramType = "query", dataType = "int"),
	})
	public ResponseEntity updateParentId(int id, int parentId) {
		return RestResponse.ok(privilegeService.updateParentId(id,parentId));
	}

	/**
	 * 批量新增权限资源
	 * @param data
	 * @return
	 */
	@ApiOperation("批量新增权限资源")
	@PostMapping("/batchInsert")
	public ResponseEntity batchInsert(@RequestBody String data) {
		return RestResponse.ok(privilegeService.batchInsert(data));
	}
}
