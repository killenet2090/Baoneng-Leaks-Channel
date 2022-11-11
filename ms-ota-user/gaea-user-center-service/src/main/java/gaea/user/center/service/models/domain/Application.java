package gaea.user.center.service.models.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "OTA用户中心-应用对象参数", description = "OTA用户中心-应用对象参数")
public class Application {

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID", name = "id", required = true)
	private Integer id; 
	
    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称", name = "appName", required = true)
	private String appName;
}
