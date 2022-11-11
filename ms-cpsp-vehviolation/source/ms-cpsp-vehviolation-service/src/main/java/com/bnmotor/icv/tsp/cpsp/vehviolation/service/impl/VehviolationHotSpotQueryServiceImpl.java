package com.bnmotor.icv.tsp.cpsp.vehviolation.service.impl;

import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHotSpotQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHotSpotQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationHotSpotQueryService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @ClassName: VehviolationHotSpotQueryServiceImpl
 * @Description: 违章热点信息查询
 * @author: liuhuaqiao1
 * @date: 2021/1/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class VehviolationHotSpotQueryServiceImpl implements IVehviolationHotSpotQueryService {

    @Autowired
    private CPSPProxy cpspProxy;

    @Override
    public VehviolationHotSpotQueryOutput queryVehviolationHotSpot(String lng, String lat, String type, String mapType, String radius) {
        VehviolationHotSpotQueryInput input = VehviolationHotSpotQueryInput.builder().build();
        input.setLng(lng);
        input.setLat(lat);
        input.setType(StringUtils.isEmpty(type) ? "all" : type);
        input.setMapType(mapType);
        input.setRadius(radius);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.VEHVIOLATION_HOTSPOT_QUERY_API);
        input.setCacheKey(String.format(RedisKey.VEHVIOLATION_HOTSPOT_QUERY_LNGANDLAT_KEY, lng, lat));
        input.setCacheClass(VehviolationQueryOutput.class);
        VehviolationHotSpotQueryOutput output = cpspProxy.call(input);
        return output;
    }
}
