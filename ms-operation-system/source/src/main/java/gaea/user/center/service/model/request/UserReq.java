/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.center.service.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import gaea.user.center.service.common.validate.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class UserReq implements Serializable
{
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
	 *  登录名;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
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
	@Length(max = 257, message = "{User.password.maxLength.message}")
    private String password;

	/**
	 * <pre>
	 *  是否启用;字段长度:4,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否启用:-1-休眠，0-禁用，1-正常", example = "1", dataType = "Integer")
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
	@ApiModelProperty(value = "0-未删除，1-已删除", example = "1", dataType = "Integer")
    private Integer delFlag;

	@ApiModelProperty(value = "所属角色列表", example = "[1,2,3]", dataType = "List")
	private List<Integer> roles;

	@ApiModelProperty(value = "项目ID列表", example = "[1,2,3]", dataType = "List")
	private List<Integer> projects;

	/**
	 * 过期时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	@ApiModelProperty(value = "过期时间", name = "expireTime")
	private Date expireTime;

	@ApiModelProperty(value = "备注", name = "remark")
	private String remark;

	@ApiModelProperty(value = "配置ID列表", example = "[1,2,3]", dataType = "List")
	private List<Long> models;

//	@ApiModelProperty(value = "配置名称列表,必须与models一一对应", example = "[AAA]", dataType = "List")
//	private List<String> modelNames;

	@ApiModelProperty(value = "标签ID列表", example = "[1,2,3]", dataType = "List")
	private List<Long> labels;

//	@ApiModelProperty(value = "标签名称列表,必须与lables一一对应", example = "[1,2,3]", dataType = "List")
//	private List<String> lableNames;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
