/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.service;

import gaea.user.center.service.models.domain.User;
import gaea.user.center.service.models.query.UserQuery;
import gaea.user.center.service.models.request.UserReq;
import gaea.user.center.service.models.vo.UserDetailVO;

import java.util.List;

/**
 * <pre>
 * 业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface UserService {
    /**
     * <pre>
     * 分页查询数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 数据集合
     */
    List<User> queryPage(UserQuery query);

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

}
