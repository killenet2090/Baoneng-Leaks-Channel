package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaUpgradeConditionMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaUpgradeConditionPo;
import com.bnmotor.icv.tsp.ota.service.IFotaUpgradeConditionDbService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FotaUpgradeConditionPo
 * @Description: 终端升级条件项目定义 服务实现类
 * @author xuxiaochang1
 * @since 2020-09-09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaUpgradeConditionDbServiceImpl extends ServiceImpl<FotaUpgradeConditionMapper, FotaUpgradeConditionPo>
		implements IFotaUpgradeConditionDbService {
}
