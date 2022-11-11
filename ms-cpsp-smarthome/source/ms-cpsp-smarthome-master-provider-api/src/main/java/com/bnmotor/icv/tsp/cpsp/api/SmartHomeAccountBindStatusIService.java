package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.AccountBindStatusInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountBindStatusOutput;

/**
 * @ClassName: SmartHomeAccountBindStatusIService
 * @Description: 智能家居账户绑定状态查询接口
 * @author: jiangchangyuan1
 * @date: 2021/2/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SmartHomeAccountBindStatusIService {
    /**
     * 账户绑定状态查询
     * @param input
     * @return
     */
    default AccountBindStatusOutput process(AccountBindStatusInput input) {
        return null;
    }
}
