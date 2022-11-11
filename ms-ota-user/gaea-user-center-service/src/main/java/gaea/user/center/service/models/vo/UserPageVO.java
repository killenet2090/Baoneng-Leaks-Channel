package gaea.user.center.service.models.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @ClassName: UserPageVO
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: jiankang
 * @date: 2020/4/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */

@Data
@ApiModel(description = "用户模型")
public class UserPageVO {

    @ApiModelProperty(value = "用户ID", example = "用户ID", dataType = "String")
    private String userId;

    @ApiModelProperty(value = "显示名", example = "显示名", dataType = "String")
    private String displayName;

    @ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
    private String loginName;

    @ApiModelProperty(value = "邮箱", example = "邮箱", dataType = "String")
    private String email;

    @ApiModelProperty(value = "手机", example = "手机", dataType = "String")
    private String phone;

    @ApiModelProperty(value = "用户所在组织", example = "伦敦007CIA中情局", dataType = "String")
    private String orgName;

    @ApiModelProperty(value = "用户所有角色", example = "007 / james bound ", dataType = "String")
    private String roleName;



}
