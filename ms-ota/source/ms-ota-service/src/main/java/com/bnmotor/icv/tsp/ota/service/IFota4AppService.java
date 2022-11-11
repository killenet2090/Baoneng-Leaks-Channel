package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.req.app.BaseAppDto;
import com.bnmotor.icv.tsp.ota.model.req.app.CancelInstalledBookedDto;
import com.bnmotor.icv.tsp.ota.model.req.app.RemoteDownloadDto;
import com.bnmotor.icv.tsp.ota.model.req.app.RemoteInstalledDto;
import com.bnmotor.icv.tsp.ota.model.resp.app.SyncResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.TboxUpgradStatusVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.VersionCheckBodyVo;

/**
 * @ClassName: IFota4AppService
 * @Description: 对接客户端App接口
 * @author: xuxiaochang1
 * @date: 2020/8/20 10:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IFota4AppService {

    /**
     * 获取车辆升级状态
     * 1、车辆是否存在当前生效的任务
     * 2、存在升级任务的情况下是否已经进入到升级开始阶段
     * @param vin
     * @return
     */
    TboxUpgradStatusVo getTboxUpgradStatus(String vin);

    /**
     * 检查车辆ota升级版本
     * @param baseAppDto
     * @return
     */
    VersionCheckBodyVo checkVersion(BaseAppDto baseAppDto);

    /**
     * 远程下载
     * @param remoteDownloadDto
     * @return 是否下发下载指令成功
     */
    SyncResultVo remoteDownload(RemoteDownloadDto remoteDownloadDto);

    /**
     * 远程安装
     * @param remoteInstalledDto
     * @return 是否下发安装指令成功
     */
    SyncResultVo remoteInstalled(RemoteInstalledDto remoteInstalledDto);

    /**
     * 取消预约安装
     * @param req
     * @return
     */
    SyncResultVo cancelInstalledBooked(CancelInstalledBookedDto req);
}
