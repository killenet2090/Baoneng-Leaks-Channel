package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: UpgradeStrategyDo
 * @Description: 升级策略 实体类
 * @author xxc
 * @since 2020-07-22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_upgrade_strategy")
@ApiModel(value="UpgradeStrategyDo对象", description="升级策略")
public class UpgradeStrategyPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "升级计划ID")
    private Long otaPlanId;

    @ApiModelProperty(value = "关联固件ID")
    private Long firmwareId;

    @ApiModelProperty(value = "固件版本")
    private Long firmwareVersionId;

    @ApiModelProperty(value = "回滚模式； 1 - 回滚， 0 - 不回滚")
    private Integer rollbackMode;

    @ApiModelProperty(value = "最大并发数")
    private Integer maxConcurrent;

    @ApiModelProperty(value = "数据并发控制版本")
    private Integer version;

}
