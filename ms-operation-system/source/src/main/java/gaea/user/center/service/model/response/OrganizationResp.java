package gaea.user.center.service.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "返回指定的组织数据", description = "返回指定的组织数据")
public class OrganizationResp
{

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", name = "id")
	private Integer id;
    
    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称", name = "name")
	private String name;
    
    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码，比如00，00_11，00_11_22", name = "code")
	private String code;
}
