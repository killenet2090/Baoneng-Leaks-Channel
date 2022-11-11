package com.bnmotor.icv.tsp.ota.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanVehicleCountPo;

/**
 * @ClassName: FotaPlanObjListPo
 * @Description: OTA升级计划对象清单定义一次升级中包含哪些需要升级的车辆 dao层
 * @author xxc
 * @since 2020-07-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaPlanObjListMapper extends BaseMapper<FotaPlanObjListPo> {

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param fotaPlanObjListId
     * @return 删除数量
     */
    int deleteByIdPhysical(Long fotaPlanObjListId);

    /**
     * 根据任务Id删除(历史原因，重新定义)
     * @param otaPlanId
     * @return
     */
    int deleteByOtaPlanIdPhysical(Long otaPlanId);

    /**
     * queryDailyReport
     *
     * @param taskId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> queryDailyReport(@Param("taskId")Long taskId, @Param("startTime") String startTime, @Param("endTime")String endTime);

    /**
     * queryWeeklyReport
     *
     * @param taskId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> queryWeeklyReport(@Param("taskId")Long taskId, @Param("startTime") String startTime, @Param("endTime")String endTime);

    /**
     * queryMonthlyReport
     *
     * @param taskId
     * @param endTime
     * @return
     */
    List<Map<String, Object>> queryMonthlyReport(@Param("taskId")Long taskId, @Param("endTime")String endTime);

    /**
     * queryTimeReport
     *
     * @param taskId
     * @param endTime
     * @return
     */
    List<Map<String, Object>> queryTimeReport(@Param("taskId")Long taskId, @Param("endTime")String endTime);

    /**
     * <pre>
     * 根据查询条件统计OTA升级计划对象清单
     定义一次升级中包含哪些需要升级的车辆数量
     * </pre>
     *
     * @param taskId
     * @param vin
     * @param taskStatus
     * @param executedStatus
     * @return
     */
    int countPageByPlanId(@Param("taskId")String taskId, @Param("vin")String vin, @Param("taskStatus")Integer taskStatus, @Param("executedStatus")Integer executedStatus);

    /**
     * 获取升级任务列表
     *
     * @param taskId
     * @param vin
     * @param taskStatus
     * @param executedStatus
     * @return
     */
    List<Map<String, Object>> queryPageByPlanId(@Param("taskId")String taskId, @Param("vin")String vin, @Param("taskStatus")Integer taskStatus, @Param("executedStatus")Integer executedStatus, @Param("startRow")Integer startRow, @Param("pageSize")Integer pageSize);

    /**
     * 获取任务升级对象详情
     * @param id
     * @return
     */
    Map<String, Object> queryVoById(@Param("id")String id);
    
    /**
     * 
     * @param fotaObjList
     * @return
     */
    @Select("<script> select count(ota_object_id) as batch_size, ota_plan_id from tb_fota_plan_obj_list t where t.del_flag = 0 and t.ota_plan_id in <foreach collection='list' open='(' close=')' item='item' separator=','>#{item}  </foreach>  group by ota_plan_id </script>")
    @Results({
    		@Result(column = "batch_size", property = "batchSize"),
    		@Result(column = "ota_plan_id", property = "otaPlanId"),
    })
    List<FotaPlanVehicleCountPo> queryVehicleConunt(@Param("list") List<Long> fotaObjList);
    
    List<FotaPlanObjListPo> queryPlanObjectListByTime(@Param("otaObjectId") Long otaObjectId, @Param("targetTime") Date targetTime);
}
