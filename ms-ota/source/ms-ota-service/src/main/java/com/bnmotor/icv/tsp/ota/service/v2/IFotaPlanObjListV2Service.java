package com.bnmotor.icv.tsp.ota.service.v2;

import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.req.v2.UpgradeObjectListV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.UpgradeTaskObjectV2Req;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.ExistValidPlanObjVo;

import java.util.List;

/**
 * @ClassName: FotaPlanObjListDo
 * @Description: OTA升级计划对象清单
定义一次升级中包含哪些需要升级的车辆 服务类
 * @author xxc
 * @since 2020-07-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Deprecated
public interface IFotaPlanObjListV2Service {
    /**
     * 根据对象Id获取最新升级对象
     * @param objectId
     * @return
     */
    FotaPlanObjListPo findOneByObjectId(Long objectId);

    /**
     * 获取当前正在升级的车辆对象
     * @param fotaObjectDo
     * @return
     */
    FotaPlanObjListPo findCurFotaPlanObjListByFotaObject(final FotaObjectPo fotaObjectDo);

//    /**
//     *
//     * @param vin
//     * @return
//     */
//    TempPlanObjListWrapper findCurTempPlanObjListWrapperByVin(String vin);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param fotaPlanObjList
     * @return 更新数量
     */
    AddFotaPlanResultVo updateFotaPlanObjList(UpgradeObjectListV2Req fotaPlanObjList);

    /**
     * 新增升级对象清单
     * @param upgradeObjectListReq
     * @return AddFotaPlanResultVo
     */
    AddFotaPlanResultVo insertUpgradeTaskObjectList(UpgradeObjectListV2Req upgradeObjectListReq);

    /**
     * 新增任务添加验证
     * @param upgradeTaskObjectReqList
     * @return
     */
    List<ExistValidPlanObjVo> validate4AddFotaPlan(List<UpgradeTaskObjectV2Req> upgradeTaskObjectReqList);

    /**
     * 检查一个车辆是否在一个有效的任务中
     * @param otaObjectId
     * @return
     */
    ExistValidPlanObjVo checkValidate4AddFotaPlan(Long otaObjectId);
}
