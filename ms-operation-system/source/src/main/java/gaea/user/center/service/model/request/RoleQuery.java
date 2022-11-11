package gaea.user.center.service.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import gaea.user.center.service.model.dto.RolePrivilege;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "角色对象数据", description = "角色对象参数")
public class RoleQuery implements Serializable {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", name = "id", required = false)
    private Long id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", name = "name", required = false)
    private String name;
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "0-未删除，1-已删除", name = "delFlag", required = false)
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
    @ApiModelProperty(value = "创新时间", name = "createTime", required = false)
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "createBy", required = false)
    private String createBy;
}
