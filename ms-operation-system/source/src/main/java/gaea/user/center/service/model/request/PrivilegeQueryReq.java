package gaea.user.center.service.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@Getter
@Setter
@ToString
@ApiModel(value = "权限查询参数", description = "权限查询参数")
public class PrivilegeQueryReq implements Serializable
{

    /**
     * 权限类型
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
	private Long userId;
    
    /**
     * 权限类型
     */
    @ApiModelProperty(value = "资源类型", name = "typeId", required = false)
	private Long typeId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
