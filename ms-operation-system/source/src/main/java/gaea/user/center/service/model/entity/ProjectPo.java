package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_project")
@ApiModel(value="ProjectPo对象", description="项目信息表")
public class ProjectPo extends BasePo {

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "项目信息_ID", name = "id", required = true)
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目信息_名称", name = "name", required = true)
    private String name;

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目信息_编码", name = "code", required = true)
    private String code;

    /**
     *项目所属机构
     */
    @ApiModelProperty(value = "项目信息_机构", name = "mechanism", required = true)
    private String mechanism;

    /**
     *项目描述
     */
    @ApiModelProperty(value = "项目信息_描述", name = "description", required = true)
    private String description;


}
