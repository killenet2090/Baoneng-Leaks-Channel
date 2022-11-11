package com.bnmotor.icv.tsp.ota.controller.inner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPo;
import com.bnmotor.icv.tsp.ota.model.req.DelFirmwareReq;
import com.bnmotor.icv.tsp.ota.model.req.DeviceTreeNodeReq;
import com.bnmotor.icv.tsp.ota.model.req.FirmwarePkgReq;
import com.bnmotor.icv.tsp.ota.model.req.FirmwareVersionsReq;
import com.bnmotor.icv.tsp.ota.model.req.LatestFirmwareReq;
import com.bnmotor.icv.tsp.ota.model.req.UpdatePkgReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradePkgReq;
import com.bnmotor.icv.tsp.ota.model.req.feign.BuildPackageDto;
import com.bnmotor.icv.tsp.ota.model.req.feign.QueryPackageStatusDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaFirmwareDto;
import com.bnmotor.icv.tsp.ota.model.resp.DeviceComponentVo;
import com.bnmotor.icv.tsp.ota.model.resp.FotaFirmwarePkgVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.BuildResultInfo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.QueryResultInfo;
import com.bnmotor.icv.tsp.ota.service.IDeviceComponentService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareService;
import com.bnmotor.icv.tsp.ota.service.feign.BuildPackageService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: FotaFirmwareController
 * @Description: OTA固件信息定义各个零部件需要支持的OTA升级软件OTA软升级需要用户的下载 controller层
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@RestController
@Api(value="固件、版本、升级包管理",tags={"固件、版本、升级包管理"})
@RequestMapping(value = "/v1/firmware")
public class FotaFirmwareController extends AbstractController{

	@Autowired
    private IFotaFirmwareService fotaFirmwareService;

	@Autowired
    private BuildPackageService buildPackageService;

    @Autowired
    private IDeviceComponentService deviceComponentService;

    @RequestMapping(value = "addFirmware", method = RequestMethod.POST)
    @ApiOperation(value="添加固件", notes="添加固件", response = Void.class)
    @WrapBasePo
    public ResponseEntity addFirmware(@RequestBody FotaFirmwareDto fotaFirmwareDto){
        String treeNodeIdStr = fotaFirmwareDto.getTreeNodeIdStr();
        MyAssertUtil.notNull(treeNodeIdStr, OTARespCodeEnum.DATA_NOT_FOUND);
        fotaFirmwareDto.setTreeNodeId(Long.parseLong(treeNodeIdStr));
        fotaFirmwareService.addFotaFirmware(fotaFirmwareDto);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "delFirmware", method = RequestMethod.POST)
    @ApiOperation(value="删除固件", notes="删除固件", response = Void.class)
    @WrapBasePo
    public ResponseEntity delFirmware(@RequestBody DelFirmwareReq req){
        fotaFirmwareService.delFotaFirmware(req.getProjectId(), Long.parseLong(req.getFirmwareId()));
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "updateFirmware", method = RequestMethod.POST)
    @ApiOperation(value="更新固件", notes="更新固件", response = Void.class)
    @WrapBasePo
    public ResponseEntity updateFirmware(@RequestBody FotaFirmwarePo fotaFirmwarePo){
        fotaFirmwareService.updateFotaFirmware(fotaFirmwarePo);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "getLatestFirmwareVersion", method = RequestMethod.POST)
    @ApiOperation(value="获取固件最新版本", notes="获取固件最新版本", response = FotaFirmwareVersionPo.class)
    public ResponseEntity getLatestFirmwareVersion(@RequestBody LatestFirmwareReq req){
        FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareService.getLatestFirmwareVersion(req.getProjectId(), Long.parseLong(req.getFirmwareId()));
        return RestResponse.ok(fotaFirmwareVersionPo);
    }

    @RequestMapping(value = "listFirmwareDos", method = RequestMethod.POST)
    @ApiOperation(value="查询设备树节点上的固件列表", notes="查询设备树节点上的固件列表", response = FotaFirmwarePo.class)
    public ResponseEntity listFirmwareDos(@RequestBody DeviceTreeNodeReq deviceTreeNodeReq){
        List<FotaFirmwarePo> fotaFirmwarePos = fotaFirmwareService.listFirmwarePos(/*deviceTreeNodeReq.getProjectId(), rootNodeId, */Long.parseLong(deviceTreeNodeReq.getTreeNodeId()));
        return RestResponse.ok(fotaFirmwarePos);
    }

