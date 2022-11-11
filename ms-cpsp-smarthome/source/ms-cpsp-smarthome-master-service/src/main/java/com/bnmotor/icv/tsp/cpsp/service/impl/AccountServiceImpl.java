package com.bnmotor.icv.tsp.cpsp.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.*;
import com.bnmotor.icv.tsp.cpsp.pojo.output.*;
import com.bnmotor.icv.tsp.cpsp.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: EquipmentServiceImpl
 * @Description: 智能家居设备服务实现类
 * @author: jiangchangyuan1
 * @date: 2021/2/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private CPSPProxy cpspProxy;

    /**
     * 生成二维码
     * @param vin 车架号
     * @return
     */
    @Override
    public AccountQrCodeOutput createQrcode(HttpServletRequest request, String vin) throws AdamException {
        AccountQrCodeInput input = AccountQrCodeInput.builder().build();
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        String userId = request.getHeader("uid");
        if(!StringUtils.isEmpty(userId)){
            input.setUserId(userId);
        }
        log.info("当前登录用户ID:{}",userId);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_ACCOUNT_QRCODE);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_ACCOUNT_QRCODE_KEY, vin));
        input.setCacheClass(AccountQrCodeOutput.class);
        AccountQrCodeOutput output = cpspProxy.call(input);
        return output;
    }

    /**
     * 查询绑定状态
     * @param request
     * @param vin 车架号
     * @return
     */
    @Override
    public AccountBindStatusOutput getAccountBindStatus(HttpServletRequest request, String vin) {
        AccountBindStatusInput input = AccountBindStatusInput.builder().build();
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        String userId = request.getHeader("uid");
        if(!StringUtils.isEmpty(userId)){
            input.setUserId(userId);
        }
        log.info("当前登录用户ID:{}",userId);
        input.setUserId(userId);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_ACCOUNT_BIND_STATUS);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_BIND_ACCOUNT_STATUS_KEY, vin));
        input.setCacheClass(AccountBindStatusOutput.class);
        AccountBindStatusOutput output = cpspProxy.call(input);
        return output;
    }

    /**
     * 账户解除绑定
     * @param request
     * @param vin 车架号
     * @return
     */
    @Override
    public AccountUnBindOutput accountUnbind(HttpServletRequest request, String vin) {
        AccountUnbindInput input = AccountUnbindInput.builder().build();
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        String userId = request.getHeader("uid");
        if(!StringUtils.isEmpty(userId)){
            input.setUserId(userId);
        }
        log.info("当前登录用户ID:{}",userId);
        input.setUserId(userId);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_ACCOUNT_UNBIND);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_ACCOUNT_UNBIND_KEY, vin));
        input.setCacheClass(AccountUnBindOutput.class);
        AccountUnBindOutput output = cpspProxy.call(input);
        return output;
    }
}
