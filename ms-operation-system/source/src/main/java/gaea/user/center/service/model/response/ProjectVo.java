package gaea.user.center.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@ApiModel(value = "项目响应实体", description = "项目响应实体")
@Data
public class ProjectVo {
    /**
     * 应用ID
     */
    @ApiModelProperty(value = "项目信息_ID", name = "id")
    private String id;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目信息_名称", name = "name")
    private String name;

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目信息_编码", name = "code")
    private String code;

    /**
     *项目所属机构
     */
    @ApiModelProperty(value = "项目信息_机构", name = "mechanism")
    private String mechanism;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "项目信息_用户ID", name = "userId")
    private Long userId;
    /**
     *项目描述
     */
    @ApiModelProperty(value = "项目信息_描述", name = "description")
    private String description;

    @ApiModelProperty(value = "项目信息_描述", name = "createBy")
    private String createBy;

    @ApiModelProperty(value = "项目信息_描述", name = "updateBy")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "项目信息_描述", name = "createTime")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "项目信息_描述", name = "updateTime")
    private Date updateTime;

    @ApiModelProperty(value = "项目信息_是否删除：0-未删除，1-已删除", name = "delFlag")
    private Integer delFlag;

    @ApiModelProperty(value = "用户IDs", example = "1,2,3", name = "userIds")
    private String userIds;

}
