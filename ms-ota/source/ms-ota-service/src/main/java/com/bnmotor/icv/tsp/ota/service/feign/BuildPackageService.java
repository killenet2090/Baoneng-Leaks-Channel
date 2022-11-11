package com.bnmotor.icv.tsp.ota.service.feign;

import com.bnmotor.icv.tsp.ota.model.req.feign.BuildPackageDto;
import com.bnmotor.icv.tsp.ota.model.req.feign.QueryPackageStatusDto;
import com.bnmotor.icv.tsp.ota.model.resp.feign.BuildResultInfo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.QueryResultInfo;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.BuildPackageFallbackFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: BuildPackageService
 * @Description: 做包服务服务接口
 * @author: xuxiaochang1
 * @date: 2020/9/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */

@FeignClient(value = "ms-ota-build-package-service",fallback = BuildPackageFallbackFactory.class)
@RequestMapping("/v1/bulid")
public interface BuildPackageService {
    @PostMapping("/task/create")
    @ApiOperation(value = "发起构建升级包任务")
    BuildResultInfo createTask(@ApiParam(name = "BuildPackageDTO", value = "传入json格式", required = true)
                               @RequestBody BuildPackageDto packageDto);

    @PostMapping(value = "/task/status")
    @ApiOperation(value = "查看升级包任务状态")
    QueryResultInfo queryStatus( @RequestBody QueryPackageStatusDto packageDto);
}
