package com.bnmotor.icv.tsp.ota.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanTaskDetailPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: FotaPlanTaskDetailPo
* @Description: OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级
在创建升级计划时创建升级 dao层
 * @author xxc
 * @since 2020-08-08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaPlanTaskDetailMapper extends BaseMapper<FotaPlanTaskDetailPo> {
    /**
     * 根据任务Id删除
     * @param otaPlanId
     * @return
     */
    int deleteByOtaPlanIdPhysical(Long otaPlanId);

    /**
     * 根据升级对象数据Id删除任务详情信息
     * @param otaPlanId
     * @param otaPlanObjId
     * @return
     */
    int deleteByOtaPlanObjIdPhysical(@Param("otaPlanId") Long otaPlanId, @Param("otaPlanObjId")Long otaPlanObjId);

    /**
     * queryTaskFailReport
     *
     * @param vin
     * @param ecuId
     * @param taskName
     * @return
     */
    List<Map<String, Object>> queryTaskFailReport(@Param("vin")String vin, @Param("ecuId") Long ecuId, @Param("taskName") String taskName);

    /**
     * queryTaskFailReport
     * @param treeNodeIds
     * @return
     */
    List<Map<String, Object>> queryTaskFailReportByTreeNodeId(List<Long> treeNodeIds);

    /**
     *
     * @param id
     * @return
     */
    int countByPlanObjId(@Param("id") String id);

    /**
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> queryByPlanObjId(@Param("id") String id);
}
