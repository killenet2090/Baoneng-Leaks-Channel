package gaea.user.center.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "返回指定的权限对象数据", description = "返回指定的权限对象参数")
public class PrivilegeResp implements Serializable
{
    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "id", required = true)
	private Integer id;
    
    /**
     * 父权限ID
     */
    @ApiModelProperty(value = "父权限ID", name = "pId", required = false)
	private Integer pId;
    
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称", name = "name", required = true)
	private String name;
    
    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码", name = "code", required = false)
	private String code;

    /**
     * 资源信息
     */
    @ApiModelProperty(value = "资源信息", name = "path", required = false)
	private String path;
    
    /**
     * 资源信息接口
     */
    @ApiModelProperty(value = "资源信息接口", name = "resourceInterface", required = false)
	private String resourceInterface;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式", name = "method", required = false)
    private String method;


    /**
     * 级别，用于权限树的级别
     */
    @ApiModelProperty(value = "级别，用于权限树的级别", name = "level", required = true)
	private Integer level;
    
    /**
     * 权限类型
     */
    @ApiModelProperty(value = "权限类型，1功能项；0菜单项", name = "type", required = true)
	private Integer type;
    /**
     * 是否公共资源
     */
    @ApiModelProperty(value = "是否公共资源，0-非公共资源，1-公共资源", name = "is_public")
    private Integer isPublic;
    /**
     * 是否可见
     */
    @ApiModelProperty(value = "是否可见，0-可见，1-不可见", name = "is_visual")
    private Integer isVisual;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createdBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("资源类型")
    private String typeName;
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
