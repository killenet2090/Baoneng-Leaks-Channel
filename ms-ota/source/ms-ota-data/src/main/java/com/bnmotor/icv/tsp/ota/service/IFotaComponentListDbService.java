package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaComponentListPo;

import java.util.List;

/**
 * @ClassName: FotaComponentListPo
 * @Description: OTA升级硬件设备信息关系表 服务类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaComponentListDbService extends IService<FotaComponentListPo> {
    /**
     * 根据数节点Id获取零件关系数据
     * @param treeNodeId
     * @return
     */
    List<FotaComponentListPo> listByTreeNodeIds(long treeNodeId);

    /**
     *
     * @param treeNodeId
     * @param deviceComponentId
     * @return
     */
    FotaComponentListPo findOne(Long treeNodeId, Long deviceComponentId);
}
