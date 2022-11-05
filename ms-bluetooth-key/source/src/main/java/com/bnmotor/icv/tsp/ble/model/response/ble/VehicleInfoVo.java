package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
/**
 * @ClassName: VehicleInfoVo
 * @Description: feign调用车辆服务返回
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class VehicleInfoVo {
    /**
     * 唯一记录ID
     */
    private  String id;

    /**
     * 车架号
     */
    private String vin;
    /**
     * 车辆模型
     */
    private  String vehModelName;
    /**
     * 品牌
     */
    private  String brandName;
    /**
     * 车系
     */
    private  String  vehSeriesName;
    /**
     * 款型
     */
    private  String yearStyleName;
    /**
     * 配置
     */
    private  String vehConfigName;
    /**
     * 动力类型
     */
     private String vehType;
    /**
     *
     */
    private String color;
    /**
     * 车牌
     */
    private String drivingLicPlate;
}
