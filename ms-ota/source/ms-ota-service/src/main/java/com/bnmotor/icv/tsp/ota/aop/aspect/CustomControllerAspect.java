package com.bnmotor.icv.tsp.ota.aop.aspect;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import com.bnmotor.icv.tsp.ota.service.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @ClassName: CustomControllerAspect
 * @Description: 自定义切面
 * @author: zhangwei2
 * @date: 2020/6/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Aspect
@Component
@Slf4j
public class CustomControllerAspect {

    /*private final String authorization = "Authorization";*/

    @Autowired
    IAuthenticationService authenticationService;
    
    public CustomControllerAspect() {
    }

    @PostConstruct
    public void init(){
        log.info("CustomControllerAspect init");
    }

    @Pointcut("execution(* *(..)) && (@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController))")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        if (ra != null && sra != null) {
            HttpServletRequest request = sra.getRequest();

            List<Object> params = extractRequestParams(request, pjp);
            log.info("[========================================== START ========================================]");
            log.info("URL : {}" + request.getRequestURL().toString());
            log.info("HTTP_METHOD : {}", request.getMethod());
            log.info("IP : " + request.getRemoteAddr());
            Logger var10000 = log;
            String var10001 = pjp.getSignature().getDeclaringTypeName();
            var10000.info("CLASS_METHOD : {}", var10001 + "." + pjp.getSignature().getName());
            log.info("REQUEST ARGS : {}", params);
            /*String token = request.getHeader(authorization);*/
            log.info("REQUEST_USER_ID : {}",  request.getHeader("userId"));
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            Annotation[] annotations = targetMethod.getAnnotations();

            /*if(!matchAnnotation(annotations, NoPrintParam.class)){
            }*/
            long startTime = System.currentTimeMillis();

            //if need wrap parameter
            if(matchAnnotation(annotations, WrapBasePo.class)){
                String userId = request.getHeader("userId");
                String userName = request.getHeader("userName");
                log.info("userId={}, userName={}", userId, userName);
               /* if(Objects.nonNull(userId)){
                    com.bnmotor.icv.tsp.ota.model.resp.feign.UserInfoVo userInfoVo = authenticationService.findUserInfo(userId);
                	log.info("userInfoVo={}", userInfoVo);
                    String username = Objects.nonNull(userInfoVo) ? userInfoVo.getUserName(): null;*/
                    //username = StringUtils.isEmpty(username) ? userInfoVo.getUserId(): null;
                    Object[] args = pjp.getArgs();
                    if(Objects.nonNull(args)) {
                        for (Object parameter : args) {
                        	if (Objects.isNull(parameter)) {
                        		continue;
                        	}
                            if (BasePo.class.isAssignableFrom(parameter.getClass())) {
                                ((BasePo) parameter).setCreateBy(userName);
                                ((BasePo) parameter).setUpdateBy(userName);
                            }
                            if (AbstractBase.class.isAssignableFrom(parameter.getClass())) {
                                ((AbstractBase) parameter).setCreateBy(userName);
                                ((AbstractBase) parameter).setUpdateBy(userName);
                            }
                        }
                    }/*else{
                        for (Object parameter : args) {
                        	if (Objects.isNull(parameter)) {
                        		continue;
                        	}
                            if (BasePo.class.isAssignableFrom(parameter.getClass())) {
                                ((BasePo) parameter).setCreateBy(CommonConstant.USER_ID_SYSTEM);
                                ((BasePo) parameter).setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                            }
                            if (AbstractBase.class.isAssignableFrom(parameter.getClass())) {
                                ((AbstractBase) parameter).setCreateBy(CommonConstant.USER_ID_SYSTEM);
                                ((AbstractBase) parameter).setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                            }
                        }
                    }*/
                /*}else{
                    Object[] args = pjp.getArgs();
                    for (Object parameter : args) {
                    	if (Objects.isNull(parameter)) {
                    		continue;
                    	}
                        if (BasePo.class.isAssignableFrom(parameter.getClass())) {
                            ((BasePo) parameter).setCreateBy(CommonConstant.USER_ID_SYSTEM);
                            ((BasePo) parameter).setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                        }
                        if (AbstractBase.class.isAssignableFrom(parameter.getClass())) {
                            ((AbstractBase) parameter).setCreateBy(CommonConstant.USER_ID_SYSTEM);
                            ((AbstractBase) parameter).setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                        }
                    }
                }*/
            }

            boolean var16 = false;

            Object var8;
            try {
                var16 = true;
                Object response = pjp.proceed();
                log.info("RESPONSE:{}", response != null ? JsonUtil.toJson(response) : "");
                var8 = response;
                var16 = false;
            } catch (Throwable var17) {
                if (var17 instanceof MethodArgumentNotValidException) {
                    log.info("RESPONSE ERROR:{}", var17.getMessage());
                } else {
                    var17.printStackTrace();
                    log.error("RESPONSE ERROR:{}", var17.getStackTrace());
                }
                throw var17;
            } finally {
                if (var16) {
                    long endTime = System.currentTimeMillis();
                    log.info("SPEND TIME : {}ms", endTime - startTime);
                    log.info("[========================================== END ==========================================]");
                }
            }

            long endTime = System.currentTimeMillis();
            log.info("SPEND TIME : {}ms", endTime - startTime);
            log.info("[========================================== END ==========================================]");
            return var8;
        } else {
            return pjp.proceed();
        }
    }



    /**
     * 是否有配置的注解
     * @param annotations
     * @param annotationClz
     * @return
     */
    private boolean matchAnnotation(Annotation[] annotations, Class<?> annotationClz){
        if(Objects.isNull(annotations) || Objects.isNull(annotationClz)){
            return false;
        }
        return Stream.of(annotations).filter(item -> item.annotationType().equals(annotationClz)).findFirst().isPresent();
    }

    /**
     * 提取请求参数：
     *   参数来源：@RequestParam、@RequestBody、@PathVariable
     * @param request
     * @param pjp
     */
    private List<Object> extractRequestParams(HttpServletRequest request, ProceedingJoinPoint pjp){
        List<Object> params = new ArrayList<>();
        //获取RequestParam参数
        request.getParameterMap().forEach((k, v) -> {
            if(Objects.nonNull(v) && v.length >= 1) {
                params.add(k + "=" + v[0]);
            }
        });
        //获取PathVariable参数和RequestBody参数
        for (int i=0; i< pjp.getArgs().length; i++) {
            Object arg = pjp.getArgs()[i];
            // System.out.println(arg);
            if(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse){
                continue;
            }
            if(arg instanceof MultipartFile[]){
                MultipartFile[] files = (MultipartFile[])arg;
                for (MultipartFile file : files) {
                    params.add(file.getOriginalFilename());
                }
            }else if(arg instanceof MultipartFile){
                params.add(((MultipartFile) arg).getOriginalFilename());
            } else{
                params.add(arg);
            }
        }
        return params;
    }
}
