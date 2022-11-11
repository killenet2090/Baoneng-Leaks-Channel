package com.bnmotor.icv.tsp.operation.veh.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehProjectDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehProjectDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehProjectStatisticsVo;
import com.bnmotor.icv.tsp.operation.veh.service.IMgtVehicleService;
import com.bnmotor.icv.tsp.operation.veh.util.SingleSheetExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: VehModelController
 * @Description:
 * @author: wuhao1
 * @data: 2020-07-17
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/vehicle/project")
@Api(value = "管理后台,车型相关接口", tags = {"管理后台,车型相关接口"})
public class VehModelController
{

    private final IMgtVehicleService vehicleService;

    @GetMapping("/list")
    @ApiOperation("项目列表")
    public ResponseEntity listProjects(PageRequest pageRequest, QueryVehProjectDto queryDto)
    {
        log.info("查询项目列表入参{}", JSON.toJSONString(queryDto));
        RestResponse<Page<VehProjectStatisticsVo>> restResponse = vehicleService.listProjectVos(pageRequest, queryDto);
        return RestResponse.ok(restResponse.getRespData());
    }

    @GetMapping("/models")
    @ApiOperation("获取某个项目全部车辆型号")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目编码", paramType = "String")
    })
    public ResponseEntity listProjectModels(PageRequest pageRequest, String projectCode,Integer vehType,String searchKey )
    {
        RestResponse<List<VehProjectDetailVo>>  restResponse = vehicleService.listModelVos(pageRequest, projectCode,vehType,searchKey);
        return RestResponse.ok(restResponse.getRespData());
    }

    @GetMapping("/export")
    @ApiOperation("项目型号数据导出")
    public void exportProjects(QueryVehProjectDto queryDto, HttpServletResponse response) throws IOException
    {
        SingleSheetExcel excel = vehicleService.exportProjects(null, queryDto);

        response.addHeader("Content-Disposition", "attachment;filename=chexingdaochu.xls");
        response.setContentType("application/octet-stream;charset=utf-8");
        excel.getWorkbook().write(response.getOutputStream());
    }
}
