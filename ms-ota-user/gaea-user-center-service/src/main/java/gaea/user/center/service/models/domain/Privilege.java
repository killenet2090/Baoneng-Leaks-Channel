package gaea.user.center.service.models.domain;

import java.util.Date;

import gaea.user.center.service.models.query.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "OTA用户中心-权限对象数据", description = "OTA用户中心-权限对象参数")
public class Privilege extends Page{

    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "id", required = true)
	private Integer id;
    
    /**
     * 父权限ID
     */
    @ApiModelProperty(value = "父权限ID", name = "parentId", required = false)
	private Integer parentId;
    
    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用id，关联tb_application，属于哪个应用的权限", name = "appId", required = false)
	private Integer appId;
    
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称", name = "privilegeName", required = true)
	private String privilegeName;
    
    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码，比如00，00_11，00_11_22", name = "privilegeCode", required = true)
	private String privilegeCode;

    /**
     * 资源信息
     */
    @ApiModelProperty(value = "资源信息", name = "privilegeCode", required = false)
	private String resource;
    
    /**
     * 级别，用于权限树的级别
     */
    @ApiModelProperty(value = "级别，用于权限树的级别", name = "level", required = true)
	private Integer level;
	
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注信息", name = "remark", required = false)
	private String remark;
    
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效，1有效；0无效", name = "isActive", required = true)
	private Integer isActive;
    
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
}
