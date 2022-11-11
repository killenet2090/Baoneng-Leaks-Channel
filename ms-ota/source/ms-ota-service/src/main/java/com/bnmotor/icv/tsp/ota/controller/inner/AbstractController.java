package com.bnmotor.icv.tsp.ota.controller.inner;

import com.bnmotor.icv.adam.web.rest.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName:  AbstractController
 * @Description:   定义一些公共类
 * @author: xuxiaochang1
 * @date: 2020/6/12 14:36
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public abstract class AbstractController extends BaseController {
    @Autowired
    HttpServletRequest request;
}
