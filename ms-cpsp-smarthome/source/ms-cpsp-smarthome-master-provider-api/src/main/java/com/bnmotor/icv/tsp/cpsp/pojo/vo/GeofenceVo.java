package com.bnmotor.icv.tsp.cpsp.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: GeofenceVo
 * @Description: 地理围栏响应实体
 * @author: jiangchangyuan1
 * @date: 2021/3/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class GeofenceVo implements Serializable {
    /**
     * 地理围栏id
     */
    private String geofenceId;
    /**
     * 车架号
     */
    private String vin;
    /**
     * logo
     */
    private String logo;
    /**
     * 地理围栏标题
     */
    private String title;
    /**
     * 半径(米)
     */
    private String radius;
    /**
     * 地址
     */
    private String address;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
}
