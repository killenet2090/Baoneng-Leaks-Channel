package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
/**
 * @ClassName: DeviceTreeNodePo
 * @Description: 设备分类树该信息从车辆数据库中同步过来 实体类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_device_tree_node")
@ApiModel(value="DeviceTreeNodePo对象", description="设备分类树该信息从车辆数据库中同步过来")
public class DeviceTreeNodePo extends BasePo {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "节点Id,用于返回到前端")
    @TableField(exist = false)
    private String idStr;

    @ApiModelProperty(value = "项目id,用于定义树的归属", example = "guanzhi001")
    private String projectId;

    @ApiModelProperty(value = "业务代码名称")
    private String nodeName;

    @ApiModelProperty(value = "业务代码编码:如果为ECU设备零件，则为零件Id")
    private String nodeCode;

    @ApiModelProperty(value = "父节点id， 如果为空，表示为根节点")
    private Long parentId;

    @ApiModelProperty(value = "节点Id,用于返回到前端")
    @TableField(exist = false)
    private String parentIdStr;

    @ApiModelProperty(value = "以节点ID作为路径元素， 以节点/开始到本节点的上一节点止的路径（不含/结尾），即当前节点所在的路径对于根节点其path 为/提供此属性的目的是便于树的查询当对某个节点进行移动时，此节点以下的所有节点的路径都需要修改")
    private String nodeIdPath;

    @ApiModelProperty(value = "以节点NAME作为路径元素，以节点/开始到本节点的上一节点止的路径（不含/结尾），即当前节点所在的路径对于根节点其path 为/提供此属性的目的是便于树的查询当对某个节点进行移动时，此节点以下的所有节点的路径都需要修改")
    private String nodeNamePath;

    @ApiModelProperty(value = "以节点CODE作为路径元素，以节点/开始到本节点的上一节点止的路径（不含/结尾），即当前节点所在的路径对于根节点其path 为/提供此属性的目的是便于树的查询当对某个节点进行移动时，此节点以下的所有节点的路径都需要修改")
    private String nodeCodePath;

    @ApiModelProperty(value = "根节点的层次为0,根节点的直接子节点层次为1,以此类推")
    private Integer treeLevel;

    @ApiModelProperty(value = "每一颗树的根节点的id,如果自身是根节点,则为空")
    private Long rootNodeId;

    @ApiModelProperty(value = "每一颗树的根节点的id,用于返回到前端")
    @TableField(exist = false)
    private String rootNodeIdStr;

    @ApiModelProperty(value = "0-正常 ,1-停用")
    private Integer status;

    @ApiModelProperty(value = "同一层级排序号")
    private Integer orderNum;

    @ApiModelProperty(value = "编辑版本号")
    private Integer version;

    @ApiModelProperty(value = "子节点列表")
    @TableField(exist = false)
    private List<DeviceTreeNodePo> childrenDeviceTreeNodeDos;
}
