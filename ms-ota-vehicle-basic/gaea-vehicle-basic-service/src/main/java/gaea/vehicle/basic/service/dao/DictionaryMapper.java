/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.dao;

import gaea.vehicle.basic.service.models.domain.Dictionary;
import gaea.vehicle.basic.service.models.query.DictionaryQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 *   数据操作对象
 * </pre>
 *
 * @author   jiankang.xia
 * @version  1.0.0
 */
@Mapper
public interface DictionaryMapper {
    /**
     * <pre>
     *  分页查询 数据
     * </pre>
     *
     * @param query 查询条件
     * @return 数据集合
     */
    List<Dictionary> queryPage(DictionaryQuery query);

    /**
     * <pre>
     * 根据查询条件统计数量
     * </pre>
     *
     * @param query 查询条件
     * @return 数量
     */
    int countPage(DictionaryQuery query);

    /**
     * <pre>
     *  根据ID查询数据
     * </pre>
     *
     * @param dictionaryId Id
     * @return 单条记录数据
     */
    Dictionary queryById(Long dictionaryId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param dictionary 数据信息
     */
    void insertDictionary(Dictionary dictionary);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param dictionary 数据信息
     * @return 修改数量
     */
    int updateDictionary(Dictionary dictionary);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param dictionaryId Id
     * @return 删除数量
     */
    int deleteById(Long dictionaryId);

 }
