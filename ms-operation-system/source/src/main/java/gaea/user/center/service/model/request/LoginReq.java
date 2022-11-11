/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

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
@ApiModel(description = "用户登录实体")
public class LoginReq implements Serializable
{
	/**
	 * <pre>
	 *  登录名;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
	@NotEmpty(message = "{User.name.notEmpty.message}")
	@Length(max = 50, message = "{User.name.maxLength.message}")
    private String name;


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
	 */

	@ApiModelProperty(value = "登录密码", example = "测试", dataType = "String")
	@NotEmpty(message = "{User.password.notEmpty.message}")
	@Length(max = 255, message = "{User.password.maxLength.message}")
    private String password;

	/**
	 * <pre>
	 *  用户所在组织id，关联tb_organization表;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "用户所在组织id，关联tb_organization表", example = "1000", dataType = "Long")
    private Long orgId;


	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
