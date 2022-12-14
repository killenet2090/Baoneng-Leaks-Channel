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
 * @Description: ?????????????????????
 * @author: zhangwei2
 * @date: 2020/11/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. ?????????????????????????????????????????????????????????????????????????????????????????????????????????
 */
@RestController
@RequestMapping("/inner/vehicle")
@Api(value = "??????????????????????????????????????????????????????????????????????????????", tags = {"????????????"})
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
    @ApiOperation(value = "??????????????????excel??????")
    public ResponseEntity importExcel(@RequestParam(value = "file") MultipartFile file) {
        String taskNo = importService.importExcel(file);
        Map<String, String> resp = new HashMap<>();
        resp.put("taskNo", taskNo);
        return RestResponse.ok(resp);
    }

    @GetMapping("/importTask/control")
    @ApiOperation(value = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskNo", value = "?????????", required = true, paramType = "query"),
            @ApiImplicitParam(name = "controlType", value = "????????????:1-????????????,2-????????????,3-????????????,4-??????", required = true,
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
    @ApiOperation(value = "????????????")
    public ResponseEntity progressQuery(@RequestParam String taskNo) {
        ProgressVo progressVo = importService.queryProgress(taskNo);
        return RestResponse.ok(progressVo);
    }

    @GetMapping("/exportFailed")
    @ApiOperation(value = "????????????????????????")
    public void exportFailed(@RequestParam String taskNo, HttpServletResponse response) {
        List<ExportFailedVo> faileds = importService.listCheckFailed(taskNo);
        if (CollectionUtils.isEmpty(faileds)) {
            throw new AdamException(BusinessExceptionEnums.EXPORT_DATA_NOT_EXIST);
        }

        ExcelProcessor.write(response, faileds, "????????????????????????", 0, "????????????");
    }

    @GetMapping("/listSuccessed")
    @ApiOperation(value = "????????????????????????????????????")
    public ResponseEntity listSuccessed(@RequestParam String taskNo) {
        return RestResponse.ok(importService.listCheckSuccessed(taskNo));
    }

    @GetMapping("/exportHeader")
    @ApiOperation(value = "??????????????????excel??????")
    public void exportHeader(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String temp = URLEncoder.encode("??????????????????" + sdf.format(new Date()), StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + temp + ".xlsx");
            PoiExcelHelper.exportExcelTemplate(response.getOutputStream(), decorateVehicleData(), decorateDeviceData());
        } catch (Exception e) {
            log.error("???????????????:" + e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * ??????????????????excel sheet??????
     *
     * @return sheet????????????
     */
    private SheetData decorateVehicleData() {
        // 1.?????????????????????
        VehAllLevelVo levelVo = relationService.getVehAllLevelVo();

        // 2.????????????
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

        // 3.????????????
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

        // 4.????????????
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

        // 5.????????????
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

        sheetData.setSheetName("????????????");
        sheetData.setHeaders(vehHeaders);
        sheetData.setRelations(Collections.singletonList(relation));
        return sheetData;
    }

    /**
     * ???????????? excel sheet??????
     *
     * @return ????????????sheet??????
     */
    private SheetData decorateDeviceData() {
        PageRequest request = new PageRequest();
        request.setPageSize(200);
        Page<DeviceTypeVo> deviceTypes = deviceService.listDeviceTypes(request);
        List<DeviceModelInfoPo> deviceModels = new ArrayList<>();
        CascadeRelation deviceModelRelation = deviceModelCascadeRelation(deviceModels, deviceTypes.getList());

        SheetData sheetData = new SheetData();
        sheetData.setSheetName("?????????????????????");
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
    @ApiOperation("????????????????????????????????????????????????Sim???????????????????????????")
    public ResponseEntity addVehicleInfo(@RequestBody @Validated VehicleDto vehicleDto) {
        importService.addVehicleInfo(vehicleDto);
        return RestResponse.ok(null);
    }
}
