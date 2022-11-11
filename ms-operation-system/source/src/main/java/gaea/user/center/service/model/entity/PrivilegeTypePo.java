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

@ToString
@ApiModel(value = "资源类型实体", description = "资源类型实体")
@TableName("tb_privilege_type")
@Data
public class PrivilegeTypePo extends BasePo {
    @ApiModelProperty(value = "资源类型ID", name = "id", required = true)
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "资源类型名称", name = "name", required = true)
    private String name;

    @ApiModelProperty(value = "资源类型编码", name = "code", required = true)
    private String code;
}