    @RequestMapping(value = "addFirmwareVersion", method = RequestMethod.POST)
    @ApiOperation(value="添加固件版本", notes="添加固件版本", response = Void.class)
    @WrapBasePo
    public ResponseEntity addFirmwareVersion(@RequestBody FotaFirmwareVersionPo fotaFirmwareVersionPo){
        fotaFirmwareService.addFotaFirmwareVersion(fotaFirmwareVersionPo);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "listFirmwareVersions", method = RequestMethod.POST)
    @ApiOperation(value="固件版本列表", notes="固件版本列表", response = FotaFirmwareVersionPo.class)
    public ResponseEntity listFirmwareVersions(@RequestBody FirmwareVersionsReq req){
        return RestResponse.ok(fotaFirmwareService.listFirmwareVersions(req.getProjectId(), Long.parseLong(req.getFirmwareId())));
    }

    @RequestMapping(value = "listFirmwarePkgs", method = RequestMethod.POST)
    @ApiOperation(value="固件升级包列表", notes="固件升级包列表", response = FotaFirmwarePkgVo.class)
    public ResponseEntity listFirmwarePkgs(@RequestBody FirmwarePkgReq firmwarePkgReq){
        return RestResponse.ok(fotaFirmwareService.listFirmwarePkgs(Long.parseLong(firmwarePkgReq.getFirmwareId()), Long.parseLong(firmwarePkgReq.getFirmwareVersionId())));
    }

    @RequestMapping(value = "delFirmwarePkgs", method = RequestMethod.POST)
    @ApiOperation(value="删除升级包列表", notes="删除升级包列表", response = Void.class)
    @WrapBasePo
    public ResponseEntity delFirmwarePkgs(@RequestBody FirmwarePkgReq firmwarePkgReq){
        fotaFirmwareService.delFirmwarePkgs(Long.parseLong(firmwarePkgReq.getFirmwareId()), Long.parseLong(firmwarePkgReq.getFirmwareVersionId()));
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "addWholePkg", method = RequestMethod.POST)
    @ApiOperation(value="添加全量包", notes="添加全量包", response = Void.class)
    @WrapBasePo
    public ResponseEntity addWholePkg(@RequestBody @Validated UpdatePkgReq updatePkgReq){
        fotaFirmwareService.addWholePkg(updatePkgReq);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "addDiffPkg", method = RequestMethod.POST)
    @ApiOperation(value="添加差分包", notes="添加差分包", response = Void.class)
    @WrapBasePo
    public ResponseEntity addDiffPkg(@RequestBody UpdatePkgReq updatePkgReq){
        fotaFirmwareService.addDifPkg(updatePkgReq);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "addPatchPkg", method = RequestMethod.POST)
    @ApiOperation(value="添加补丁包", notes="添加补丁包", response = Void.class)
    @WrapBasePo
    public ResponseEntity addPatchPkg(@RequestBody UpdatePkgReq updatePkgReq){
        fotaFirmwareService.addPatchPkg(updatePkgReq);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "createBuildPkg", method = RequestMethod.POST)
    @ApiOperation(value="在线生成升级包", notes="创建在线生成升级包", response = BuildResultInfo.class)
    @WrapBasePo
    public ResponseEntity buildUpgradePkg(@RequestBody BuildPackageDto buildPackageReq){
        return RestResponse.ok(buildPackageService.createTask(buildPackageReq));
    }

    @RequestMapping(value = "queryBuildPkgStatus", method = RequestMethod.POST)
    @ApiOperation(value="查看在线生成升级包状态", notes="查看在线生成升级包状态", response = QueryResultInfo.class)
    public ResponseEntity queryUpgradePkgStatus(@RequestBody QueryPackageStatusDto req){
        return RestResponse.ok(buildPackageService.queryStatus(req));
    }

    @RequestMapping(value = "addUpgradePkg", method = RequestMethod.POST)
    @ApiOperation(value="在线生成升级包构建完成保存信息", notes="在线生成升级包构建完成保存信息", response = Void.class)
    @WrapBasePo
    public ResponseEntity addUpgradePkg(@RequestBody UpgradePkgReq req){
        fotaFirmwareService.addUpgradePkg(req);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "getDeviceComponentVosByTreeNodeId", method = RequestMethod.GET)
    @ApiOperation(value="获取设备树节点下关联的零件列表", notes="获取设备树节点下关联的零件列表", response = DeviceComponentVo.class)
    public ResponseEntity getDeviceComponentVosByTreeNodeId(@RequestParam Long treeNodeId){
        return RestResponse.ok(deviceComponentService.getDeviceComponentVosByTreeNodeId(treeNodeId));
    }
}
