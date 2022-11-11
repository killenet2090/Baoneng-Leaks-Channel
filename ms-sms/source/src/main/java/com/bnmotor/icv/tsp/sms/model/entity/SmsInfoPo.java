package com.bnmotor.icv.tsp.sms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ClassName: MsgInfoPO
 * @Description: 消息表
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sms_info")
@ApiModel(value="SmsInfoPo", description="消息表")
public class SmsInfoPo extends BasePo {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务id
     */
    private String businessId;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 发送状态，0发送中 1发送完成 2发送失败
     */
    private Integer sendStatus;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 来源终端类型 0后台管理平台 1远控服务
     */
    private Integer fromType;

    /**
     * 发送渠道 1极光
     */
    private Integer sendChannel = 1;
    /**
     * 发送手机号
     */
    private String sendPhone;

    /**
     * 短信发送记录
     */
    @TableField(exist = false)
    private SmsRecordPo smsRecordPo;

}
