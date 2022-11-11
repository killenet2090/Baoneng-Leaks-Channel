package gaea.user.center.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@ApiModel(value = "资源类型响应实体", description = "资源类型响应实体")
@Data
public class PrivilegeTypeVo {
    @ApiModelProperty(value = "资源类型ID", name = "id")
    private String id;

    @ApiModelProperty(value = "资源类型名称", name = "name")
    private String name;

    @ApiModelProperty(value = "资源类型编码", name = "code")
    private String code;

    @ApiModelProperty(value = "资源类型状态：0-未删除，1-已删除", name = "delFlag")
    private Integer delFlag;

    @ApiModelProperty(value = "创建人", name = "createBy")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    @ApiModelProperty(value = "更新人", name = "updateBy")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    private Date updateTime;
}
