package gaea.user.center.service.model.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "用户车辆关系")
public class UserVehicleVo {

    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("车辆Vin码")
    private String vin;

    @ApiModelProperty("用户ID")
    private Set userIds;
}
