package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo;

import lombok.Data;

/**
 * @ClassName: HotSpotVo
 * @Description: 违章热点查询
 * @author: liuhuaqiao1
 * @date: 2021/1/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class HotSpotVo {

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 地点名称
     */
    private String location;

    /**
     * 违章数量
     */
    private String illegalCount;

    /**
     * 违章类型
     */
    private String type;

}
