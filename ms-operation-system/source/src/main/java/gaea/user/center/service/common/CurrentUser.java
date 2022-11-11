package gaea.user.center.service.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 当前登录用户信息
 *
 * @author yuhb1
 * @version 1.0.0
 */
@Data
@Api("用户信息")
public class CurrentUser {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id")
    private String userName;
}
