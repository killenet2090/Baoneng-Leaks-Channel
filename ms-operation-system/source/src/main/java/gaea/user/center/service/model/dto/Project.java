package gaea.user.center.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: Project
 * @Description: 项目实体
 * @author: jiangchangyuan1
 * @date: 2020/8/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class Project implements Serializable {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目ID", name = "id", required = true)
    private Long id;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", name = "name", required = true)
    private String name;

    /**
     * 项目编号
     */
    @ApiModelProperty(value = "项目编号", name = "code", required = true)
    private String code;

    /**
     * 项目编号
     */
    @ApiModelProperty(value = "项目所属机构", name = "mechanism", required = true)
    private String mechanism;

    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "createTime", required = true)
    private Date createTime;

    /**
     * 更新时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", name = "updateTime", required = true)
    private Date updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人id", name = "createBy", required = true)
    private String createBy;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人id", name = "updateBy", required = true)
    private String updateBy;

    /**
     * 是否有效：0-无效，1-有效
     */
    @ApiModelProperty(value = "0-未删除，1-已删除", name = "delFlag", required = true)
    private Integer delFlag;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
