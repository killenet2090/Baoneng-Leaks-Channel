package com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input;

import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryDetailOutput;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: AutodecorationQueryDetailInput
* @Description: 汽车美容商家明细查询入参
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutodecorationQueryDetailInput extends CPSPInput<AutodecorationQueryDetailOutput> {

    /**
     * 商家ID
     */
    private String merchantId;

}
