package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPo;
import com.bnmotor.icv.tsp.ota.model.req.UpdatePkgReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradePkgReq;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaFirmwareDto;
import com.bnmotor.icv.tsp.ota.model.resp.FotaFirmwarePkgVo;

import java.util.List;

/**
 * @ClassName: FotaFirmwarePo
 * @Description: OTA固件信息
定义各个零部件需要支持的OTA升级软件

OTA软升级需要用户的下载 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaFirmwareService {
    /**
     * 根据固件代码获取固件
     * @param projectId
     * @param firmwareCode
     * @return
     */
    FotaFirmwarePo getByFrimwareCode(String projectId, String firmwareCode);

    /**
     * 添加固件(同时新建设备数节点)
     * @param fotaFirmwareDto
     */
    void addFotaFirmware(FotaFirmwareDto fotaFirmwareDto);

    /**
     *
     * @param fotaFirmwarePo
     */
    void updateFotaFirmware(FotaFirmwarePo fotaFirmwarePo);

    /**
     *
     * @param projectId
     * @param firmwareId
     */
    void delFotaFirmware(String projectId, long firmwareId);

    /**
     * 根据设备树Id获取固件信息
     * @param projectId
     * @param treeNodeId
     * @return
     */
    //List<FotaFirmwarePo> listFotaFirmwareByTreeNodeId(String projectId, Long treeNodeId);

    /**
     * 添加固件版本
     * @param fotaFirmwareVersionPo
     */
    void addFotaFirmwareVersion(FotaFirmwareVersionPo fotaFirmwareVersionPo);

    /**
     * 获取固件版本列表
     * @param firmwareId
     * @return
     */
    List<FotaFirmwareVersionPo> listFirmwareVersions(String projectId, long firmwareId);

    /**
     * @param firmwareId
     * @return
     */
    FotaFirmwareVersionPo getLatestFirmwareVersion(String projectId, long firmwareId);

    /**
     *  固件升级包列表
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    List<FotaFirmwarePkgVo> listFirmwarePkgs(long firmwareId, long firmwareVersionId);

    /**
     *  删除固件升级包列表
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    void delFirmwarePkgs(long firmwareId, long firmwareVersionId);

    /**
     * 添加全量升级包
     * @param updatePkgReq
     */
    void addWholePkg(UpdatePkgReq updatePkgReq);

    /**
     * 添加差分升级包
     * @param updatePkgReq
     */
    void addDifPkg(UpdatePkgReq updatePkgReq);

    /**
     * 添加补丁升级包
     * @param updatePkgReq
     */
    void addPatchPkg(UpdatePkgReq updatePkgReq);

    /**
     * 获取同级别下的固件列表
     * @param treeNodeId
     * @return
     */
    List<FotaFirmwarePo> listFirmwarePos(Long treeNodeId);

    /**
     * 做包回调接口
     * @param req
     */
    void addUpgradePkg(UpgradePkgReq req);

    /**
     * 是否固件所有版本升级包已经构建成功
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    boolean isFirmwarePkgBuildedSuccess(Long firmwareId, Long firmwareVersionId);
}
