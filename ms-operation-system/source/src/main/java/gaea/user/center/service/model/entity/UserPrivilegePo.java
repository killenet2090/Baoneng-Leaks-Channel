package gaea.user.center.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UserPrivilegePO
 * @Description: 用户权限实体
 * @author: jiangchangyuan1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@TableName("tb_user_privilege")
public class UserPrivilegePo {

    @ApiModelProperty("用户id,关联tb_user")
    @TableId(value = "user_id",type = IdType.INPUT)
    private Long userId;

    @ApiModelProperty("权限id,关联tb_privilege")
    private Long privilegeId;
}
