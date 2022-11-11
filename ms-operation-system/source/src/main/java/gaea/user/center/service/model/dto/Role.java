package gaea.user.center.service.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "OTA用户中心-角色对象数据", description = "OTA用户中心-角色对象参数")
public class Role
{
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", name = "id", required = false)
    private Integer id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", name = "name", required = true)
	private String name;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "描述", name = "remark", required = false)
	private String remark;
    /**
     * 全选权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "selectAllId", required = false)
	private List<Integer> selectAllId;

    /**
     * 半选权限ID
     */
    @ApiModelProperty(value = "权限ID", name = "selectHalfId", required = false)
    private List<Integer> selectHalfId;
}
