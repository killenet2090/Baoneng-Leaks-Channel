package com.bnmotor.icv.tsp.ota.controller.inner;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.req.DeviceTreeNodeBaseReq;
import com.bnmotor.icv.tsp.ota.model.req.DeviceTreeNodePartReq;
import com.bnmotor.icv.tsp.ota.model.req.DeviceTreeNodeReq;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeDbService;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: DeviceTreeNodePo
 * @Description: 设备分类树
该信息从车辆数据库中同步过来 controller层
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@RestController
@Api(value="设备树管理",tags={"设备树管理"})
@RequestMapping(value = "/v1/deviceTree", method = RequestMethod.POST)
@Slf4j
@AllArgsConstructor
public class DeviceTreeNodeController extends AbstractController {
    private final IDeviceTreeNodeService deviceTreeNodeService;
    private final IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @RequestMapping(value = "listAll")
    @ApiOperation(value="获取设备树", notes="获取设备树", response = DeviceTreeNodePo.class)
    public ResponseEntity listAll(@RequestBody DeviceTreeNodeBaseReq deviceTreeNodeBaseReq){
        List<DeviceTreeNodePo> rootdeviceTreeNodePos = deviceTreeNodeService.listAll();
        return RestResponse.ok(rootdeviceTreeNodePos);
    }

    @RequestMapping(value = "listChildren")
    @ApiOperation(value="获取设备树子节点列表", notes="获取设备树子节点列表", response = DeviceTreeNodePo.class)
    public ResponseEntity listChildren(@RequestBody DeviceTreeNodeReq deviceTreeNodeReq){
        String treeNodeId = deviceTreeNodeReq.getTreeNodeId();
        List<DeviceTreeNodePo> deviceTreeNodePos = deviceTreeNodeDbService.listChildren(StringUtils.isEmpty(treeNodeId) ? null: Long.parseLong(deviceTreeNodeReq.getTreeNodeId()));
        return RestResponse.ok(deviceTreeNodePos);
    }

    @RequestMapping(value = "listByTreeLevel")
    @ApiOperation(value="获取设备树某一层级子节点列表", notes="获取设备树某一层级子节点列表", response = DeviceTreeNodePo.class)
    public ResponseEntity listByTreeLevel(@RequestBody DeviceTreeNodePartReq deviceTreeNodeReq){
        List<DeviceTreeNodePo> deviceTreeNodePos = deviceTreeNodeDbService.listByTreeLevel(deviceTreeNodeReq.getTreeLevel() );
        return RestResponse.ok(deviceTreeNodePos);
    }
}
