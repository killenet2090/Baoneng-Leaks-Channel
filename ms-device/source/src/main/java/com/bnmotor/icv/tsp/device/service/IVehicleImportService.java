package com.bnmotor.icv.tsp.device.service;

import com.bnmotor.icv.tsp.device.common.enums.dataaysn.ControlType;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleDto;
import com.bnmotor.icv.tsp.device.model.response.vehImport.ExportFailedVo;
import com.bnmotor.icv.tsp.device.model.response.vehImport.ExportVehicleVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.ProgressVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName: IVehicleImportService
 * @Description: 车辆入库服务
 * @author: zhangwei2
 * @date: 2020/11/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IVehicleImportService {
    /**
     * 导入车辆数据
     *
     * @param uploadFile 文件
     */
    String importExcel(MultipartFile uploadFile);

    /**
     * 进度查询
     *
     * @param taskNo 任务号
     * @return 进度
     */
    ProgressVo queryProgress(String taskNo);

    /**
     * 导入任务控制
     *
     * @param taskNo 任务号
     * @param type   控制类型
     */
    void controlTask(String taskNo, ControlType type);

    /**
     * 查询成功条目
     *
     * @param taskNo 任务号
     * @return 成功集合
     */
    List<ExportVehicleVo> listCheckSuccessed(String taskNo);

    /**
     * 查询校验失败条目
     *
     * @param taskNo 任务号
     * @return 成功集合
     */
    List<ExportFailedVo> listCheckFailed(String taskNo);

    /**
     * 车辆同步入库
     *
     * @param dto 车辆信息
     */
    void addVehicleInfo(VehicleDto dto);
}
