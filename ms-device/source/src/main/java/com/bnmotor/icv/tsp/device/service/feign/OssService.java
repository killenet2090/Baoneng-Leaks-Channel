package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.excel.FileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @ClassName: OssService
 * @Description: 基于feign调用文件上传
 * @author: zhangjianghua1
 * @date: 2020/5/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-common-oss")
public interface OssService {
    /**
     * 上传本地文件
     */
    @PostMapping(value = "/v1/oss/upload",produces = {MediaType.APPLICATION_JSON_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestResponse uploadFiles(@RequestHeader(name = "uid") Long uid, @RequestBody FileDto fileDto);
}
