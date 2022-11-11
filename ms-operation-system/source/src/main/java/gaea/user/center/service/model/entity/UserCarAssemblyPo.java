package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UserCarAssembly
 * @Description: 用户车辆集合实体
 * @author: jiangchangyuan1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@TableName("tb_user_car_assembly")
public class UserCarAssemblyPo extends BasePo {
    @ApiModelProperty("记录id")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id,关联tb_user")
    private Long userId;

    @ApiModelProperty("cid,车辆集合id")
    private Long cid;

    @ApiModelProperty("cname,车辆集合名称")
    private String cname;

    @ApiModelProperty("type,1-配置，2-标签")
    private Integer type;
}
