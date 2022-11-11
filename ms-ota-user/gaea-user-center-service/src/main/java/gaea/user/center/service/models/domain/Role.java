package gaea.user.center.service.models.domain;

import java.util.Date;
import java.util.List;

import gaea.user.center.service.models.query.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "OTA用户中心-角色对象数据", description = "OTA用户中心-角色对象参数")
public class Role extends Page{

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", name = "id", required = true)
	private Integer id;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID", name = "appId", required = true)
	private Integer appId;
    
    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", name = "orgId", required = false)
	private Integer orgId;
    
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", name = "roleName", required = true)
	private String roleName;
    
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效，1有效；0无效", name = "isActive", required = true)
	private Integer isActive;
    
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "描述", name = "remark", required = false)
	private String remark;
    
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createTime", required = true)
	private Date createTime;
	
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "updateTime", required = true)
	private Date updateTime;
	
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "createBy", required = false)
	private String createBy;
    
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", name = "updateBy", required = false)
	private String updateBy;
    
    /**
     * 权限角色关系对象
     */
    @ApiModelProperty(value = "权限角色关系对象", name = "subjectPrivilege", required = false)
	private List<SubjectPrivilege> subjectPrivilege;
}
