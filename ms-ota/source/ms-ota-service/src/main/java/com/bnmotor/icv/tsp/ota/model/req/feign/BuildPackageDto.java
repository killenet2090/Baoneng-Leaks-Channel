package com.bnmotor.icv.tsp.ota.model.req.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName: BuildPackageDto
 * @Description: 构建升级包请求对象
 * @author: handong
 * @date: 2020/9/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "构建升级包请求对象", description = "构建升级包请求对象")
public class BuildPackageDto {
    private Long packageId;
    private String targetVersionFileKey;
    private String sourceVersionFileKey;
    private Integer type;
    private String packageName;
    private String targetHash;
    private String sourceHash;
}
