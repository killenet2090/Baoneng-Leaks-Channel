package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import gaea.user.center.service.model.dto.RolePrivilege;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "OTA用户中心-角色对象数据", description = "OTA用户中心-角色对象参数")
@TableName("tb_role")
public class RolePo
{

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", name = "id", required = true)
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", name = "orgId", required = false)
    private Integer orgId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", name = "name", required = true)
    private String name;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "0-未删除，1-已删除", name = "delFlag", required = true)
    private Integer delFlag;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "描述", name = "remark", required = false)
    private String remark;

    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创新时间", name = "createTime", required = true)
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 更新时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", name = "updateTime", required = true)
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;
}