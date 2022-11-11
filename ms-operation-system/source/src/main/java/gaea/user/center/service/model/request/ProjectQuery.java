package gaea.user.center.service.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @ClassName: ProjectQuery
 * @Description: 项目查询参数实体
 * @author: jiangchangyuan1
 * @date: 2020/8/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "项目查询参数")
@Data
public class ProjectQuery implements Serializable {

    private Long userId;

    @ApiModelProperty(value = "项目ID", example = "1", dataType = "String")
    private String id;

    @ApiModelProperty(value = "项目名称", example = "OTA项目", dataType = "String")
    private String name;

    @ApiModelProperty(value = "项目编号", example = "OTA项目编号", dataType = "String")
    private String code;

    @ApiModelProperty(value = "项目所属机构", example = "宝能汽车集团有限公司", dataType = "String")
    private String mechanism;

    @ApiModelProperty(value = "项目描述", example = "宝能汽车集团有限公司", dataType = "String")
    private String description;

    @ApiModelProperty(value = "创建者名称", example = "1", dataType = "String")
    private String createBy;

    @ApiModelProperty(value = "更新者名称", example = "1", dataType = "String")
    private String updateBy;

    @ApiModelProperty(value = "0-未删除，1-已删除", example = "1", dataType = "Integer")
    private Integer delFlag;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
