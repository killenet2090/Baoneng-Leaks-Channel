package com.bnmotor.icv.tsp.sms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: MsgInfoDetailDo
 * @Description: 消息明细表
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sms_info_detail")
@ApiModel(value="SmsInfoDetailPo", description="消息明细表")
public class SmsInfoDetailPo extends BasePo {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 短信id
     */
    private Long smsInfoId;
    /**
     * 内容
     */
    private String smsContent;

    /**
     * 发送标签列表(json串)
     */
    private String extraData;

}
