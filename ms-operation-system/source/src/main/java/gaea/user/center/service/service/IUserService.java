/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import gaea.user.center.service.common.auth.TokenVO;
import gaea.user.center.service.model.dto.User;
import gaea.user.center.service.model.request.UserBasic;
import gaea.user.center.service.model.request.UserQuery;
import gaea.user.center.service.model.request.UserReq;
import gaea.user.center.service.model.response.UserDetailVO;
import gaea.user.center.service.model.response.UserResp;
import gaea.user.center.service.model.response.UserVo;
import java.util.List;

/**
 * <pre>
 * 业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface IUserService
{
    /**
     * 分页查询数据
     * @param pageable
     * @param query
     * @return
     */
    Page<UserVo> queryPage(Pageable pageable, UserQuery query);

    /**
     * <pre>
     * 根据ID查询数据
     * </pre>
     *
     * @param userId Id
     * @return 
     */
    UserDetailVO queryById(Long userId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param user 
     * @return 保存数据ID
     */
    Long insertUser(UserReq user);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param user 
     * @return 更新数量
     */
    int updateUser(UserReq user);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param userId Id
     * @return 删除数量
     */
    int deleteById(Long userId);

    /**
     * 用户校验并返回token
     * @param name
     * @param password
     * @return
     */
    TokenVO login(String name,String password);

    /**
     * 忘记密码
     * @param phone
     * @param code
     * @return
     */
    Boolean forgetPassword(String phone,String code);

    /**
     * 修改密码
     * @param userBasic
     * @return
     */
    Boolean changePassword(UserBasic userBasic);

    /**
     * 根据项目id查询项目拥有者列表
     * @param projectId
     * @return
     * @throws AdamException
     */
    List<User> queryUserListByProjectId(Long projectId);

    /**
     * 批量保存某用户下分配的所有项目列表信息
     * @param userQuery
     * @return
     */
    Integer saveBatchUserProjects(UserQuery userQuery);

    /**
     * 启用被禁用或者休眠状态的账户
     * @param userId
     * @return
     */
    Integer enableUserById(Long userId);

    /**
     * 禁用账户
     * @param userId
     * @return
     */
    Integer disEnableUserById(Long userId);

    /**
     * 定时任务
     * 每天凌晨执行一次，将过期账户置为禁用状态，精确到天
     */
    void userExpireTimeTask();

    /**
     * 异步校验用户参数唯一性接口
     * @param email
     * @param  id
     * @return
     */
    String userParamValidate(String email,String phone,Long id);

    /**
     * 定时任务
     * 每天凌晨执行一次，将长期（60天）未登录账户置为休眠状态，精确到天
     */
    void userLastLoginTimeoutTask();

    /**
     * 登出
     */
    void loginout();

//    /**
//     * 账户激活-获取验证码
//     * @param email 账户邮箱
//     * @param phone 账户手机号
//     * @return
//     */
//    Integer checkCode(String email, String phone);

    /**
     * 账户激活-验证手机验证码是否有效
     * @param code 手机验证码
     * @param phone 账户手机号
     * @param email 账户邮箱
     * @return
     */
    Integer activation(String phone,String email, String code);

    /**
     * 用户激活链接
     * @param phone 手机号
     */
    String activationUrl(String phone) throws Exception;

    /**
     * 修改手机号
     * @param uid
     * @param phone
     * @return
     */
    Integer changePhone(Long uid,String phone,String code,String password);

    /**
     * 获取手机验证码-无需登录
     * @param phone
     * @return
     */
    Integer getCheckCodeNonLogin(String phone,String email,Integer type);

    /**
     * 修改手机号获取手机验证码
     * @param phone
     * @param email
     * @param password
     * @return
     */
    Integer getChangePhoneCode(String phone, String email, String password);
    /**
     * 校验用户名密码是否正确
     * @param userId
     * @param password
     * @return
     */
    public Integer checkUserPassword(Long userId, String password);
    /**
     * 刷新用户Token
     * @param userId
     * @param token
     * @return
     */
    public String refreshToken(String userId, String token);
    /**
     * 校验Token是否有效
     * @param token
     * @return
     */
    public String verifyToken(String token);
    /**
     * 获取tonken中相关信息
     *
     * @param token
     * @return
     */
    public String queryTokenBody(String token);
    /**
     * 根据角色id查询账号列表
     * @param roleIds
     * @return
     */
    public List<UserResp> queryUserListByRoleId(List<String> roleIds);

    /**
     * 查询用户信息
     * @param userIds
     * @return
     */
    List<UserVo> getUserInfoByIds(List<String> userIds);
}
