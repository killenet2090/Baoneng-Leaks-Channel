package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaVersionCheckVerifyPo;
import com.bnmotor.icv.tsp.ota.model.query.FotaUpgradeLogQuery;

import java.util.Date;

/**
 * @ClassName: FotaVersionCheckVerifyPo
 * @Description: OTA升级版本结果确认表 服务类
 * @author xxc
 * @since 2020-07-15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaVersionCheckVerifyDbService extends IService<FotaVersionCheckVerifyPo> {

    /**
     * 获取当前事务中的任务车辆对象信息
     * @param transId
     * @return
     */
    FotaPlanObjListPo findFotaPlanObjListPoWithTransId(Long transId);

    /**
     * 获取当前升级事务
     * @param planId
     * @param planObjectId
     * @return
     */
    FotaVersionCheckVerifyPo findCurFotaVersionCheckVerifyPo(Long planId, Long planObjectId);

    /**
     * 根据Vin码获取升级记录
     * @param vin
     * @return
     */
    FotaVersionCheckVerifyPo findCurFotaVersionCheckVerifyPoByVin(String vin);

    /**
     *
     * @param fotaUpgradeLogQuery
     * @return
     */
    IPage<FotaVersionCheckVerifyPo> queryPage4UpgradeLog(FotaUpgradeLogQuery fotaUpgradeLogQuery);

    /**
     * 构建更新修改器
     * @param id
     * @param installedVerifyBookedTime
     * @return
     */
    UpdateWrapper<FotaVersionCheckVerifyPo> build4InstalledVerifyBookedTime(Long id, Date installedVerifyBookedTime);
}
