package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UserCarPo
 * @Description: 用户-车辆关系实体
 * @author: jiangchangyuan1
 * @date: 2020/10/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@TableName("tb_user_car")
public class UserCarPo extends BasePo {
    @ApiModelProperty("记录id")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("车辆Vin码")
    private String vin;

}
