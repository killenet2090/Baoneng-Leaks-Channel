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
 * @ClassName: PhoneRecordPo
 * @Description: 手机验证码发送实体
 * @author: jiangchangyuan1
 * @date: 2020/11/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ToString
@ApiModel(value = "手机发送记录", description = "手机发送记录")
@TableName("tb_phone_record")
public class PhoneRecordPo extends BasePo {
    /**
     * 记录ID
     */
    @ApiModelProperty(value = "记录ID", name = "id")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "phone", name = "手机号")
    private String phone;
    /**
     * 关键内容，例如激活发送的关键内容为验证码
     */
    @ApiModelProperty(value = "keyWord", name = "关键内容，例如激活发送的关键内容为验证码")
    private String keyWord;
    /**
     * 类型：0-激活，1-修改手机号
     */
    @ApiModelProperty(value = "type", name = "类型：0-激活，1-修改手机号")
    private Integer type;
    /**
     * 发送时间
     */
//    @ApiModelProperty(value = "createTime", name = "发送时间")
//    private Date createTime;
    /**
     * 过期时间
     */
    @ApiModelProperty(value = "expireTime", name = "过期时间")
    private Date expireTime;
}
