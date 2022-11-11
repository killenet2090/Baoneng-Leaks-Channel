package com.bnmotor.icv.tsp.device.model.response.vehLevel;

import lombok.Data;

/**
 * @ClassName: VehicleSeriesVo
 * @Description: 车系
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehSeriesVo {
    /**
     * id
     */
    private Long id;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
}
