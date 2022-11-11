package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Data;

/**
 * @ClassName: VehBrandVo
 * @Description: 车辆品牌
 * @author: zhangwei2
 * @date: 2020/10/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehBrandVo {
    /**
     * id
     */
    private Long id;
    /**
     * 品牌code
     */
    private String code;
    /**
     * 品牌名称
     */
    private String name;
}
