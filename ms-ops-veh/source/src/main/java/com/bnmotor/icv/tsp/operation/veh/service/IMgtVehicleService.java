package com.bnmotor.icv.tsp.operation.veh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehProjectDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehProjectDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehProjectStatisticsVo;
import com.bnmotor.icv.tsp.operation.veh.util.SingleSheetExcel;

import java.util.List;

/**
 * @ClassName: IMgtVehicleService
 * @Description: 车型和车辆列表业务类
 * @author: wuhao1
 * @data: 2020-07-17
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IMgtVehicleService
{

    /**
     * 车型项目列表
     * @param pageRequest
     * @param queryDto
     * @return
     */
    RestResponse<Page<VehProjectStatisticsVo>> listProjectVos(PageRequest pageRequest, QueryVehProjectDto queryDto);

    /**
     * 某车型型号列表
     * @param projectCode
     * @return
     */
    RestResponse<List<VehProjectDetailVo>>  listModelVos(PageRequest pageRequest, String projectCode, Integer vehType,String searchKey);

    /**
     * 导出项目、车型列表,根据搜索条件全量导出
     * @return
     */
    SingleSheetExcel exportProjects(PageRequest pageRequest, QueryVehProjectDto queryDto);

    /**
     * 车辆列表
     * @param pageRequest
     * @param queryDto
     * @return
     */
    RestResponse listVehVos(PageRequest pageRequest, QueryVehicleDto queryDto);
}
