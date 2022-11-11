package gaea.user.center.service.models.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <pre>
 * 默认请求参数基础父类
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
public abstract class AbstractReq implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
