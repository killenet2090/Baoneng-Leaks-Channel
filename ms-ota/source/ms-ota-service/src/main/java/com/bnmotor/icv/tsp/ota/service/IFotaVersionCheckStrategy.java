package com.bnmotor.icv.tsp.ota.service;

import java.util.List;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;

/**
 * @ClassName: FotaVersionCheckPo
 * @Description:  服务类
 * @author xxc
 * @since 2020-07-15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaVersionCheckStrategy {

    /**
     *
     * @param vin
     * @return
     */
    List<FotaFirmwarePo> queryFotaFirmwarePos(String vin);


    /**
     * 版本检查
     *
     * 主要流程：
     * 判断车辆是否存在有效：升级对象基础信息表中是否存在对应记录
     * 判断车辆是否在有关联升级任务
     * 判断升级任务是否有效：在有效期内/状态为已审核通过
     * 若存在有效的升级任务：检查上传的ecu列表参数是否匹配任务的升级固件集合
     * 若存在有效的升级固件集合：检查是否存在有效的升级路径（车当前固件版本与升级任务固件清单中目标版本比对）。
     * 若至少存在一个ecu固件对应的一条有效的升级路径，则认为该车辆存在可用的升级版本
     * 补充判断：检查该路径是否有关联的升级包，若不存在，则该升级路径无效
     * 针对一条有效的升级路径：
     * 添加关联的升级包信息，用于为TBox下载提供必要的属性信息
     * 计算该固件版本依赖的其他固件依赖列表。前提保证：后台固件版本管理功能				中，对于新增固件版本需要控制依赖深度，及避免依赖回环
     * 返回版本检查结果：存在一条有效的升级路径，则认为检查结果为真，补充该次升级任务必要属性及升级固件列表信息；否则，检查结果为假。检查结果通过汇聚平台通道下发到TBox。检查结果主要包括如下数据：
     * 升级固件版本列表：安装包大小、下载地址、安全校验、刷写脚本
     * 升级固件版本依赖的其他固件版本：需要铺平到“升级固件版本列表”中
     * 任务前置条件列表：升级前提条件确认
     * 任务中途停止条件配置信息：该信息用于为Tbox端任务终止配置提供策略
     * 版本检查请求记录需要在保存到数据库(表：tb_fota_version_check)。请求主体信息存储到mysql，并留存一份记录到MongoDb(请求记录中的Ecu列表信息数据会比较大)。如果新版本检查记录为真，需要保存升级版本结果确认表记录(表：tb_fota_version_check_verify)
     *
     * @param req
     * @return
     */
    OtaProtocol checkVersion(OtaProtocol req);

    /**
     * 获取失败的版本检查
     * @param req
     * @param tboxAdamException
     * @return
     */
    OtaProtocol failCheckVersion(OtaProtocol req, TboxAdamException tboxAdamException);

    /**
     * 来自云端的升级通知
     * @param vin
     * @return
     */
    OtaProtocol newVersionFromOta(String vin);
}
