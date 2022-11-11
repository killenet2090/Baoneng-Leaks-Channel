package com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: AutodecorationQueryInput
* @Description: 汽车美容商家列表查询入参
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutodecorationQueryInput extends CPSPInput<AutodecorationQueryOutput> {

    /**
     * 城市区域编码
     */
    private String adCode;

    /**
     * 车架号
     */
    private String name;

    /**
     * 排序方式
     */
    private String sorting;

    /**
     * 地理位置经度
     */
    private String lng;

    /**
     * 地理位置纬度
     */
    private String lat;

}
