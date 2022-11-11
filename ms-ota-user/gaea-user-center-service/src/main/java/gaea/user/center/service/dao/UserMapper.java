/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.dao;

import gaea.user.center.service.models.domain.User;
import gaea.user.center.service.models.query.UserQuery;
import gaea.user.center.service.models.vo.UserDetailVO;
import org.apache.ibatis.annotations.Mapper;

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
@Mapper
public interface UserMapper {
    /**
     * <pre>
     *  分页查询 数据
     * </pre>
     *
     * @param query 查询条件
     * @return 数据集合
     */
    List<User> queryPage(UserQuery query);

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

 }
