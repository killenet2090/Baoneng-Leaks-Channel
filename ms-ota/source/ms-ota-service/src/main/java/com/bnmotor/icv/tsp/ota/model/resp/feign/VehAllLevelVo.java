package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: VehicleAllLevelVo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/8/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class VehAllLevelVo {

    /**
     * 车辆品牌
     */
    private List<VehBrandVo> brands;

    /**
     * 车系
     */
    private List<VehSeriesVo> series;

    /**
     * 车辆型号
     */
    private List<VehModelVo> models;

    /**
     * 车辆年款
     */
    private List<VehYearStyleVo> yearStyles;

    /**
     * 车辆配置
     */
    private List<VehConfigurationVo> configurations;
}
