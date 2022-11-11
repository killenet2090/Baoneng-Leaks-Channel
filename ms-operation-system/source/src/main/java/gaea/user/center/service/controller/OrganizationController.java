package gaea.user.center.service.controller;

import com.bnmotor.icv.adam.web.rest.BaseController;
import gaea.user.center.service.model.request.OrganizationQuery;
import gaea.user.center.service.service.IOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: OrganizationController
 * 组织信息Controller类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@AllArgsConstructor
@RestController
@Api(value="组织管理",tags={"组织管理"})
@RequestMapping("organization")
public class OrganizationController extends BaseController {

	public OrganizationController(){

	}

	@Autowired
	private IOrganizationService organizationService;

	/**
	 * 组织列表查询
	 * @param query
	 * @return
	 */
	@ApiOperation("组织列表查询")
	@PostMapping(value = "/queryOrganizationList")
	public ResponseEntity queryApplicationList(@RequestBody OrganizationQuery query) {
		return ResponseEntity.ok(organizationService.queryOrganization(query));
	}
	
	/**
	 * 根据用户ID查询该用户所属组织信息
	 * 
	 * @param userId 用户ID 
	 * @return
	 */
	@ApiOperation("根据用户ID查询该用户所属组织信息")
	@GetMapping(value = "/queryOrganizationByUserId")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "int")})
	public ResponseEntity queryOrganizationByUserId(int userId) {
		return ResponseEntity.ok(organizationService.queryOrganizationByUserId(Integer.valueOf(userId)));
	}
}
