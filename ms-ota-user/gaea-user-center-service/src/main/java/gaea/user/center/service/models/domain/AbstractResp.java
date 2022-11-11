package gaea.user.center.service.models.domain;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <pre>
 * 返回结果参数抽象父类
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
@Setter
@Getter
public abstract class AbstractResp implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
