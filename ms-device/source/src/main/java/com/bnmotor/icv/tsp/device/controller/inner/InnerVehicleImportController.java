package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import com.bnmotor.icv.tsp.device.common.enums.dataaysn.ControlType;
import com.bnmotor.icv.tsp.device.common.excel.*;
import com.bnmotor.icv.tsp.device.model.entity.DeviceModelInfoPo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleConfigCommonPo;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleDto;
import com.bnmotor.icv.tsp.device.model.response.device.DeviceTypeVo;
import com.bnmotor.icv.tsp.device.model.response.vehImport.ExportFailedVo;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.VehAllLevelVo;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.VehConfigurationVo;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.VehModelVo;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.VehYearStyleVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.ProgressVo;
import com.bnmotor.icv.tsp.device.service.IDeviceService;
import com.bnmotor.icv.tsp.device.service.IOrgRelationService;
import com.bnmotor.icv.tsp.device.service.IVehicleImportService;
import com.bnmotor.icv.tsp.device.util.PoiExcelHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: InnerVehicleImportController
 * @Description: 车辆入库控制类
 * @author: zhangwei2
 * @date: 2020/11/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner/vehicle")
@Api(value = "车辆入库管理类，提供车辆数据导入，车辆设备同步接口等", tags = {"车辆入库"})
@Slf4j
public class InnerVehicleImportController {
    @Autowired
    private IVehicleImportService importService;
    @Autowired
    private IOrgRelationService relationService;
    @Autowired
    private IDeviceService deviceService;

    @Value("#{'${excel.vehicle.exportHeader}'.split(',')}")
    List<String> vehHeaders;
    @Value("#{'${excel.vehDevice.exportHeader}'.split(',')}")
    List<String> vehDeviceHeaders;

    @PostMapping("/import")
    @ApiOperation(value = "批量导入车辆excel数据")
    public ResponseEntity importExcel(@RequestParam(value = "file") MultipartFile file) {
        String taskNo = importService.importExcel(file);
        Map<String, String> resp = new HashMap<>();
        resp.put("taskNo", taskNo);
        return RestResponse.ok(resp);
    }

