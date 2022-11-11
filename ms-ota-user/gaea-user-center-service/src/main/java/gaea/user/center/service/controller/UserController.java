/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.controller;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.web.rest.BaseController;
import gaea.user.center.service.models.page.PageResult;
import gaea.user.center.service.models.domain.User;
import gaea.user.center.service.models.response.Resp;
import gaea.user.center.service.models.query.UserQuery;
import gaea.user.center.service.models.request.UserReq;
import gaea.user.center.service.models.validate.Update;
import gaea.user.center.service.models.vo.UserDetailVO;
import gaea.user.center.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <pre>
 *   Api,控制业务流程.
 *  提供包括业务流程校验,业务流程控制.
 *
 *  <b>地址规则</b>
 *   列表或分页:GET /persons
 *   单人: GET /persons/1
 *   新增: POST /persons
 *   修改: PUT /persons
 *   删除: DELETE /persons/1
 *   关联
 *   用户下狗
 *   列表 GET /persons/1/dogs
 *   单狗 GET /persons/1/dogs/2
 *   增加狗 POST /persons/1/dogs
 *   修改狗 PUT /persons/1/dogs
 *   删除狗 DELETE /persons/1/dogs/2
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Api(value="用户管理",tags={"用户管理"})
@RestController
public class UserController extends BaseController {
    /**
     * 服务接口
     */
    @Autowired
    private final UserService userService;

    @ApiOperation("列表")
    @GetMapping("users")
    public PageResult<List<User>> queryPage(UserQuery query) {
        return new PageResult<>(query, userService.queryPage(query));
    }

    @ApiOperation("获取")
    @GetMapping("users/{userId}")
    public Resp<UserDetailVO> queryById(@PathVariable Long userId) {
        return new Resp<>(userService.queryById(userId));
    }

    @ApiOperation("新增")
    @PostMapping("users")
    public Resp<Long> saveUser(@RequestBody @Valid UserReq userReq) throws AdamException {
        return new Resp<>(userService.insertUser(userReq));
    }

    @ApiOperation("更新")
    @PutMapping("users")
    public Resp<Integer> updateUser(@RequestBody @Validated(value = Update.class) UserReq userReq) {
        return new Resp<>(userService.updateUser(userReq));
    }

    @ApiOperation("删除")
    @DeleteMapping("users/{userId}")
    public Resp<Integer> deleteById(@PathVariable Long userId) {
        return new Resp<>(userService.deleteById(userId));
    }

}
