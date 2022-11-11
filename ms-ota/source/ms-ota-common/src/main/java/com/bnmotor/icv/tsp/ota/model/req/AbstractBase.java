package com.bnmotor.icv.tsp.ota.model.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public abstract class AbstractBase implements Serializable
{
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
