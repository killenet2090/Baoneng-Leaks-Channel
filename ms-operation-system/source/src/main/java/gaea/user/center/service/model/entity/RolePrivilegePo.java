package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@ToString
@ApiModel(value = "OTA用户中心-权限主体与权限关系对象数据", description = "OTA用户中心-权限主体与权限关系对象参数")
@TableName("tb_role_privilege")
public class RolePrivilegePo {
    /**
     * 关系ID
     */
    @ApiModelProperty(value = "关系ID", name = "id", required = true)
    @TableId(value = "id",type = IdType.AUTO)
    @Id
    private Integer id;
    /**
     * 权限主体id
     */
    @ApiModelProperty(value = "权限主体id", name = "roleId", required = true)
    private Integer roleId;
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
     * 选择类型
     */
    @ApiModelProperty(value = "选择状态，0半选；1全选", name = "selectStatus", required = false)
    private Integer selectStatus;
    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "createTime", required = true)
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "updateTime", required = true)
    private Date updateTime;
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "0-未删除，1-已删除", name = "delFlag", required = false)
    private Integer delFlag;
}
