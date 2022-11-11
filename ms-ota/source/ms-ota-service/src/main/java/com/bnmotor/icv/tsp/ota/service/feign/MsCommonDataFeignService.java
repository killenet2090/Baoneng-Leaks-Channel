package com.bnmotor.icv.tsp.ota.service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.req.feign.AreaInfo;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.MsCommonDataFeignFallbackFactory;

/**
 * @ClassName: MsCommonDataFeignService
 * @Description: 基础数据服务
 * @author: xuxiaochang1
 * @date: 2020/9/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-common-data", fallback = MsCommonDataFeignFallbackFactory.class)
public interface MsCommonDataFeignService {
    /**
     * 获取全国区划
     *
     * @param code
     * @return
     */
    @GetMapping("/v1/geo/cn/tree/list")
    RestResponse list(@RequestParam(value = "code") String code);

    /**
     * inner/geo/list
     * 批量获取地理位置列表信息
     */
    @GetMapping("/inner/geo/list")
    RestResponse<List<AreaInfo>> batchlist(@RequestParam(value = "code") String code, @RequestParam(value = "codes") List<String> codes);
}