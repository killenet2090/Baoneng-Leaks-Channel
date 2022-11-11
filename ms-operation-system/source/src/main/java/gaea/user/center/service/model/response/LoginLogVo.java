package gaea.user.center.service.model.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName: LoginLog
 * @Description: 登录日志实体
 * @author: jiangchangyuan1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ToString
@ApiModel(value = "用户登录日志", description = "用户登录日志")
public class LoginLogVo {
    /**
     * 记录ID
     */
    @ApiModelProperty(value = "记录ID", name = "id")
    @TableId(value = "id",type = IdType.AUTO)
    private String id;

    /**
     * 登录用户id
     */
    @ApiModelProperty(value = "用户ID", name = "userId")
    private String userId;

    /**
     * 登录用户名称
     */
    @ApiModelProperty(value = "用户名称", name = "userName")
    private String userName;

    /**
     * 登录用户ip
     */
    @ApiModelProperty(value = "用户ip", name = "ip")
    private String ip;

    /**
     * 登录用户设备码
     */
    @ApiModelProperty(value = "用户设备码", name = "deviceCode")
    private String deviceCode;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "过期时间", name = "loginTime")
    private Date loginTime;
}
