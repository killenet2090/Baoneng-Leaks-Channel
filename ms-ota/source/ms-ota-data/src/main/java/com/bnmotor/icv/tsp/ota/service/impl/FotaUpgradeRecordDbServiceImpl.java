package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.FotaUpgradeRecordPo;
import com.bnmotor.icv.tsp.ota.mapper.FotaUpgradeRecordMapper;
import com.bnmotor.icv.tsp.ota.service.IFotaUpgradeRecordDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FotaUpgradeRecordPo
 * @Description: OTA升级记录指的是升级对象针对每一个升级软件的一次升级记录
升级记录记录的是升级计划和任务的实际执行情况
 服务实现类
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaUpgradeRecordDbServiceImpl extends ServiceImpl<FotaUpgradeRecordMapper, FotaUpgradeRecordPo> implements IFotaUpgradeRecordDbService {

}
