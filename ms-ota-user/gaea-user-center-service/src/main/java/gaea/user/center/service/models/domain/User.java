/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.models.domain;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * <pre>
 *	<b>表名</b>：tb_user
 *  ，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("")
public class User extends AbstractBase {
	/**
	 * <pre>
	 * 数据库字段: id
	 * 描述: 用户id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("用户id")
    private Long id;
	/**
	 * <pre>
	 * 数据库字段: app_id
	 * 描述: 应用id，关联tb_application，属于哪个应用的权限;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("应用id，关联tb_application，属于哪个应用的权限")
    private Long appId;
	/**
	 * <pre>
	 * 数据库字段: login_name
	 * 描述: 登录名;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("登录名")
    private String loginName;

	@ApiModelProperty(value = "邮箱", example = "邮箱", dataType = "String")
	private String email;

	@ApiModelProperty(value = "手机", example = "手机", dataType = "String")
	private String phone;
	/**
	 * <pre>
	 * 数据库字段: login_pwd
	 * 描述: 登录密码;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@JsonIgnore
	@ApiModelProperty("登录密码")
    private String loginPwd;
	/**
	 * <pre>
	 * 数据库字段: is_enable
	 * 描述: 是否启用;字段长度:4,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("是否启用")
    private Integer isEnable;
	/**
	 * <pre>
	 * 数据库字段: display_name
	 * 描述: 显示名;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("显示名")
    private String displayName;
	/**
	 * <pre>
	 * 数据库字段: org_id
	 * 描述: 用户所在组织id，关联tb_organization表;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("用户所在组织id，关联tb_organization表")
    private Long orgId;
	/**
	 * <pre>
	 * 数据库字段: is_active
	 * 描述: 是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("是否有效，1有效；0无效")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
