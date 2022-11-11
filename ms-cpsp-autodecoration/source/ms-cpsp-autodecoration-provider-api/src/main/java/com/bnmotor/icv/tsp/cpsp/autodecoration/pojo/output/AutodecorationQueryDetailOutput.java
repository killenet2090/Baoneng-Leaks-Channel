package com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output;

import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.vo.ServicePrice;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @ClassName: AutodecorationQueryDetailOutput
* @Description: 汽车美容商家明细查询出参
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutodecorationQueryDetailOutput extends CPSPOutput {

    /**
     * 商家图片列表
     */
    private List<String> icons;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 评分
     */
    private String rating;

    /**
     * 地址
     */
    private String address;

    /**
     * 距离（距离为保留小数点后两位）
     */
    private String distance;

    /**
     * 销量
     */
    private String sales;

    /**
     * 营业时间
     */
    private String businessHours;

    /**
     * 商家电话
     */
    private String telephone;

    /**
     * 服务项目
     */
    private List<ServicePrice> services;

    /**
     * 地理位置经度信息
     */
    private String lng;

    /**
     * 地理位置纬度信息
     */
    private String lat;

}
