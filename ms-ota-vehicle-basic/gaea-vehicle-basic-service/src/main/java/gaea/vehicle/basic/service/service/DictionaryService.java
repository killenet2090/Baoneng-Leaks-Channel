/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.Dictionary;
import gaea.vehicle.basic.service.models.query.DictionaryQuery;

import java.util.List;

/**
 * <pre>
 * 业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface DictionaryService {
    /**
     * <pre>
     * 分页查询数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 数据集合
     */
    List<Dictionary> queryPage(DictionaryQuery query);

    /**
     * <pre>
     * 根据ID查询数据
     * </pre>
     *
     * @param dictionaryId Id
     * @return 
     */
    Dictionary queryById(Long dictionaryId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param dictionary 
     * @return 保存数据ID
     */
    int insertDictionary(Dictionary dictionary);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param dictionary 
     * @return 更新数量
     */
    int updateDictionary(Dictionary dictionary);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param dictionaryId Id
     * @return 删除数量
     */
    int deleteById(Long dictionaryId);

}
