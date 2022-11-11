package com.bnmotor.icv.tsp.device.model.response.vehLevel;

import lombok.Data;

/**
 * @ClassName: VehicleYeaStyleVo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/8/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class VehYearStyleVo {

    /**
     * id
     */
    private Long id;

    /**
     * 车辆型号
     */
    private Long modelId;

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;
}
