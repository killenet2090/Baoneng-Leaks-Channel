package com.bnmotor.icv.tsp.cpsp.engine.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;
/**
 * @ClassName: MongoDbUtil
 * @Description:
 * @author: liuhuaqiao1
 * @date: 2020/9/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class MongoUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存数据对象，集合为数据对象中@Document 注解所配置的collection
     *
     * @param obj
     *            数据对象
     */
    public void save(Object obj) {
        mongoTemplate.save(obj);
    }

    /**
     * 指定集合保存数据对象
     *
     * @param obj
     *            数据对象
     * @param collectionName
     *            集合名
     */
    public void save(Object obj, String collectionName) {
        mongoTemplate.save(obj, collectionName);
    }

    /**
     * 查询指定id的数据对象
     *
     * @param id
     *            数据对象
     * @param obj
     *            集合名
     */
    public <T> T findById(String id, Class<T> obj) {
        return mongoTemplate.findById(id, obj);
    }

    /**
     * 指定集合 查询出所有结果集
     * @param clazz
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> clazz, String collectionName) {
        List<T> resultList = mongoTemplate.findAll(clazz, collectionName);
        return resultList;
    }

}
