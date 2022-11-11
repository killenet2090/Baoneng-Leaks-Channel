package gaea.user.center.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: UserPageVO
 * @Description: 用户详情实体
 * @author: jiankang
 * @date: 2020/4/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */

@Data
@ApiModel(description = "用户模型")
public class UserDetailVO
{
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "用户ID", dataType = "String")
    private String userId;

    /**
     * 显示名
     */
    @ApiModelProperty(value = "显示名", example = "显示名", dataType = "String")
    private String displayName;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名", example = "测试", dataType = "String")
    private String name;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", example = "邮箱", dataType = "String")
    private String email;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机", example = "手机", dataType = "String")
    private String phone;

    /**
     * 用户所在组织名称
     */
    @ApiModelProperty(value = "用户所在组织", example = "伦敦007CIA中情局", dataType = "String")
    private String orgName;

    /**
     * 用户所有组织
     */
    @ApiModelProperty(value = "用户所在组织", example = "伦敦007CIA中情局", dataType = "Long")
    private Long orgId;

    /**
     * 用户所有组织
     */
    @ApiModelProperty(value = "用户所在角色名称", example = "伦敦007CIA中情局", dataType = "String")
    private String roleName;

    /**
     * 用户所有角色
     */
    @ApiModelProperty(value = "用户所有角色", example = "007 / james bound ", dataType = "String")
    private List<Long> roles;

    /**
     * 用户所有项目
     */
    @ApiModelProperty(value = "用户所有项目", example = "GX16 ", dataType = "String")
    private List<Long> projects;

    /**
     * 用户所有车辆集
     */
    @ApiModelProperty(value = "用户所有车辆集", dataType = "String")
    private List<UserCarAssemblyVO> userCarAssemblyVos;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "过期时间", name = "expireTime")
    private Date expireTime;
    /**
     * 账户状态
     */
    @ApiModelProperty(value = "账户状态：-1-休眠，0-禁用，1-正常")
    private Integer isEnable;

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

}
