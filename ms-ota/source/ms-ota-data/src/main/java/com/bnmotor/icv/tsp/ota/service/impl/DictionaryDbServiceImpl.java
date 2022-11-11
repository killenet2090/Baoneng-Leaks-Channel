package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.model.entity.DictionaryPo;
import com.bnmotor.icv.tsp.ota.mapper.DictionaryMapper;
import com.bnmotor.icv.tsp.ota.service.IDictionaryDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: DictionaryPo
 * @Description: 字典表 服务实现类
 * @author xxc
 * @since 2020-06-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class DictionaryDbServiceImpl extends ServiceImpl<DictionaryMapper, DictionaryPo> implements IDictionaryDbService {

    @Override
    public List<DictionaryPo> listAll() {
        return MyCollectionUtil.safeList(list());
    }
}
