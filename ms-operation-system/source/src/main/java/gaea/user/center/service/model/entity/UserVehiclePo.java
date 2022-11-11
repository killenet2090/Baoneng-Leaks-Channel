package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: UserCarPo
 * @Description: 用户-车辆关系实体
 * @author: yuhb1
 * @date: 2021/03/12
 * @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@TableName("tb_user_vehicle")
public class UserVehiclePo extends BasePo {

    @ApiModelProperty("记录id")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("车辆Vin码")
    private String vin;

    @ApiModelProperty("用户ID")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set userIds;
}
