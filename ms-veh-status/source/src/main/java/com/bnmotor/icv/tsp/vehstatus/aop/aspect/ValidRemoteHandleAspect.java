package com.bnmotor.icv.tsp.vehstatus.aop.aspect;

import com.alibaba.fastjson.JSONObject;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.vehstatus.aop.annotation.ValidRemoteHandle;
import com.bnmotor.icv.tsp.vehstatus.common.ReqContext;
import com.bnmotor.icv.tsp.vehstatus.common.enums.BusinessRetEnum;
import com.bnmotor.icv.tsp.vehstatus.service.feign.TspUserFeignService;
import io.micrometer.core.ipc.http.HttpSender;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName: ValidRemoteHandleAspect
 * @Description: ValidRemoteHandle远控操作校验自定义注解
 * @author: huangyun1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Aspect
@Component
public class ValidRemoteHandleAspect {

    @Resource
    private TspUserFeignService tspUserFeignService;

    @Pointcut("@annotation(com.bnmotor.icv.tsp.vehstatus.aop.annotation.ValidRemoteHandle)")
    public void annotationPointcut() {

    }

    @Before("annotationPointcut() && @annotation(validRemoteHandle)")
    public void beforePointcut(JoinPoint joinPoint, ValidRemoteHandle validRemoteHandle) {
        String argValue = null;
        Object[] args = joinPoint.getArgs();
        String argName = validRemoteHandle.argName();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Long uid = ReqContext.getUid();

        if (uid == null) {
            throw new AdamException(BusinessRetEnum.USER_NOT_LOGIN_ERROR);
        }

        if (HttpSender.Method.GET.name().equals(request.getMethod())) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if(parameterMap.containsKey(argName)) {
                argValue = (parameterMap.get(argName)[0] != null ? parameterMap.get(argName)[0] : null);
            }
        } else {
            for(Object arg : args) {
                Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(arg), Map.class);
                if(map.containsKey(argName)) {
                    argValue = (map.get(argName) != null ? map.get(argName).toString() : null);
                    break;
                }
            }
        }

        if (StringUtils.isBlank(argValue)) {
            throw new AdamException(RespCode.USER_REQUIRED_PARAMETER_EMPTY);
        }

        ValidRemoteHandle.ArgType argType = validRemoteHandle.argType();
        //根据vin判断
        /*if (ValidRemoteHandle.ArgType.VIN.name().equals(argType.name())) {
            //校验用户是否已绑定该车或授权该车
            RestResponse<Boolean> result = tspUserFeignService.checkUserHasVehicle(uid, argValue);
            if (!RespCode.SUCCESS.getValue().equals(result.getRespCode())) {
                throw new AdamException(result.getRespCode(), result.getRespMsg());
            }
            if (result.getRespData()) {
                return;
            }
            throw new AdamException(BusinessRetEnum.USER_NOT_PRIVILEGE_ERROR);
        }*/
    }
}
