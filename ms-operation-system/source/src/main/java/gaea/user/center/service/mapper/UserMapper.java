/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.dto.User;
import gaea.user.center.service.model.entity.UserPo;
import gaea.user.center.service.model.request.UserQuery;
import gaea.user.center.service.model.response.UserDetailVO;
import gaea.user.center.service.model.response.UserPageVo;
import gaea.user.center.service.model.response.UserResp;
import gaea.user.center.service.model.response.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <pre>
 *   <b>表名</b>：tb_user
 *   数据操作对象
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Component
public interface UserMapper extends AdamMapper<UserPo> {
    /**
     * <pre>
     *  分页查询 数据
     * </pre>
     *
     * @param query 查询条件
     * @return 数据集合
     */
    Page<UserPageVo> queryPage(IPage iPage, @Param("query") UserQuery query);

    /**
     * <pre>
     * 根据查询条件统计数量
     * </pre>
     *
     * @param query 查询条件
     * @return 数量
     */
    int countPage(UserQuery query);

    /**
     * <pre>
     *  根据ID查询数据
     * </pre>
     *
     * @param userId Id
     * @return 单条记录数据
     */
    UserDetailVO queryById(Long userId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param user 数据信息
     */
    Integer insertUser(User user);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param user 数据信息
     * @return 修改数量
     */
    int updateUser(User user);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param userId Id
     * @return 删除数量
     */
    int deleteById(Long userId);

    User queryByUserFiled(User user);

    /**
     * 根据项目id查询拥有者列表
     * @param projectId
     * @return
     */
    List<User> queryUserListByProjectId(Long projectId);

    /**
     * 查询用户列表
     * @param userQuery
     * @return
     */
    List<UserPo> getUserList(@Param("query") UserQuery userQuery);

    /**
     * 根据用户名精确查找用户信息
     * @param userName
     * @return
     */
    UserPo queryByUserName(String userName);

    /**
     * 根据角色id查询账号列表
     * @param roleIds
     * @return
     */
    List<UserResp> queryUserListByRoleId(@Param("roleIds")List<String> roleIds);

    /**
     * 查询用户信息
     * @param userIds
     * @return
     */
    List<UserVo> getUserInfoByIds(@Param("userIds") List<String> userIds);
}
