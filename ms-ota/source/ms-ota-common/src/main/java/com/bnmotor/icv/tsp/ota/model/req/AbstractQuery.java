package com.bnmotor.icv.tsp.ota.model.req;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <pre>
 * 默认查询条件对象,主要提供后续对公用查询条件参数,
 * 这里继承了分页,后续的查询条件不在单独需要继承.
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
public abstract class AbstractQuery implements Serializable
{

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
