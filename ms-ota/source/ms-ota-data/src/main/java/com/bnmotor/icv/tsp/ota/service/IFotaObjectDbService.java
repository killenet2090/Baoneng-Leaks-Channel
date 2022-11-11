package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.compose.RegionInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;

import java.util.List;

/**
 * @ClassName: IFotaObjectService
 * @Description: OTA升级对象指需要升级的一个完整对象，在车联网中指一辆车通常拿车的vin作为升级的ID服务类
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaObjectDbService extends IService<FotaObjectPo> {
    /**
     * 分页查询升级对象列表
     * @param treeNodeId
     * @param offset
     * @param pageSize
     * @return
     */
    IPage<FotaObjectPo> listPageByTreeNodeId(long treeNodeId, int offset, int pageSize);

    /**
     * 获取同一个设备树节点下的所有升级对象
     * @param treeNodeId   树节点Id
     * @return
     */
    List<FotaObjectPo> listAllByTreeNodeId(long treeNodeId);

    /**
     * 根据Vin码获取车辆
     * @param objectId  vin码
     * @return  参考{@link FotaObjectDo}
     */
    FotaObjectPo findByObjectId(String objectId);

    /**
     * 根据Vin码获取车辆
     * @param vin  vin码
     * @return  参考{@link FotaObjectDo}
     */
    FotaObjectPo findByVin(String vin);

    /**
     * 获取车辆当前升级状态
     * @param vin   vin码
     * @return  参考{@link TboxUpgradStatusVo}
     */
    //TboxUpgradStatusVo getFotaUpgradeStatus(String vin);

    /**
     * 获取车辆当前升级状态枚举
     * @param vin
     * @return  参考{@link Enums.TaskObjStatusTypeEnum}
     */
    //Enums.TaskObjStatusTypeEnum getTaskObjStatusTypeEnum(String vin);
    List<RegionInfo> listRegion(Long treeNodeId);
    
    List<FotaObjectPo> listByVins(List<String> vins);
    
    List<FotaObjectPo> listByRegionCodeAndTreeNodeId(List<String> regionCodes, Long treeNodeId);
    
    Page<FotaObjectPo> selectVehicleWithTreeNodeIdPage(Long treeNodeId, List<Long> targetObjectIds, Page page);

    List<FotaObjectPo> selectVehicleWithTreeNodeId(Long treeNodeId, List<Long> targetObjectIds);
}