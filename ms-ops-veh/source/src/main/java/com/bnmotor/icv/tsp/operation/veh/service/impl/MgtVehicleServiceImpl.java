package com.bnmotor.icv.tsp.operation.veh.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.PageDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehProjectDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehProjectDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehProjectExportVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehProjectStatisticsVo;
import com.bnmotor.icv.tsp.operation.veh.service.IMgtVehicleService;
import com.bnmotor.icv.tsp.operation.veh.service.feign.VehicleFeignService;
import com.bnmotor.icv.tsp.operation.veh.util.PageConverterUtils;
import com.bnmotor.icv.tsp.operation.veh.util.SingleSheetExcel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: MgtVehicleServiceImpl
 * @Description:
 * @author: wuhao1
 * @data: 2020-07-17
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RequiredArgsConstructor
@Service
public class MgtVehicleServiceImpl implements IMgtVehicleService
{

    private static final String[] PROJECT_EXPORT_COLS = {"projectId", "项目编码", "项目名称", "型号数量", "型号编码", "型号名称", "车辆类型"};
    private static final String EXCEL_SHEET_NAME = "车型数据";

    private final VehicleFeignService vehicleFeignService;

    @Override
    public RestResponse<Page<VehProjectStatisticsVo>> listProjectVos(PageRequest pageRequest, QueryVehProjectDto queryDto)
    {
        PageDto pageDto = PageConverterUtils.pageRequestToPageDto(pageRequest);

        RestResponse<Page<VehProjectStatisticsVo>> restResponse = vehicleFeignService.queryVehicleProjects(queryDto);
        return restResponse;
    }

    @Override
    public RestResponse<List<VehProjectDetailVo>>  listModelVos(PageRequest pageRequest, String projectCode,Integer vehType,String searchKey)
    {
        // 查车型暂未做分页
        RestResponse<List<VehProjectDetailVo>> restResponse = vehicleFeignService.queryVehicleModels(projectCode,vehType,searchKey);
        return restResponse;
    }

    @Override
    public SingleSheetExcel exportProjects(PageRequest pageRequest, QueryVehProjectDto queryDto)
    {
        if (pageRequest == null) {
            pageRequest = new PageRequest();
            pageRequest.setCurrent(1);
            pageRequest.setPageSize(10000);
        }
        PageDto pageDto = PageConverterUtils.pageRequestToPageDto(pageRequest);

        RestResponse<Page<VehProjectStatisticsVo>> restResponse = vehicleFeignService.queryVehicleProjects(queryDto);
        Page<VehProjectStatisticsVo> projectVoPage = restResponse.getRespData();
        List<VehProjectStatisticsVo> projectStatisticsVos = projectVoPage.getRecords();

        SingleSheetExcel singleSheetExcel = new SingleSheetExcel(EXCEL_SHEET_NAME);
        singleSheetExcel.addHeadRow(PROJECT_EXPORT_COLS);
        for (VehProjectStatisticsVo statisticsVo : projectStatisticsVos)
        {
            String code = statisticsVo.getCode();
            List<VehProjectDetailVo> detailVos = vehicleFeignService.queryVehicleModels(code,queryDto.getVehType(),"").getRespData();
            int index = 0;
            for (VehProjectDetailVo detailVo : detailVos)
            {
                // 多做一步转换,方便后续扩展
                VehProjectExportVo exportVo = genExportVo(statisticsVo, detailVo);
                String[] rowCellStrs = genRowCellStrs(exportVo);
                singleSheetExcel.addValueRow(rowCellStrs, index++);
            }
        }

        return singleSheetExcel;
    }

    /**
     *
     * @param exportVo
     * @return
     */
    private String[] genRowCellStrs(VehProjectExportVo exportVo) {
        String[] rowCellStrs = new String[PROJECT_EXPORT_COLS.length];
        rowCellStrs[0] = exportVo.getProjectId();
        rowCellStrs[1] = exportVo.getCode();
        rowCellStrs[2] = exportVo.getName();
        rowCellStrs[3] = exportVo.getModelNum().toString();
        rowCellStrs[4] = exportVo.getModelCode();
        rowCellStrs[5] = exportVo.getModelName();
        rowCellStrs[6] = exportVo.getVehTypeValue();
        return rowCellStrs;
    }

    /**
     *
     * @param statisticsVo
     * @param detailVo
     * @return
     */
    private VehProjectExportVo genExportVo(VehProjectStatisticsVo statisticsVo, VehProjectDetailVo detailVo) {
        VehProjectExportVo vo = new VehProjectExportVo();
        vo.setProjectId(statisticsVo.getProjectId());
        vo.setCode(statisticsVo.getCode());
        vo.setName(statisticsVo.getName());
        vo.setModelNum(statisticsVo.getModelNum());
        vo.setModelName(detailVo.getName());
        vo.setModelCode(detailVo.getCode());
        vo.setVehType(detailVo.getVehType());
        vo.setVehTypeValue(detailVo.getVehTypeValue());
        return vo;
    }

    @Override
    public RestResponse listVehVos(PageRequest pageRequest, QueryVehicleDto queryDto)
    {
        return null;
    }
}
