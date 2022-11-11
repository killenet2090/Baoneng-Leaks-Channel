/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *   查询条件,专门提供查询使用，请勿当成提交数据保存使用，
 *	如果有必要新增额外领域模型数据，可以单独创建，请勿使用查询对象做提交数据保存
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "用户查询实体")
public class UserQuery implements Serializable
{
    private Long userId;

    @ApiModelProperty(value = "用户所在组织id，关联tb_organization表", example = "1000", dataType = "Long")
    private Long orgId;

    @ApiModelProperty(value = "所属角色，1", example = "1", dataType = "int")
    @NotNull(message = "所属角色不能为空")
    private Integer roleId;

    @ApiModelProperty(value = "显示名", example = "显示名", dataType = "String")
    @Length(max = 50, message = "{User.displayName.maxLength.message}")
    private String displayName;

    @ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
    @Length(max = 50, message = "{User.name.maxLength.message}")
    private String name;

    @ApiModelProperty(value = "手机号", dataType = "String")
    private String phone;

    @ApiModelProperty(value = "项目ids", example = "1,2,3", dataType = "String")
    private String projectIds;

    @ApiModelProperty(value = "账户状态：-1-休眠，0-禁用，1-正常", example = "1", dataType = "Integer")
    private Integer isEnable;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "过期时间", name = "expireTime")
    private Date expireTime;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
