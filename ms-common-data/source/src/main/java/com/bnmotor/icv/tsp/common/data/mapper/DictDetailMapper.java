package com.bnmotor.icv.tsp.common.data.mapper;


import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.common.data.model.entity.DictDetailPo;
import com.bnmotor.icv.tsp.common.data.model.response.DictDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictDetailMapper extends AdamMapper<DictDetailPo> {

    List<DictDetailVo> getDictValueByIdOrCode(String code);

}