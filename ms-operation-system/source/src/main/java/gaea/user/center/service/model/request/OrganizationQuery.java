package gaea.user.center.service.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@ApiModel(value = "组织对象数据", description = "组织对象参数")
public class OrganizationQuery {
    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", name = "id", required = true)
    private Integer id;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称", name = "name", required = true)
    private String name;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码，比如00，00_11，00_11_22", name = "code", required = true)
    private String code;

    /**
     * 组织级别
     */
    @ApiModelProperty(value = "组织级别，用于权限树的级别，深度，配合编码使用", name = "level", required = true)
    private Integer level;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "0-未删除，1-已删除", name = "delFlag", required = true)
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "更新时间", name = "createTime", required = true)
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 更新时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", name = "updateTime", required = true)
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;
}
