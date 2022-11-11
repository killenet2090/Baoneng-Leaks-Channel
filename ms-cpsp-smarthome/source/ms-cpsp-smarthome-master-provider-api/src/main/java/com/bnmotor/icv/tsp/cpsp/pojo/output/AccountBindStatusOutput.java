package com.bnmotor.icv.tsp.cpsp.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import lombok.Data;

/**
 * @ClassName: AccountQrCodeOutput
 * @Description: 查询绑定账户状态响应体
 * @author: jiangchangyuan1
 * @date: 2021/3/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class AccountBindStatusOutput extends CPSPOutput {
    /**
     * 车架号
     */
    private String vin;
}
