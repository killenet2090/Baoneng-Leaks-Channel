package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: DictionaryPo
 * @Description: 字典表 实体类
 * @author xxc
 * @since 2020-06-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Accessors(chain = true)
@TableName("tb_dictionary")
@ApiModel(value="DictionaryPo对象", description="字典表")
public class DictionaryPo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典表id")
    @TableId(value = "id", type = IdType.AUTO)
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "字典键")
    private String dicKey;

    @ApiModelProperty(value = "字典值")
    private String dicValue;

    @ApiModelProperty(value = "字典类型", example = "刷写类型：FLASH_SCRIPT,其他待添加")
    private String dicType;

    @ApiModelProperty(value = "是否有效，1有效；0无效")
    @JsonIgnore
    private Integer isActive;

    @TableField(
            value = "create_time",
            fill = FieldFill.INSERT
    )
    /*@JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )*/
    @ApiModelProperty(value = "创建时间", example = "2020-06-12 12:00:00", dataType = "datetime")
    @JsonIgnore
    private Date createTime;

    @TableField(
            value = "create_by",
            fill = FieldFill.INSERT
    )
    @ApiModelProperty(value = "新增操作用户Id")
    @JsonIgnore
    private String createBy;

    @TableField(
            value = "update_time",
            fill = FieldFill.UPDATE
    )
    /*@JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )*/
    @ApiModelProperty(value = "更新日期", example = "2020-06-12 12:00:00", dataType = "datetime")
    @JsonIgnore
    private Date updateTime;

    @TableField(
            value = "update_by",
            fill = FieldFill.UPDATE
    )
    @ApiModelProperty(value = "更新操作用户Id")
    @JsonIgnore
    private String updateBy;

    @TableLogic(
            value = "0",
            delval = "1"
    )
    @TableField(
            value = "del_flag",
            fill = FieldFill.INSERT_UPDATE
    )
    @JsonIgnore
    @ApiModelProperty(value = "逻辑删除标志：0=有效，1=逻辑删除")
    private Integer delFlag;

}
