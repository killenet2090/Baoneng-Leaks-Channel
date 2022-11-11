package com.bnmotor.icv.tsp.operation.veh.model.response;

import lombok.Data;

/**
 * @ClassName: VehModelDetailVo
 * @Description: 车辆型号详情
 * @author: zhangwei2
 * @date: 2020/7/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehProjectDetailVo {
    /**
     * 型号编码
     */
    private String code;
    /**
     * 型号名称
     */
    private String name;
    /**
     * 车辆类型：0-燃油；1-电动；2-混动
     */
    private Integer vehType;

    /**
     * 车辆类型 1-纯电动；2-燃油车
     */
    private String vehTypeValue;
}