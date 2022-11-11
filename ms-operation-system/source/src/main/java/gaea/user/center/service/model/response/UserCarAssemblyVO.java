package gaea.user.center.service.model.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: UserCarAssemblyVo
 * @Description: 用户车辆集实体
 * @author: jiangchangyuan1
 * @date: 2020/10/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(description = "用户车辆集实体")
public class UserCarAssemblyVO {
    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("用户id,关联tb_user")
    private String userId;

    @ApiModelProperty("cid,车辆集合id")
    private String cid;

    @ApiModelProperty("cname,车辆集合名称")
    private String cname;

    @ApiModelProperty("type,1-配置，2-标签")
    private Integer type;

    //标签列表
    private List cids;
}
