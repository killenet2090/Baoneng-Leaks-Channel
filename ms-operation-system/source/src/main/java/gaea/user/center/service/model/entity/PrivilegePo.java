package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;


@ToString
@ApiModel(value = "OTA用户中心-权限对象数据", description = "OTA用户中心-权限对象参数")
@TableName("tb_privilege")
@Data
public class PrivilegePo extends BasePo {

    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "id", required = true)
    @TableId(value = "id",type = IdType.AUTO)
    @Id
	private Long id;
    
    /**
     * 父权限ID
     */
    @TableField(value = "parent_id",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "父权限ID", name = "parentId", required = true)
	private Integer parentId;
    
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称", name = "name", required = false)
	private String name;
    
    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码，比如00，00_11，00_11_22", name = "code", required = true)
	private String code;

    /**
     * 资源信息
     */
    @ApiModelProperty(value = "资源信息", name = "resource", required = false)
	private String resource;
    
    /**
     * 资源信息接口
     */
    @ApiModelProperty(value = "资源信息接口", name = "resourceInterface", required = false)
	private String resourceInterface;
    
    /**
     * 级别，用于权限树的级别
     */
    @ApiModelProperty(value = "级别，用于权限树的级别", name = "level", required = true)
	private Integer level;
    
    /**
     * 权限类型
     */
    @ApiModelProperty(value = "权限类型，0-菜单，1-路径，2-按钮", name = "type", required = true)
	private Integer type;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "功能类型：0-新增，1-删除，2-编辑，3-查询，4-启用，5-停用，6-升级", name = "opType", required = true)
    private Integer opType;
    
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注信息", name = "remark", required = false)
	private String remark;

    @ApiModelProperty(value = "资源类型id", name = "typeId")
    private Long typeId;
}
