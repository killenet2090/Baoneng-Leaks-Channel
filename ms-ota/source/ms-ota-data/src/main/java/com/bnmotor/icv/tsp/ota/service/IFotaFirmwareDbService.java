package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;

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

public interface IFotaFirmwareDbService extends IService<FotaFirmwarePo> {
    /**
     *
     * @param firmwareCode
     * @return
     */
    List<FotaFirmwarePo> getByFrimwareCodes(String firmwareCode);

    /**
     * 根据固件代码获取固件
     * @param firmwareCode
     * @return
     */
    FotaFirmwarePo getByFrimwareCode(String firmwareCode);


    /**
     * 获取同级别下的固件列表
     * @param parentTreeNodeId
     * @return
     */
    List<FotaFirmwarePo> listFirmwareDos(Long parentTreeNodeId);

    /**
     * 通过零件型号来获取固件
     * @param componentCode
     * @param componentModel
     * @return
     */
    List<FotaFirmwarePo> getByComponentCodeAndModel(String componentCode, String componentModel);

    /**
     * 根据树节点Id获取固件列表
     * @param treeNodeId
     * @return
     */
    List<FotaFirmwarePo> listByTreeNodeId(Long treeNodeId);
}
