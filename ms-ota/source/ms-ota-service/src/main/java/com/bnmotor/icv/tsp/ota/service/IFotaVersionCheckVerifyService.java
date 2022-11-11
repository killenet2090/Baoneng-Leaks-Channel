package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;

/**
 * @ClassName: IFotaVersionCheckVerifyService
 * @Description: OTA升级版本结果确认表 服务类
 * @author xxc
 * @since 2020-07-15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaVersionCheckVerifyService{

    /**
     * 获取当前事务中的任务车辆对象信息
     * @param transId
     * @return
     */
    FotaPlanObjListPo findFotaPlanObjListPoWithTransId(Long transId);

    /**
     * 判断任务是否有效
     * @param req
     * @return
     */
    OtaProtocol judgeFotaPlanValid(OtaProtocol req);

    /**
     * 升级确认请求
     * 该请求送达表示让用户确认是否进行下载
     * 1、修改升级确认表中升级确认请求时间
     * 2、往App端转发升级确认请求
     * @param otaProtocol
     */
    void downloadVerifyReq(OtaProtocol otaProtocol);

    /**
     * 版本安装确认请求发送（来源于TBox）
     * 该请求送达表示让用户确认是否进行安装
     * 1、修改升级确认表中安装确认请求时间
     * 2、往APP端转发升级安装确认请求
     * @param otaProtocol
     */
    void installedVerifyReq(OtaProtocol otaProtocol);

    /**
     * 升级确认结果
     * 1、修改升级确认表((tb_fota_version_check_verify）中相关字段属性
     * 2、修改升级对象表（tb_fota_plan_obj_list）中相关字段属性
     * 3、往App端转发升级确认结果
     * @param otaProtocol
     */
    void downloadVerifyResult(OtaProtocol otaProtocol);

    /**
     * 升级确认
     * @param otaProtocol
     */
    void installedVerifyResult(OtaProtocol otaProtocol);

    /**
     * 从版本请求中解析出版本检查结果
     * @param checkReqId
     * @return
     */
    OtaDownVersionCheckResponse parseFromFotaVersionCheckPo(Long checkReqId);

    /**
     * 升级过程汇报
     * @param otaProtocol
     */
    void downloadProcessReq(OtaProtocol otaProtocol);

    /**
     * 升级过程汇报
     * @param otaProtocol
     */
    void installedProcessReq(OtaProtocol otaProtocol);

    /**
     * 升级（安装）过程结果汇报
     * 1、修改升级安装确认表((tb_fota_version_check_verify）中相关字段属性
     * 2、修改升级安装对象表（tb_fota_plan_obj_list）中相关字段属性
     * @param otaProtocol
     */
    void upgradeResultReq(OtaProtocol otaProtocol);

    /**
     * 单独处理任务失效
     * @param otaProtocol
     */
    void handle4PlanInvalid(OtaProtocol otaProtocol);
}
