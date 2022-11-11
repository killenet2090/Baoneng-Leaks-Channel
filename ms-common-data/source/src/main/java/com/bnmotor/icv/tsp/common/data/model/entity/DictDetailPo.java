package com.bnmotor.icv.tsp.common.data.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * tb_dick_detail
 * @author 
 */
@Data
@TableName("tb_dick_detail")
@ApiModel(value = "字典详细po", description = "字典详细")
public class DictDetailPo extends BasePo implements Serializable {


    private Long id;
    /**
     * 字典编码
     */
    private String code;

    /**
     * 数据字典编码
     */
    private String dictCode;

    /**
     * 选项名称
     */
    private String itemName;

    /**
     * 选项值
     */
    private String itemValue;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Short status;

    /**
     * 备注
     */
    private String remark;



    private static final long serialVersionUID = 1L;
}