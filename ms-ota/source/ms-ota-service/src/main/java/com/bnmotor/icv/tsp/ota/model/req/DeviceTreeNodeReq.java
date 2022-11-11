package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DeviceTreeNodeReq
 * @Description:    post请求参数，用于设备树相关操作请求
 * @author: xuxiaochang1
 * @date: 2020/6/5 13:13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class DeviceTreeNodeReq extends DeviceTreeNodeBaseReq{
    @ApiModelProperty(value = "父节点Id;如果父节点ID为空，则查询出所有品牌一级的数据", example = "123456789", dataType = "String", required = true)
    private String treeNodeId;
}
