package com.bnmotor.icv.tsp.ota.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanDelayQuery;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanQuery;

/*import com.bnmotor.icv.tsp.ota.common.enums.Enums;*/

/**
 * @ClassName: IFotaPlanService
 * @Description: OTA升级计划表 服务类
 * @author xxc
 * @since 2020-07-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaPlanDbService extends IService<FotaPlanPo> {

    /**
     * 分页查询升级任务
     * @param query
     * @return
     */
    IPage<FotaPlanPo> queryPage(FotaPlanQuery query);


    /**
     * 是否存在已经在任务列表中的固件
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    boolean existPlanWithFirmware(Long firmwareId, Long firmwareVersionId);


    /**
     * 分页获取到期未结束的升级计划
     * @return
     */
    IPage<FotaPlanPo> getExpiredFotaPlan(int offset, int pageSize, long currentId);

    /**
     *
     * @return
     */
    List<FotaPlanPo> getFotaPlans();

    /**
     *
     * @param current
     * @param pageSize
     * @param treeNodeId
     * @return
     */
    IPage<FotaPlanPo> queryPage(Integer current, Integer pageSize, Long treeNodeId, Integer rebuild);

    /**
     * 获取需要升级通知推送到TBOX的任务列表
     * @param offset
     * @param pageSize
     * @param id
     * @param beforeNowTime
     * @return
     */
    IPage<FotaPlanPo> listNeedUpgradeNotifyFotaPlans(Integer offset, Integer pageSize, Long id, String beforeNowTime);

    /**
     * 获取绑定了策略Id的任务列表
     * @param otaStrategyId
     * @return
     */
    List<FotaPlanPo> listWithOtaStrategyId(Long otaStrategyId);
 
    /**
     * 查询延迟任务
     * @param fotaPlanDelayQuery
     * @return
     */
    IPage<FotaPlanPo> queryDelayPlanPage(FotaPlanDelayQuery fotaPlanDelayQuery);

}