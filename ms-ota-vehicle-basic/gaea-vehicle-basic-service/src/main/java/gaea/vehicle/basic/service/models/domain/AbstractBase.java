package gaea.vehicle.basic.service.models.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *  基础的数据对象
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
@Getter
@Setter
public abstract class AbstractBase implements Serializable {
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createdBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updatedBy;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
