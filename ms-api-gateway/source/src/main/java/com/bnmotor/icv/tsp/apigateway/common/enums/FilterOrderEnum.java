package com.bnmotor.icv.tsp.apigateway.common.enums;

import com.bnmotor.icv.tsp.apigateway.common.base.BaseEnum;
import lombok.Getter;
import org.springframework.core.Ordered;

/**
 * @ClassName: FilterOrderEnum
 * @Description: 过滤器序号
 * @author: huangyun1
 * @date: 2020/5/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Getter
public enum FilterOrderEnum implements BaseEnum<Integer> {
    /**
     * 日志记录过滤器排序
     */
    LOG_RECORD("日志记录过滤器",Ordered.HIGHEST_PRECEDENCE + 100),
    /**
     * 忽略鉴权过滤器排序
     */
    IGNORE_TOKEN_AUTHENTICATION("忽略鉴权过滤器",Ordered.HIGHEST_PRECEDENCE + 200),
    /**
     * 鉴权过滤器
     */
    TOKEN_AUTHENTICATION("鉴权过滤器",Ordered.HIGHEST_PRECEDENCE + 400),
    /**
     * 获取用户基础信息
     */
    GET_USER_INFO("获取用户基础信息过滤器",Ordered.HIGHEST_PRECEDENCE + 500),
    /**
     * 添加公共信息
     */
    ADD_COMMON_INFO("添加公共信息过滤器",Ordered.HIGHEST_PRECEDENCE + 600);
        
    private String name;
    private int index;
    
    private FilterOrderEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String getDescription() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.index;
    }
}
