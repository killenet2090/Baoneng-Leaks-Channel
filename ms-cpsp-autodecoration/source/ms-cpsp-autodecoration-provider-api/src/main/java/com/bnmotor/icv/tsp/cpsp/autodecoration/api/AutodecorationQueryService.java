package com.bnmotor.icv.tsp.cpsp.autodecoration.api;


import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input.AutodecorationQueryDetailInput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input.AutodecorationQueryInput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryDetailOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryOutput;

/**
* @ClassName: AutodecorationQueryService
* @Description: 汽车美容商家查询标准接口
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface AutodecorationQueryService {

    /**
     * 商家列表查询
     */
    default AutodecorationQueryOutput process(AutodecorationQueryInput input) {
        return null;
    }

    /**
     * 商家明细查询
     */
    default AutodecorationQueryDetailOutput process(AutodecorationQueryDetailInput input) {
        return null;
    }

}
