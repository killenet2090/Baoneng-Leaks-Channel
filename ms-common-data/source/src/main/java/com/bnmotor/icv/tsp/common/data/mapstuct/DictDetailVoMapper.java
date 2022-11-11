package com.bnmotor.icv.tsp.common.data.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import com.bnmotor.icv.tsp.common.data.model.entity.DictDetailPo;
import com.bnmotor.icv.tsp.common.data.model.response.DictDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author :luoyang
 * @date_time :2020/11/12 15:47
 * @desc:
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface DictDetailVoMapper extends AdamMybatisMapMapper<DictDetailPo, DictDetailVo> {
    DictDetailVoMapper INSTANCE = Mappers.getMapper(DictDetailVoMapper.class);
}