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
 * @ClassName: ChannelRosterDo
 * @Description: 短信渠道发送名单设置表 实体类
 * @author huangyun1
 * @since 2020-07-29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_channel_roster")
@ApiModel(value="ChannelRosterDo对象", description="短信渠道发送名单设置表")
public class ChannelRosterPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "多租户id")
    private String projectId;

    @ApiModelProperty(value = "渠道设置表id")
    private Long channelRuleId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "类型：1白名单 2黑名单")
    private Integer rosterType;

    @ApiModelProperty(value = "启用状态，0未启用 1启用")
    private Integer enableFlag;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
