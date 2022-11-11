package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeAccountBindStatusIService;
import com.bnmotor.icv.tsp.cpsp.api.SmartHomeAccountQrCodeIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.AccountBindStatusInput;
import com.bnmotor.icv.tsp.cpsp.pojo.input.AccountQrCodeInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountBindStatusOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountQrCodeOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @ClassName: SmartHomeAccountBindStatusServiceImpl
* @Description: 供应商查询账户绑定状态
* @author: jiangchangyuan1
* @date: 2021/2/22
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_ACCOUNT_BIND_STATUS)
public class SmartHomeAccountBindStatusServiceImpl implements SmartHomeAccountBindStatusIService, AbstractStrategy<AccountBindStatusInput, AccountBindStatusOutput> {


    @Override
    public AccountBindStatusOutput call(AccountBindStatusInput input) {
        AccountBindStatusOutput output = this.process(input);
        return output;
    }

    @Override
    public AccountBindStatusOutput process(AccountBindStatusInput input) {
        AccountBindStatusOutput output = new AccountBindStatusOutput();
        output.setVin(input.getVin());
        return output;
    }
}
