package com.bnmotor.icv.tsp.sms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ClassName: SmsRecordDo
 * @Description: 短信发送记录表
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sms_record")
@ApiModel(value="SmsRecordPo", description="短信发送记录表")
public class SmsRecordPo extends BasePo {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 消息id
     */
    private Long smsInfoId;
    /**
     * 模板id
     */
    private String templateId;
    /**
     * 回调消息id
     */
    private String callbackMsgId;
    /**
     * 发送状态，0发送中 1发送完成 2发送失败
     */
    private Integer sendStatus;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 状态码
     */
    private Integer statusCode;
    /**
     * 发送渠道 1极光短信
     */
    private Integer sendChannel;
}
