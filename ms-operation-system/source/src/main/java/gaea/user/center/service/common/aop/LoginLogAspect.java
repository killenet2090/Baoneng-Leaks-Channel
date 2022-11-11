package gaea.user.center.service.common.aop;

import gaea.user.center.service.model.entity.LoginLogPo;
import gaea.user.center.service.service.ILoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName: LoginLogAspect
 * @Description: 登录日志切面类
 * @author: jiangchangyuan1
 * @date: 2020/9/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Aspect
@Component
@Slf4j
public class LoginLogAspect {

    @Autowired
    ILoginLogService loginLogService;

    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.wwj.springboot.service.impl.*.*(..))")'
     */
    @Pointcut("@annotation(gaea.user.center.service.common.annotation.LoginLogAnnotation)")
    public void loginLog() {
    }

    /**
     * 方法执行前执行日志打印
     * @param joinPoint
     */
    @Before("loginLog()")
    public void before(JoinPoint joinPoint){
       log.debug("登录日志保存开始.....");
    }

    /**
     * 方法返回之后执行日志保存方法，相当于MethodInterceptor
     */
    @AfterReturning("loginLog()")
    public void afterReturning(JoinPoint joinPoint) throws Throwable {
        Date time = new Date();
        try {
            //方法执行完成后增加日志
            addOperationLog(joinPoint,time);
        } catch (Exception e) {
            log.debug("LoginLogAspect 操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addOperationLog(JoinPoint joinPoint,Date time) {
        log.debug("自定义注解保存开始");
        //获取参数值
        Object[] args = joinPoint.getArgs();
        LoginLogPo loginLogPo = new LoginLogPo();
        loginLogPo.setLoginTime(time);
        loginLogPo.setUserName(args[0].toString());
        log.debug("记录日志:" + loginLogPo.toString());
        loginLogService.saveLoginLog(loginLogPo);
    }

    @After("loginLog()")
    public void after(JoinPoint joinPoint){
        log.debug("登录日志保存结束.....");
    }
}
