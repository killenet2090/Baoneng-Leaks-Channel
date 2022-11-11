package com.bnmotor.icv.tsp.common.data.service.impl;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.common.data.mapper.DictDetailMapper;
import com.bnmotor.icv.tsp.common.data.model.response.DictDetailVo;
import com.bnmotor.icv.tsp.common.data.service.IDictDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author :luoyang
 * @date_time :2020/11/12 16:03
 * @desc:
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class DictDetailServiceImpl implements IDictDetailService {

    @Resource
    private DictDetailMapper dictDetailMapper;


    @Override
    public ResponseEntity getDictValueByIdOrCode(String code) {

        List<DictDetailVo> list = dictDetailMapper.getDictValueByIdOrCode(code);

        return RestResponse.ok(list);
    }
}
