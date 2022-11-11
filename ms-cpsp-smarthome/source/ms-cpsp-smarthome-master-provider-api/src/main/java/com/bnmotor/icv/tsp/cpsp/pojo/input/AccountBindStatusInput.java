package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountBindStatusOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountQrCodeOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: AccountBindStatusInput
 * @Description: 设备卡片排序请求实体
 * @author: jiangchangyuan1
 * @date: 2021/2/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBindStatusInput extends CPSPInput<AccountBindStatusOutput> {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 用户id
     */
    private String userId;
}
