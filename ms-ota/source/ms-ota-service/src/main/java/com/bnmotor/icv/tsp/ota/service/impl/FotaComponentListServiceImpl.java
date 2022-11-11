package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaComponentListPo;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaComponentListDto;
import com.bnmotor.icv.tsp.ota.service.IDeviceComponentDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaComponentListDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaComponentListService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FotaComponentListPo
 * @Description: OTA升级硬件设备信息关系表 服务实现类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Deprecated
public class FotaComponentListServiceImpl implements IFotaComponentListService {

    @Autowired
    private IFotaComponentListDbService fotaComponentListDbService;

    @Autowired
    private IDeviceComponentDbService deviceComponentDbService;

    @Override
    public List<FotaComponentListDto> listByTreeNodeIds(long treeNodeId) {
        List<FotaComponentListPo> fotaComponentListDtos = fotaComponentListDbService.listByTreeNodeIds(treeNodeId);
        return MyCollectionUtil.newCollection(fotaComponentListDtos, item -> {
            DeviceComponentPo deviceComponentPo = deviceComponentDbService.getById(item.getDeviceComponentId());

            //获取具体的零件数据
            FotaComponentListDto fotaComponentListDto = new FotaComponentListDto();
            fotaComponentListDto.setComponentCode(deviceComponentPo.getComponentCode());
            fotaComponentListDto.setComponentModel(deviceComponentPo.getComponentModel());
            fotaComponentListDto.setComponentName(deviceComponentPo.getComponentName());
            fotaComponentListDto.setDeviceComponentId(deviceComponentPo.getId());
            fotaComponentListDto.setId(item.getId());
            return fotaComponentListDto;
        });
    }
}
