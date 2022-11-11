package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName: EmailRecord
 * @Description: 邮箱发送记录
 * @author: jiangchangyuan1
 * @date: 2020/11/2
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ToString
@ApiModel(value = "邮件发送记录", description = "邮件发送记录")
@TableName("tb_email_record")
public class EmailRecordPo extends BasePo {
    /**
     * 记录ID
     */
    @ApiModelProperty(value = "id", name = "记录ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "email", name = "邮箱")
    private String email;
    /**
     * 邮件主题
     */
    @ApiModelProperty(value = "subject", name = "邮件主题")
    private String subject;
    /**
     * 关键内容，例如账户注册，则保存初始密码
     */
    @ApiModelProperty(value = "keyWord", name = "关键内容，例如账户注册，则保存初始密码")
    private String keyWord;
    /**
     * 类型:0-账户注册，1-密码重置，2-账户激活
     */
    @ApiModelProperty(value = "type", name = "类型:0-账户注册，1-密码重置，2-账户激活")
    private Integer type;
    /**
     * 是否成功发送:0-不成功，1-成功
     */
    @ApiModelProperty(value = "isSend", name = "是否成功发送:0-不成功，1-成功")
    private Integer isSend;
    /**
     * 过期时间
     */
    @ApiModelProperty(value = "expireTime", name = "过期时间")
    private Date expireTime;

    private String displayName;
}
