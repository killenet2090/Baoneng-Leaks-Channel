package gaea.user.center.service.model.response;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: UserPageVO
 * @Description: 用户分页实体
 * @author: jiankang
 * @date: 2020/4/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */

@Data
@ApiModel(description = "用户模型")
public class UserPageVo extends BasePo
{

    @ApiModelProperty(value = "用户ID", example = "用户ID", dataType = "String")
    private String id;

    @ApiModelProperty(value = "显示名", example = "显示名", dataType = "String")
    private String displayName;

    @ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
    private String name;

    @ApiModelProperty(value = "邮箱", example = "邮箱", dataType = "String")
    private String email;

    @ApiModelProperty(value = "手机", example = "手机", dataType = "String")
    private String phone;

    @ApiModelProperty(value = "用户所在组织", example = "伦敦007CIA中情局", dataType = "String")
    private String orgName;

    @ApiModelProperty(value = "用户所有角色", example = "007 / james bound ", dataType = "String")
    private String roleName;


    @ApiModelProperty(value = "账户状态：-1-休眠，0-禁用，1-正常")
    private Integer isEnable;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "过期时间", name = "expireTime")
    private Date expireTime;

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

}
