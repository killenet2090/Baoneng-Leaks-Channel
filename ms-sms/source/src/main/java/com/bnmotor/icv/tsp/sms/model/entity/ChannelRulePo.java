package com.bnmotor.icv.tsp.sms.model.entity;
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
 * @ClassName: ChannelRuleDo
 * @Description: 短信渠道规则表 实体类
 * @author huangyun1
 * @since 2020-07-29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_channel_rule")
@ApiModel(value="ChannelRuleDo对象", description="短信渠道规则表")
public class ChannelRulePo extends BasePo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "多租户id")
    private String projectId;

    @ApiModelProperty(value = "渠道 1极光短信 ")
    private Integer smsChannel;

    @ApiModelProperty(value = "发送频率 条数")
    private Integer sendRate;

    @ApiModelProperty(value = "频率单位 10:毫秒ms 15:秒s 20:分min 25:小时hour 30:天")
    private Integer rateUnit;

    @ApiModelProperty(value = "发送次数")
    private Integer sendTimes;

    @ApiModelProperty(value = "次数单位 10:毫秒ms 15:秒s 20:分min 25:小时hour 30:天day 35:月month")
    private Integer timesUnit;

    @ApiModelProperty(value = "拦截类型 1:默认放行全部 2：默认拦截全部")
    private Integer interceptType;

    @ApiModelProperty(value = "启用状态，0未启用 1启用")
    private Integer enableFlag;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
