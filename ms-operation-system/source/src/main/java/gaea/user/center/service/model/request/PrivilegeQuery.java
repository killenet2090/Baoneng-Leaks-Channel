package gaea.user.center.service.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@ToString
@ApiModel(value = "权限对象数据", description = "权限对象参数")
@Data
public class PrivilegeQuery implements Serializable {

    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "id")
	private String id;
    
    /**
     * 父权限ID
     */
    @ApiModelProperty(value = "父权限ID", name = "parentId")
	private Integer parentId;
    
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称", name = "name")
	private String name;

    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码，比如00，00_11，00_11_22", name = "code")
	private String code;

    /**
     * 资源信息
     */
    @ApiModelProperty(value = "资源信息", name = "resource")
	private String resource;
    
    /**
     * 资源信息接口
     */
    @ApiModelProperty(value = "资源信息接口", name = "resourceInterface")
	private String resourceInterface;
    
    /**
     * 级别，用于权限树的级别
     */
    @ApiModelProperty(value = "级别，用于权限树的级别", name = "level")
	private Integer level;
    
    /**
     * 权限类型
     */
    @ApiModelProperty(value = "权限类型，0-菜单，1-路径，2-按钮", name = "type")
	private Integer type;

    /**
     * 权限类型
     */
    @ApiModelProperty(value = "功能类型：0-新增，1-删除，2-编辑，3-查询，4-启用，5-停用，6-升级", name = "opType")
    private Integer opType;
    
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注信息", name = "remark")
	private String remark;
    
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "0-未删除，1-已删除", name = "delFlag")
	private Integer delFlag;

    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    private String createBy;
    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    private Date updateTime;
    /**
     * 更新人id
     */
    @ApiModelProperty("更新人id")
    private String updateBy;

    @ApiModelProperty("类型id")
    private Long typeId;
}
