package com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.vo.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @ClassName: AutodecorationQueryOutput
* @Description: 汽车美容商家列表查询出参
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutodecorationQueryOutput extends CPSPOutput {

    /**
     * 商家列表
     */
    private List<Merchant> merchants;

}
