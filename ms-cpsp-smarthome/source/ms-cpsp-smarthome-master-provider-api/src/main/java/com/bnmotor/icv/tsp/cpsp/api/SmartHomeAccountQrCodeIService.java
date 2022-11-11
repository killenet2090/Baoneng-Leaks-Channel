package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.AccountQrCodeInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountQrCodeOutput;

/**
 * @ClassName: SmartHomeEquipmenConditionIService
 * @Description: 智能家居家庭数据采集接口
 * @author: jiangchangyuan1
 * @date: 2021/2/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SmartHomeAccountQrCodeIService {
    /**
     * 生成二维码
     * @param input
     * @return
     */
    default AccountQrCodeOutput process(AccountQrCodeInput input) {
        return null;
    }
}
