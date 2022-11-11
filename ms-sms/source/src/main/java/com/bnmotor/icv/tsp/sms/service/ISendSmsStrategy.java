package com.bnmotor.icv.tsp.sms.service;

import cn.jiguang.common.resp.BaseResult;
import com.bnmotor.icv.tsp.sms.model.request.AbstractSmsDto;

/**
 * @ClassName: SmsStrategy
 * @Description: 消息策略
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ISendSmsStrategy<R extends BaseResult, T extends AbstractSmsDto> {
    /**
     * 发送消息
     * @param abstractMsgDto 请求参数
     * @return 返回数据
     */
    R sendSms(T abstractMsgDto);

    /**
     * 校验是否开启发送短信
     * @param parameter
     * @return
     */
    boolean checkEnableSms(T parameter);
}
