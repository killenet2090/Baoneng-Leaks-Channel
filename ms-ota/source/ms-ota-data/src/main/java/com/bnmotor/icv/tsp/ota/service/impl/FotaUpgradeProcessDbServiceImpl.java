package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.FotaUpgradeProcessPo;
import com.bnmotor.icv.tsp.ota.mapper.FotaUpgradeProcessMapper;
import com.bnmotor.icv.tsp.ota.service.IFotaUpgradeProcessDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FotaUpgradeProcessPo
 * @Description: OTA升级由一系列的升级过程组成
OTA升级过程:
1:升级确认
2:升级包下载
                                             服务实现类
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaUpgradeProcessDbServiceImpl extends ServiceImpl<FotaUpgradeProcessMapper, FotaUpgradeProcessPo> implements IFotaUpgradeProcessDbService {

}
