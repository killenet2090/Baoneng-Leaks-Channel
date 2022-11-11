package gaea.user.center.service.controller;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import gaea.user.center.service.model.response.PrivilegeTypeVo;
import gaea.user.center.service.service.IPrivilegeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName: PrivilegeTypeController
 * @Description: 资源类型管理
 * @author: jiangchangyuan1
 * @date: 2020/8/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@AllArgsConstructor
@RestController
@Api(value="资源类型管理",tags={"资源类型管理"})
@RequestMapping("privilege/type")
public class PrivilegeTypeController extends BaseController {
    /**
     * 服务接口
     */
    @Autowired
    private IPrivilegeTypeService privilegeTypeService;

    /**
     * 分页查询资源类型信息
     * @param pageRequest
     * @param privilegeTypeVo
     * @return
     */
    @ApiOperation("分页查询资源类型信息")
    @GetMapping("/queryPage")
    public ResponseEntity queryPrivilegeTypePage(PageRequest pageRequest, PrivilegeTypeVo privilegeTypeVo){
        Page<PrivilegeTypeVo> projectVoList = privilegeTypeService.queryPrivilegeTypePage(pageRequest,privilegeTypeVo);
        return RestResponse.ok(projectVoList);
    }

    /**
     * 新增资源类型
     * @param privilegeTypeVo
     * @return
     */
    @ApiOperation("新增资源类型")
    @PostMapping("insert")
    public ResponseEntity insertPrivilegeType(@RequestBody @Valid PrivilegeTypeVo privilegeTypeVo){
        return RestResponse.ok(privilegeTypeService.insert(privilegeTypeVo));
    }

    /**
     * 修改资源类型信息
     * @param id
     * @param privilegeTypeVo
     * @return
     */
    @ApiOperation("修改资源类型")
    @PostMapping("update/{id}")
    public ResponseEntity updatePrivilegeType(@PathVariable String id,@RequestBody @Valid PrivilegeTypeVo privilegeTypeVo) {
        return RestResponse.ok(privilegeTypeService.update(id,privilegeTypeVo));
    }

    /**
     * 删除资源类型
     * @param id
     * @return
     */
    @ApiOperation("删除资源类型")
    @GetMapping("delete/{id}")
    public ResponseEntity deletePrivilegeType(@PathVariable String id){
        return RestResponse.ok(privilegeTypeService.deletePrivilegeType(id));
    }
    /**
     * 查询资源类型列表
     * @return
     */
    @ApiOperation("查询资源类型列表")
    @GetMapping("/queryList")
    public ResponseEntity queryList(PrivilegeTypeVo privilegeTypeVo){
        return RestResponse.ok(privilegeTypeService.queryList(privilegeTypeVo));
    }

}
