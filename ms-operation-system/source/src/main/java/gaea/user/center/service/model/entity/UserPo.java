package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@TableName("tb_user")
@Data
public class UserPo extends BasePo {
    /**
     * <pre>
     * 数据库字段: id
     * 描述: 用户id;字段长度:20,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("用户id")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * <pre>
     * 数据库字段: login_name
     * 描述: 登录名;字段长度:50,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("登录名")
    private String name;

    @ApiModelProperty(value = "邮箱",  dataType = "String")
    private String email;

    @ApiModelProperty(value = "手机", dataType = "String")
    private String phone;
    /**
     * <pre>
     * 数据库字段: login_pwd
     * 描述: 登录密码;字段长度:50,是否必填:是。
     * </pre>
     */
    @JsonIgnore
    @ApiModelProperty("登录密码")
    private String password;
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

//    /**
//     * 角色名
//     */
//    @ApiModelProperty("角色名")
//    private String roleName;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "过期时间", name = "expireTime", required = true)
    private Date expireTime;

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
