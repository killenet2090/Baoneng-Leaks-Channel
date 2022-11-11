package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanTaskDetailPo;

import java.util.List;

/**
 * @ClassName: FotaPlanTaskDetailPo
 * @Description: OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级
在创建升级计划时创建升级 服务类
 * @author xxc
 * @since 2020-08-08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaPlanTaskDetailDbService extends IService<FotaPlanTaskDetailPo> {
    /**
     * 根据任务Id和任务升级对象数据Id删除升级对象清单列表
     *
     * @param otaPlanId
     * @param otaPlanObjId
     */
    void deleteByOtaPlanObjId(Long otaPlanId, Long otaPlanObjId);

    /**
     * 查询OTA升级任务明细
     * @param otaPlanObjectId
     * @param planFirmwareIds
     * @return
     */
    List<FotaPlanTaskDetailPo> listByOtaPlanObjIdWithPlanFirmwareId(Long otaPlanObjectId, List<Long> planFirmwareIds);

    /**
     * 查询OTA升级任务明细
     * @param otaPlanObjectId
     * @return
     */
    List<FotaPlanTaskDetailPo> listByOtaPlanObjId(Long otaPlanObjectId);
}
