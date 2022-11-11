package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.DictionaryPo;

import java.util.List;

/**
 * @ClassName: DictionaryPo
 * @Description: 字典表 服务类
 * @author xxc
 * @since 2020-06-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IDictionaryDbService extends IService<DictionaryPo> {
    /**
     * 获取所有的字典值列表
     * @return
     */
    List<DictionaryPo> listAll();
}
