package com.bnmotor.icv.tsp.sms.controller;


import cn.jiguang.common.resp.BaseResult;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.sms.common.ReqContext;
import com.bnmotor.icv.tsp.sms.model.request.TemplateSmsDto;
import com.bnmotor.icv.tsp.sms.service.ISendSmsStrategy;
import com.bnmotor.icv.tsp.sms.service.factory.SendSmsStrategyFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: JsmsController
 * @Description: 发送短信controller
 * @author: huangyun1
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@RestController
@RequestMapping("/v1/sms")
@Api(value = "短信controller", tags = {"极光短信接口"})
public class JsmsController {
    @Autowired
    SendSmsStrategyFactory sendSmsStrategyFactory;

    /**
     * 通过模板发送短信
     *
     * @return
     */
    @PostMapping("/message/sendByTemplate")
    @ApiOperation(value = "发送短信")
    public ResponseEntity sendByTemplate(@ApiParam(name = "smsMsgDto", value = "传入json格式", required = true)
                                          @Validated @RequestBody TemplateSmsDto templateMsgDto) {
        templateMsgDto.setFromUserId(ReqContext.getUid());
        ISendSmsStrategy sendMsgStrategy = sendSmsStrategyFactory.getStrategy(templateMsgDto);
        BaseResult pushResult  = sendMsgStrategy.sendSms(templateMsgDto);
        if (pushResult != null && pushResult.getResponseCode() == HttpStatus.OK.value()) {
            return RestResponse.ok(null);
        } else {
            return RestResponse.error(RespCode.SERVER_EXECUTE_ERROR);
        }
    }
}
