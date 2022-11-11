package gaea.user.center.service.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "返回指定的用户数据", description = "返回指定的用户数据")
public class UserResp {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "id")
    private Long id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", name = "name")
    private String name;
}
