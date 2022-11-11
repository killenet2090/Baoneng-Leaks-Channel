package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPo;

import java.util.List;

/**
 * @ClassName: FotaStrategyPo
 * @Description: OTA升级策略-新表 服务类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaStrategyDbService extends IService<FotaStrategyPo> {
    /**
     * 删除策略
     * @param id
     */
    void delFotaStrategy(Long id);

    /**
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    List<FotaStrategyPo> listByColumnValue(String columnName, Object columnValue);
}
