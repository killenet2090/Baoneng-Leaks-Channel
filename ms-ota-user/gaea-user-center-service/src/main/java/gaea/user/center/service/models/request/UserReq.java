/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.models.request;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import gaea.user.center.service.models.domain.AbstractReq;
import gaea.user.center.service.models.validate.Update;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 *  接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "")
public class UserReq extends AbstractReq {
	/**
	 * <pre>
	 *  用户id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "用户id", example = "1000", dataType = "Long")
	@NotNull(message = "{User.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  应用id，关联tb_application，属于哪个应用的权限;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "应用id，关联tb_application，属于哪个应用的权限", example = "1000", dataType = "Long")
	@NotNull(message = "{User.appId.notNull.message}")
    private Long appId;
	/**
	 * <pre>
	 *  登录名;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
	@NotEmpty(message = "{User.loginName.notEmpty.message}")
	@Length(max = 50, message = "{User.loginName.maxLength.message}")
    private String loginName;


	@ApiModelProperty(value = "邮箱", example = "邮箱", dataType = "String")
	@Length(max = 50, message = "{User.邮箱.maxLength.message}")
	private String email;

	@ApiModelProperty(value = "手机", example = "手机", dataType = "String")
	@Length(max = 50, message = "{User.手机.maxLength.message}")
	private String phone;

	/**
	 * <pre>
	 *  登录密码;字段长度:50,是否必填:是。
	 * </pre>

	@ApiModelProperty(value = "登录密码", example = "测试", dataType = "String")
	@NotEmpty(message = "{User.loginPwd.notEmpty.message}")
	@Length(max = 50, message = "{User.loginPwd.maxLength.message}")
    private String loginPwd;
	 */
	/**
	 * <pre>
	 *  是否启用;字段长度:4,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否启用", example = "1", dataType = "Integer")
	@NotNull(message = "{User.isEnable.notNull.message}")
    private Integer isEnable;
	/**
	 * <pre>
	 *  显示名;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "显示名", example = "测试", dataType = "String")
	@Length(max = 50, message = "{User.displayName.maxLength.message}")
    private String displayName;
	/**
	 * <pre>
	 *  用户所在组织id，关联tb_organization表;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "用户所在组织id，关联tb_organization表", example = "1000", dataType = "Long")
    private Long orgId;
	/**
	 * <pre>
	 *  是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否有效，1有效；0无效", example = "1", dataType = "Integer")
	@NotNull(message = "{User.isActive.notNull.message}")
    private Integer isActive;

	@ApiModelProperty(value = "所属角色列表，1,2,3", example = "1", dataType = "List")
	private List<Integer> roles;


	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
