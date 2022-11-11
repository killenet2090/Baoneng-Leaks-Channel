package com.bnmotor.icv.tsp.ota.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.MsCommonDataFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: MsOperationTagsFeignService
 * @Description: 标签数据服务（可考虑由web后台直接调用）
 * @author: xuxiaochang1
 * @date: 2020/9/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-operation-tags", fallback = MsCommonDataFeignFallbackFactory.class)
public interface MsOperationTagsFeignService {
    /**
     * 查询标签列表
     * @param businessId
     * @param categoryId
     * @param tagIds
     * @return
     */
    @GetMapping("/inner/tag/list")
    RestResponse list(@RequestParam("businessId") Long businessId,
                      @RequestParam(value = "categoryId", required = false) Long categoryId,
                      @RequestParam(value = "tagIds", required = false) List<Long> tagIds
    );
}