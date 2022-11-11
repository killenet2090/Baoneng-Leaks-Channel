package gaea.user.center.service.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @ClassName: UserBasic
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: jiankang
 * @date: 2020/5/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */

@Getter
@Setter
@ApiModel(description = "用户基础实体")
public class UserBasic implements Serializable
{

    private Long userId;

    @ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
    @Length(max = 50, message = "{User.password.maxLength.message}")
    private String password;

    @ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
    @Length(max = 50, message = "{User.newloginPwd.maxLength.message}")
    private String newLoginPwd;

    @ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
    @Length(max = 50, message = "{User.repeatPwd.maxLength.message}")
    private String repeatPwd;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
