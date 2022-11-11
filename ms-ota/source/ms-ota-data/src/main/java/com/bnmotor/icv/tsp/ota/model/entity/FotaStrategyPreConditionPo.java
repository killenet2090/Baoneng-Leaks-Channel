package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaStrategyPreConditionPo
 * @Description: 升级策略前置条件表 实体类
 * @author xuxiaochang1
 * @since 2020-12-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_strategy_pre_condition")
public class FotaStrategyPreConditionPo extends BasePo{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id"/*, type = IdType.AUTO*/)
    private Long id;

    /**
     * 项目ID,多租户，不同的租户具有不同的项目ID
     */
    private String projectId;

    /**
     * 策略ID
     */
    private Long otaStrategyId;

    /**
     * 终端升级条件代码,比如：1=电源，2=档位,3=....
     */
    private String condCode;

    /**
     * 条件名称
     */
    private String condName;

    /**
     * 条件值
     */
    private String value;

    /**
     * 值类型:0=数值型, 1=字符型
     */
    private Integer valueType;

    /**
     * 运算符类型:0=关系运算符(> < == != >= <=),1=逻辑运算符
     */
    private Integer operatorType;

    /**
     * 运算符
     */
    private Integer operatorValue;

    /**
     * 数据版本字段
     */
    private Integer version;

}
