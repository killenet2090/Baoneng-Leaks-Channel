package gaea.user.center.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;


@ToString
@ApiModel(value = "权限对象数据", description = "权限对象参数")
@Data
public class PrivilegeVo
{
    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "id")
	private String id;
    /**
     * 父权限ID
     */
    @ApiModelProperty(value = "父权限ID", name = "parentId")
	private String parentId;
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称", name = "name")
	private String name;
    /**
     * 资源信息
     */
    @ApiModelProperty(value = "资源信息", name = "resource")
	private String resource;
    /**
     * 权限类型
     */
    @ApiModelProperty(value = "权限类型，1功能项；0菜单项", name = "type")
	private Integer type;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注信息", name = "remark")
	private String remark;
    /**
     * 是否公共资源
     */
    @ApiModelProperty(value = "是否公共资源，0-非公共资源，1-公共资源", name = "isPublic")
    private Integer isPublic;
    /**
     * 是否可见
     */
    @ApiModelProperty(value = "是否可见，0-可见，1-不可见", name = "isVisual")
    private Integer isVisual;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    private Date updateTime;
    /**
     * 更新人id
     */
    @ApiModelProperty("更新人")
    private String updateBy;
}
