package gaea.user.center.service.model.response;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import gaea.user.center.service.model.dto.RolePrivilege;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "角色模型")
public class RoleVo extends BasePo {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", name = "id", required = true)
    private String id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", name = "name", required = true)
    private String name;

    /**
     * 关联账户
     */
    @ApiModelProperty(value = "10", name = "AccountNum", required = false)
    private Integer AccountNum;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "描述", name = "remark", required = false)
    private String remark;

    /**
     * 全选权限
     */
    @ApiModelProperty(value = "全选权限角色关系对象", name = "selectAllPrivilege", required = false)
    private List<String> selectAllPrivilege;

    /**
     * 半选权限
     */
    @ApiModelProperty(value = "半选权限角色关系对象", name = "selectHalfPrivilege", required = false)
    private List<String> selectHalfPrivilege;
}
