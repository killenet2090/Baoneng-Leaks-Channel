/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.controller;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.fasterxml.jackson.annotation.JsonView;
import gaea.user.center.service.common.validate.Update;
import gaea.user.center.service.model.request.LoginReq;
import gaea.user.center.service.model.request.UserBasic;
import gaea.user.center.service.model.request.UserQuery;
import gaea.user.center.service.model.request.UserReq;
import gaea.user.center.service.model.response.UserVo;
import gaea.user.center.service.model.response.view.MgtView;
import gaea.user.center.service.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: UserController
 * 用户信息Controller类
 * @author: 宝能汽车公司-智能网联部
 * @date: 2020年04月27日 上午9:54:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@AllArgsConstructor
@Api(value="用户管理",tags={"用户管理"})
@RestController
@RequestMapping(value="/user")
@Slf4j
public class UserController extends BaseController {
    /**
     * 服务接口
     */
    @Autowired
    private IUserService userService;

    /**
     * 分页查询用户列表
     * @param pageRequest
     * @param userQuery
     * @return
     */
    @ApiOperation("用户列表")
    @GetMapping(value = "/queryPage")
    public ResponseEntity queryPage(PageRequest pageRequest, UserQuery userQuery) {
        Page<UserVo> userList = userService.queryPage(pageRequest, userQuery);
        return RestResponse.ok(userList);
    }

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @ApiOperation("获取")
    @GetMapping("/queryById/{userId}")
    public ResponseEntity queryById(@PathVariable Long userId) {
        return RestResponse.ok(userService.queryById(userId));
    }

    /**
     * 新增用户
     * @param userReq
     * @return
     * @throws AdamException
     */
    @ApiOperation("新增")
    @PostMapping("insert")
    public ResponseEntity saveUser(@RequestBody @Valid UserReq userReq){
        return RestResponse.ok(userService.insertUser(userReq));
    }

    /**
     * 更新用户信息
     * @param userReq
     * @return
     */
    @ApiOperation("更新")
    @PostMapping("update")
    public ResponseEntity updateUser(@RequestBody @Validated(value = Update.class) UserReq userReq) {
        return RestResponse.ok(userService.updateUser(userReq));
    }

    /**
     * 删除用户信息（逻辑删除）
     * @param userId
     * @return
     */
    @ApiOperation("删除")
    @GetMapping("delete/{userId}")
    public ResponseEntity deleteById(@PathVariable Long userId) {
        return RestResponse.ok(userService.deleteById(userId));
    }

    /**
     * 用户登录
     * @param loginReq
     * @return
     */
    @ApiOperation("登陆")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated(value = Update.class) LoginReq loginReq) {
        return RestResponse.ok(userService.login(loginReq.getName(),loginReq.getPassword()));
    }

    /**
     * 用户登出
     * @return
     */
    @ApiOperation("登出")
    @PostMapping("/logout")
    public ResponseEntity logout() {
        userService.loginout();
        return RestResponse.ok("SUCCESS");
    }

    /**
     * 忘记密码
     * @param phone
     * @return
     */
    @ApiOperation("忘记密码")
    @GetMapping("forgetPassword")
    public ResponseEntity password(String phone,String code) {
        return RestResponse.ok(userService.forgetPassword(phone,code));
    }

    /**
     * 修改密码
     * @param userBasic
     * @return
     */
    @ApiOperation("修改密码")
    @PostMapping("changePassword")
    public ResponseEntity changePassword(@RequestBody UserBasic userBasic) {
        return RestResponse.ok(userService.changePassword(userBasic));
    }

    /**
     * 根据项目id获取项目拥有者列表
     * @param projectId
     * @return
     */
    @ApiOperation("获取项目拥有者列表")
    @GetMapping("/queryUserListByProjectId/{projectId}")
    public ResponseEntity queryUserListByProjectId(@PathVariable Long projectId) {
        return RestResponse.ok(userService.queryUserListByProjectId(projectId));
    }

    /**
     * 批量保存某用户下所有项目信息
     * @param userQuery
     * @return
     */
    @ApiOperation("批量保存某用户下所有项目信息")
    @PostMapping("/saveBatchUserProjects")
    public ResponseEntity saveBatchUserProjects(UserQuery userQuery) {
        return RestResponse.ok(userService.saveBatchUserProjects(userQuery));
    }

    /**
     * 启用被禁用状态的账户
     * @param id
     * @return
     */
    @ApiOperation("启用被禁用状态的账户")
    @JsonView(MgtView.class)
    @GetMapping("/enableUserById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = false, dataType = "Integer")
    })
    public ResponseEntity enableUserById(Long id) {
        return RestResponse.ok(userService.enableUserById(id));
    }

    /**
     * 禁用账户
     * @param id
     * @return
     */
    @ApiOperation("禁用账户")
    @GetMapping("/disEnableUserById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = false, dataType = "Integer")
    })
    public ResponseEntity disEnableUserById(Long id) {
        return RestResponse.ok(userService.disEnableUserById(id));
    }

    /**
     * 用户邮箱校验接口
     * @param email
     * @param id
     * @return
     */
    @ApiOperation("用户邮箱/手机号规则校验接口")
    @GetMapping("/userParamValidate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = false, dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话号码", required = false, dataType = "string"),
            @ApiImplicitParam(name = "id", value = "当前编辑用户id,保存不传，编辑必传", required = false, dataType = "Integer")
    })
    public ResponseEntity userParamValidate(String email,String phone,Long id) {
        return RestResponse.ok(userService.userParamValidate(email,phone,id));
    }


