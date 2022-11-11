package gaea.user.center.service.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "返回指定的角色数据", description = "返回指定的角色数据")
public class RoleResp
{
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", name = "id")
	private Integer id;
    
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", name = "name")
	private String name;
}
