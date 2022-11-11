package gaea.user.center.service.controller;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import gaea.user.center.service.model.request.ProjectQuery;
import gaea.user.center.service.model.response.ProjectVo;
import gaea.user.center.service.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName: ProjectController
 * @Description: 项目信息管理
 * @author: jiangchangyuan1
 * @date: 2020/8/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@AllArgsConstructor
@RestController
@Api(value="项目管理",tags={"项目管理"})
@RequestMapping("/project")
public class ProjectController extends BaseController {
    /**
     * 服务接口
     */
    @Autowired
    private IProjectService projectService;

    /**
     * 分页查询项目信息
     * @param pageRequest
     * @param query
     * @return
     */
    @ApiOperation("分页查询项目信息")
    @GetMapping("/queryPage")
    public ResponseEntity queryProjectPage(PageRequest pageRequest,ProjectQuery query){
        Page<ProjectVo> projectVoList = projectService.queryProjectPage(pageRequest,query);
        return RestResponse.ok(projectVoList);
    }

    /**
     * 新增项目信息
     * @param projectVo
     * @return
     */
    @ApiOperation("新增项目信息")
    @PostMapping("insert")
    public ResponseEntity insertProject(@RequestBody @Valid ProjectVo projectVo){
        return RestResponse.ok(projectService.insert(projectVo));
    }

    /**
     * 修改项目信息
     * @param id
     * @param projectVo
     * @return
     */
    @ApiOperation("修改项目信息")
    @PostMapping("update/{id}")
    public ResponseEntity updateProject(@PathVariable String id,@RequestBody @Valid ProjectVo projectVo) {
        return RestResponse.ok(projectService.updateProject(id,projectVo));
    }

    /**
     * 删除项目信息
     * @param id
     * @return
     */
    @ApiOperation("删除项目信息")
    @GetMapping("delete/{id}")
    public ResponseEntity deleteProject(@PathVariable String id){
        return RestResponse.ok(projectService.deleteProject(id));
    }

    /**
     * 查询项目信息列表（不分页）
     * @param projectVo
     * @return
     */
    @ApiOperation("查询项目信息列表")
    @GetMapping("queryList")
    public ResponseEntity queryList(ProjectVo projectVo){
        return RestResponse.ok(projectService.queryList(projectVo));
    }

    /**
     * 根据id查询单个项目信息
     * @param id
     * @return
     */
    @ApiOperation("查询项目信息")
    @GetMapping("queryById/{id}")
    public ResponseEntity queryById(@PathVariable Long id){
        return RestResponse.ok(projectService.queryById(id));
    }

    /**
     * 根据用户id查询用户下项目列表
     * @param userId
     * @return
     */
    @ApiOperation("根据用户id查询项目信息")
    @GetMapping("queryListByUserId/{userId}")
    public ResponseEntity queryListByUserId(@PathVariable Long userId){
        return RestResponse.ok(projectService.queryListByUserId(userId));
    }

    /**
     * 批量插入某项目管理者列表
     * @param projectVo
     * @return
     */
    @ApiOperation("批量插入某项目下管理者列表")
    @PostMapping("saveBatchProjectUsers")
    public ResponseEntity saveBatchProjectUsers(ProjectVo projectVo){
        return RestResponse.ok(projectService.saveBatchProjectUsers(projectVo));
    }
}
