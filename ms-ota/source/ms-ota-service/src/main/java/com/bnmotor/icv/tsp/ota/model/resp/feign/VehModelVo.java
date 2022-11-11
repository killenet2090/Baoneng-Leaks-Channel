package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Data;

/**
 * @ClassName: VehicleModelVo
 * @Description: 车辆型号
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehModelVo {
    /**
     * id
     */
    private Long id;
    /**
     * 车系id
     */
    private Long seriesId;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
}
