package gaea.user.center.service.models.domain;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "OTA用户中心-组织对象数据", description = "OTA用户中心-组织对象参数")
public class Organization {

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", name = "id", required = true)
	private Integer id; 
    
    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用id，关联tb_application，属于哪个应用的权限", name = "id", required = true)
	private Integer appId; 
    
    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称", name = "orgName", required = true)
	private String orgName;
    
    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码，比如00，00_11，00_11_22", name = "orgCode", required = true)
	private String orgCode;
    
    /**
     * 组织级别
     */
    @ApiModelProperty(value = "组织级别，用于权限树的级别，深度，配合编码使用", name = "level", required = true)
	private Integer level; 

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
