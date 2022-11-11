package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.entity.FotaUpgradeProcessPo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: FotaUpgradeProcessPo
 * @Description: OTA升级由一系列的升级过程组成
OTA升级过程:
1:升级确认
2:升级包下载
                                             服务类
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaUpgradeProcessDbService extends IService<FotaUpgradeProcessPo> {

}