    @GetMapping("/importTask/control")
    @ApiOperation(value = "导入任务控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskNo", value = "任务号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "controlType", value = "控制类型:1-暂停任务,2-恢复任务,3-删除任务,4-导入", required = true,
                    paramType = "query", allowableValues = "1,2,3,4")})
    public ResponseEntity controlImportTask(@RequestParam String taskNo, @RequestParam Integer controlType) {
        ControlType type = ControlType.valueOf(controlType);
        if (type == null) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }

        importService.controlTask(taskNo, type);
        return RestResponse.ok(null);
    }

    @GetMapping("/progress/query")
    @ApiOperation(value = "进度查询")
    public ResponseEntity progressQuery(@RequestParam String taskNo) {
        ProgressVo progressVo = importService.queryProgress(taskNo);
        return RestResponse.ok(progressVo);
    }

    @GetMapping("/exportFailed")
    @ApiOperation(value = "校验失败数据导出")
    public void exportFailed(@RequestParam String taskNo, HttpServletResponse response) {
        List<ExportFailedVo> faileds = importService.listCheckFailed(taskNo);
        if (CollectionUtils.isEmpty(faileds)) {
            throw new AdamException(BusinessExceptionEnums.EXPORT_DATA_NOT_EXIST);
        }

        ExcelProcessor.write(response, faileds, "车辆校验失败数据", 0, "车辆数据");
    }

    @GetMapping("/listSuccessed")
    @ApiOperation(value = "获取导入车辆校验成功数据")
    public ResponseEntity listSuccessed(@RequestParam String taskNo) {
        return RestResponse.ok(importService.listCheckSuccessed(taskNo));
    }

    @GetMapping("/exportHeader")
    @ApiOperation(value = "导出车辆数据excel模板")
    public void exportHeader(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String temp = URLEncoder.encode("车辆数据模板" + sdf.format(new Date()), StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + temp + ".xlsx");
            PoiExcelHelper.exportExcelTemplate(response.getOutputStream(), decorateVehicleData(), decorateDeviceData());
        } catch (Exception e) {
            log.error("服务端异常:" + e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 组装车辆相关excel sheet数据
     *
     * @return sheet相关数据
     */
    private SheetData decorateVehicleData() {
        // 1.获取待组装数据
        VehAllLevelVo levelVo = relationService.getVehAllLevelVo();

        // 2.组装车型
        SheetData sheetData = new SheetData();
        CascadeData modelData = new CascadeData(false, false);
        List<CascadeDataItem> modelItems = new ArrayList<>();
        modelData.setCascadeDataItems(modelItems);
        for (VehModelVo modelVo : levelVo.getModels()) {
            CascadeDataItem item = new CascadeDataItem();
            item.setParent(null);
            item.setDisplay(modelVo.getName());
            item.setUniqKey(String.valueOf(modelVo.getId()));
            modelItems.add(item);
        }

        // 3.组装年款
        CascadeData yearData = new CascadeData(false, true);
        List<CascadeDataItem> yearItems = new ArrayList<>();
        yearData.setCascadeDataItems(yearItems);
        for (VehYearStyleVo styleVo : levelVo.getYearStyles()) {
            CascadeDataItem item = new CascadeDataItem();
            item.setParent(String.valueOf(styleVo.getModelId()));
            item.setUniqKey(String.valueOf(styleVo.getId()));
            item.setDisplay(styleVo.getName());
            yearItems.add(item);
        }

        // 4.组装配置
        CascadeData configData = new CascadeData(true, true);
        List<CascadeDataItem> configItems = new ArrayList<>();
        configData.setCascadeDataItems(configItems);
        List<Long> configIds = new ArrayList<>();
        for (VehConfigurationVo configVo : levelVo.getConfigurations()) {
            configIds.add(configVo.getId());
            CascadeDataItem item = new CascadeDataItem();
            item.setParent(String.valueOf(configVo.getYearStyleId()));
            item.setUniqKey(String.valueOf(configVo.getId()));
            item.setDisplay(configVo.getName());
            configItems.add(item);
        }

        // 5.组装颜色
        List<VehicleConfigCommonPo> configs = relationService.listByConfigIds(configIds);
        CascadeData commonData = new CascadeData(true, false);
        List<CascadeDataItem> commonItems = new ArrayList<>();
        commonData.setCascadeDataItems(commonItems);
        for (VehicleConfigCommonPo common : configs) {
            CascadeDataItem item = new CascadeDataItem();
            item.setParent(String.valueOf(common.getConfigId()));
            item.setUniqKey(String.valueOf(common.getId()));
            item.setDisplay(common.getColorName());
            commonItems.add(item);
        }

        CascadeRelation relation = new CascadeRelation();
        relation.builder(modelData, yearData, configData, commonData);
        relation.setOffset('C');
        relation.setOffsetColumn(4);
        relation.setFirstColumn(2);
        relation.setTotalRow(100);
        relation.setHideSheetName("hideModel");

        sheetData.setSheetName("车辆信息");
        sheetData.setHeaders(vehHeaders);
        sheetData.setRelations(Collections.singletonList(relation));
        return sheetData;
    }

    /**
     * 组装设备 excel sheet数据
     *
     * @return 设备相关sheet数据
     */
    private SheetData decorateDeviceData() {
        PageRequest request = new PageRequest();
        request.setPageSize(200);
        Page<DeviceTypeVo> deviceTypes = deviceService.listDeviceTypes(request);
        List<DeviceModelInfoPo> deviceModels = new ArrayList<>();
        CascadeRelation deviceModelRelation = deviceModelCascadeRelation(deviceModels, deviceTypes.getList());

        SheetData sheetData = new SheetData();
        sheetData.setSheetName("车辆零部件信息");
        sheetData.setHeaders(vehDeviceHeaders);
        sheetData.setRelations(Arrays.asList(deviceModelRelation));
        return sheetData;
    }

    private CascadeRelation deviceModelCascadeRelation(List<DeviceModelInfoPo> deviceModels, List<DeviceTypeVo> deviceTypes) {
        CascadeData typeData = new CascadeData(false, false);
        List<CascadeDataItem> typeItems = new ArrayList<>();
        typeData.setCascadeDataItems(typeItems);
        for (DeviceTypeVo typeVo : deviceTypes) {
            CascadeDataItem item = new CascadeDataItem();
            item.setParent(null);
            item.setUniqKey(String.valueOf(typeVo.getDeviceType()));
            item.setDisplay(typeVo.getTypeName());
            typeItems.add(item);
            deviceModels.addAll(deviceService.listDeviceModel(typeVo.getDeviceType()));
        }

        CascadeData modelData = new CascadeData(false, true);
        List<CascadeDataItem> modelItems = new ArrayList<>();
        modelData.setCascadeDataItems(modelItems);

        for (DeviceModelInfoPo info : deviceModels) {
            CascadeDataItem item = new CascadeDataItem();
            item.setParent(String.valueOf(info.getDeviceType()));
            item.setUniqKey(String.valueOf(info.getId()));
            item.setDisplay(info.getDeviceModel());
            modelItems.add(item);
        }

        CascadeRelation relation = new CascadeRelation();
        relation.builder(typeData, modelData);
        relation.setOffset('C');
        relation.setOffsetColumn(4);
        relation.setFirstColumn(2);
        relation.setTotalRow(500);
        relation.setHideSheetName("hideType");
        return relation;
    }


    @PostMapping("/addVehicle")
    @ApiOperation("车辆信息及与车辆关联的设备信息和Sim卡信息新增调用接口")
    public ResponseEntity addVehicleInfo(@RequestBody @Validated VehicleDto vehicleDto) {
        importService.addVehicleInfo(vehicleDto);
        return RestResponse.ok(null);
    }
}
