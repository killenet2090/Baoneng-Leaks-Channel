package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.service.IFotaObjectComponentListDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectComponentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FotaObjectComponentListPo
 * @Description: OTA升级对象零件表,用于接收车辆零件信息同步数据 服务实现类
 * @author xuxiaochang1
 * @since 2020-12-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaObjectComponentListServiceImpl implements IFotaObjectComponentListService {
    @Autowired
    private IFotaObjectComponentListDbService fotaObjectComponentListDbService;

    @Override
    public void delByFotaObjectIdPysical(Long otaObjectId) {
        fotaObjectComponentListDbService.delByFotaObjectIdPysical(otaObjectId);
    }
}
