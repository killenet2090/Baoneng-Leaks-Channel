package gaea.user.center.service.models.domain;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "OTA用户中心-权限主体与权限关系对象数据", description = "OTA用户中心-权限主体与权限关系对象参数")
public class SubjectPrivilege {

    /**
     * 关系ID
     */
    @ApiModelProperty(value = "关系ID", name = "id", required = true)
	private Integer id;
    
    /**
     * 权限主体id
     */
    @ApiModelProperty(value = "权限主体id", name = "subjectId", required = true)
	private Integer subjectId;
    
    /**
     * 权限主体类型
     */
    @ApiModelProperty(value = "权限主题类型：1：角色", name = "subjectType", required = true)
	private Integer subjectType;
    
    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "privilegeId", required = true)
	private Integer privilegeId;
    
    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码", name = "privilegeCode", required = true)
	private String privilegeCode;
    
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