//    @ApiOperation("获取验证码")
//    @GetMapping("/activition/checkCode")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "string"),
//    })
//    public ResponseEntity checkCode(String email,String phone) {
//        return RestResponse.ok(userService.checkCode(email,phone));
//    }

    @ApiOperation("验证码验证并发送激活邮件")
    @GetMapping("/activation/sendEmail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "string"),
    })
    public ResponseEntity activation(String phone,String email,String code) {
        return RestResponse.ok(userService.activation(phone,email,code));
    }

    /**
     * 修改手机号
     * @param uid
     * @param phone
     * @return
     * @throws Exception
     */
    @ApiOperation("验证手机验证码并修改手机号")
    @GetMapping("/changePhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "password", value = "账户密码", required = true, dataType = "string"),
    })
    public ResponseEntity changePhone(Long uid,String phone,String code,String password) throws Exception {
        return RestResponse.ok(userService.changePhone(uid,phone,code,password));
    }

    /**
     * 通过手机号邮箱获取验证码
     * 无需校验登录
     * @param phone
     * @return
     * @throws Exception
     */
    @ApiOperation("通过手机号邮箱获取验证码")
    @GetMapping("/getCheckCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型：0-账户激活，1-忘记密码",dataType = "Integer"),
    })
    public ResponseEntity getCheckCodeNonLogin(String phone,String email,Integer type) throws Exception {
        return RestResponse.ok(userService.getCheckCodeNonLogin(phone,email,type));
    }

    /**
     * 修改手机号邮箱获取验证码
     * @param phone
     * @return
     * @throws Exception
     */
    @ApiOperation("修改手机号邮箱获取验证码")
    @GetMapping("/getChangePhoneCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "string"),
            @ApiImplicitParam(name = "password", value = "原密码", required = true, dataType = "string"),
    })
    public ResponseEntity getChangePhoneCode(String phone,String email,String password) throws Exception {
        return RestResponse.ok(userService.getChangePhoneCode(phone,email,password));
    }

    /**
     * 校验用户密码
     * @param userId 用户ID
     * @param password 密码
     * @return
     * @throws Exception
     */
    @ApiOperation("校验用户密码")
    @GetMapping("/checkUserPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string")
    })
    public ResponseEntity checkUserPassword(Long userId, String password) throws Exception {
        return RestResponse.ok(userService.checkUserPassword(userId, password));
    }
    /**
     * 刷新Token
     * @param userId 用户ID
     * @param token 原始token
     * @return
     * @throws Exception
     */
    @ApiOperation("刷新Token")
    @GetMapping("/refreshToken")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "string"),
            @ApiImplicitParam(name = "token", value = "原始token", required = true, dataType = "string")
    })
    public ResponseEntity refreshToken(String userId, String token) throws Exception {
        return RestResponse.ok(userService.refreshToken(userId, token));
    }
    /**
     * 校验Token是否有效
     * @param token token信息
     * @return
     * @throws Exception
     */
    @ApiOperation("校验Token是否有效")
    @GetMapping("/verifyToken")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token信息", required = true, dataType = "string")
    })
    public String verifyToken(String token) throws Exception {
        return userService.verifyToken(token);
    }
    /**
     * 获取tonken中相关信息
     * @param token token信息
     * @return
     * @throws Exception
     */
    @ApiOperation("获取tonken中相关信息")
    @GetMapping("/queryTokenBody")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token信息", required = true, dataType = "string")
    })
    public String queryTokenBody(String token) throws Exception {
        return userService.queryTokenBody(token);
    }
    /**
     * 根据角色id查询账号列表
     * @param roleIds
     * @return
     */
    @ApiOperation("根据角色id查询账号列表")
    @PostMapping("/queryUserListByRoleId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds", value = "角色id", required = true, dataType = "String[]")
    })
    public ResponseEntity queryUserListByRoleId(@RequestBody List<String> roleIds) {
        return RestResponse.ok(userService.queryUserListByRoleId(roleIds));
    }
}
