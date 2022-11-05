package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.entity.BleKeyServicePo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserCheckVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;
import com.bnmotor.icv.tsp.ble.mapper.BleUserMapper;
import com.bnmotor.icv.tsp.ble.service.BleUserService;
import com.bnmotor.icv.tsp.ble.service.feign.UserFeignService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ClassName: BleUserServiceImpl
 * @Description: 与用户中心交互实现类
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class BleUserServiceImpl extends ServiceImpl<BleUserMapper, BleKeyServicePo> implements BleUserService {

    @Autowired
    UserFeignService userFeignService;

    @Override
    public UserVo getUserName(String userId) {
        RestResponse<UserVo> response = userFeignService.getUserInfo(userId);
         if (!Optional.ofNullable(response).isPresent()){
             throw new AdamException(RespCode.USER_PARAM_TYPE_ERROR.USER_ACCOUNT_NOT_EXIST.getValue(), RespCode.USER_ACCOUNT_NOT_EXIST.getDescription());
         }
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode())
                || !Optional.ofNullable(response.getRespData()).isPresent()) {
            log.error("调用用户服务获取用户名发生异常{}"+response.getRespMsg());
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            UserVo userVo  = response.getRespData();
            return userVo;
        }


    }

    @Override
    public UserCheckVo getUserCheck(String phoneNum) {
        try {
            UserCheckVo userCheckVo = new UserCheckVo();
            RestResponse<UserCheckVo> response = userFeignService.checkUserAccount(phoneNum);
            log.info(JsonUtil.toJson(response));
            if (RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
                if (response.getRespData() != null) {
                    userCheckVo = response.getRespData();
                }
                return userCheckVo;
            } else {
                log.error("调用用户服务获取用户名发生异常{}", JsonUtil.toJson(response));
                throw new AdamException(response.getRespCode(), response.getRespMsg());
            }
        } catch (JsonProcessingException ex) {
            log.error("获取用户信息-json转换发生异常{}", ex.getMessage());
            throw new AdamException(RespCode.OTHER_SERVICE_INVOKE_ERROR);
        }

    }
}
