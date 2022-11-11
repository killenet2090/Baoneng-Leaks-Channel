package com.bnmotor.icv.tsp.cpsp.vehviolation.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: VehViolationHandleRequest
 * @Description: 违章办理入参类
 * @author: liuhuaqiao1
 * @date: 2021/4/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class VehViolationHandleDto {

    /**
     * 车架号
     */
    @NotBlank(message = "车架号不能为空")
    private String vin;

    /**
     * 违章ID
     */
    @NotNull(message = "违章ID不能为空")
    List<String> violationIds;
}
