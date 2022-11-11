package com.bnmotor.icv.tsp.common.data.controller;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.common.data.service.IDictDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :luoyang
 * @date_time :2020/11/12 15:56
 * @desc:
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("v1/dict")
@Api(value = "字典相关" , tags = "字典相关接口")
public class DictController {


    @Autowired
    private IDictDetailService dictDetailIService;

    @GetMapping("get")
    @ApiOperation(value = "根据字典code获取值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity getDictValueByIdOrCode(@RequestParam(required = false) String code){
        if (StringUtils.isEmpty(code)){
            throw new AdamException(RespCode.USER_INVALID_INPUT);
        }

        ResponseEntity entity = dictDetailIService.getDictValueByIdOrCode(code);

        return entity;
    }


}
