package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleImportRecordPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleImportRecordPo
 * @Description: 车辆信息 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-11-11
 */

@Mapper
public interface VehicleImportRecordMapper extends AdamMapper<VehicleImportRecordPo> {
    /**
     * 根据任务号批量删除任务
     *
     * @param taskNo 任务号
     * @return 影响条目
     */
    int deleteByTaskNo(@Param("taskNo") String taskNo);

    /**
     * 根据任务号批量查询任务
     *
     * @param taskNo 任务号
     * @return 车辆导入记录
     */
    List<VehicleImportRecordPo> listByTaskNoAndStatus(@Param("vins") Collection<String> vins,
                                                      @Param("taskNo") String taskNo,
                                                      @Param("checkStatus") Integer checkStatus,
                                                      @Param("fromId") Long fromId,
                                                      @Param("limit") Integer limit);


    /**
     * 根据任务号和状态查询车架号集合
     *
     * @param taskNo      任务号
     * @param checkStatus 检查状态
     * @return 车架号集合
     */
    List<String> listVinsByTaskNo(@Param("taskNo") String taskNo, @Param("checkStatus") Integer checkStatus);
}
