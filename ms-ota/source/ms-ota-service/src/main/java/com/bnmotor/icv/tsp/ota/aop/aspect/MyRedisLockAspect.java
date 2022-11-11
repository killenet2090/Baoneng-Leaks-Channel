package com.bnmotor.icv.tsp.ota.aop.aspect;

import com.bnmotor.icv.adam.data.redis.lock.annotation.RedisLock;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: MyRedisLockAspect
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/7 15:24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Aspect
@Component
public class MyRedisLockAspect {
    private static final Logger log = LoggerFactory.getLogger(MyRedisLockAspect.class);
    @Autowired
    private RedissonClient redissonClient;

    public MyRedisLockAspect() {
    }

    @Pointcut("@annotation(com.bnmotor.icv.adam.data.redis.lock.annotation.RedisLock)")
    public void redisLockAspect() {
    }

    @Around("redisLockAspect() && @annotation(com.bnmotor.icv.adam.data.redis.lock.annotation.RedisLock)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object returnObj = null;
        Class targetClass = pjp.getTarget().getClass();
        String methodName = pjp.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature)pjp.getSignature()).getMethod().getParameterTypes();
        Method method = targetClass.getMethod(methodName, parameterTypes);
        RedisLock annotation = (RedisLock)method.getAnnotation(RedisLock.class);
        Object[] arguments = pjp.getArgs();
        String lockName = this.getLockName(annotation, arguments);
        Assert.hasText(lockName, "Redis锁名字不能为空");
        long waitTime = annotation.waitTime();
        long leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();
        RLock rLock = null;

        Logger var10000;
        long var10001;
        try {
            var10000 = log;
            var10001 = Thread.currentThread().getId();
            var10000.info("线程:" + var10001 + "尝试获取Redis锁，lockName:" + lockName);
            rLock = annotation.fairLock() ? this.redissonClient.getFairLock(lockName) : this.redissonClient.getLock(lockName);
            boolean isLocked = rLock.tryLock(waitTime, leaseTime, timeUnit);
            if (!isLocked) {
                var10000 = log;
                var10001 = Thread.currentThread().getId();
                var10000.info("线程:" + var10001 + "没有获得到Redis锁，lockName:" + lockName);
                throw new MyRedisException("Redis锁没有获取到，lockName:" + lockName);
            }
        /*}catch (MyRedisException e){
            throw  e;
        }catch (Throwable e){
            throw new MyRedisException("获取锁异常", e);
        }finally {
            if (rLock != null && rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                var10000 = log;
                var10001 = Thread.currentThread().getId();
                var10000.info("线程:" + var10001 + "成功释放Redis锁，lockName:" + lockName);
            }
        }

        try {*/
            var10000 = log;
            var10001 = Thread.currentThread().getId();
            var10000.info("线程:" + var10001 + "成功获得到Redis锁，lockName:" + lockName);
            returnObj = pjp.proceed();
        } catch (MyRedisException e){
            log.error("获取锁失败.e.getMessage={}", e);
            throw  e;
        } catch (Exception var20) {
            log.error("锁操作异常.e.getMessage={}", var20);
            throw var20;
        } finally {
            if (rLock != null && rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                var10000 = log;
                var10001 = Thread.currentThread().getId();
                var10000.info("线程:" + var10001 + "成功释放Redis锁，lockName:" + lockName);
            }
        }

        return returnObj;
    }

    private String getLockName(RedisLock redisLock, Object[] args) {
        String lockName = redisLock.lockName();
        if (StringUtils.isBlank(lockName) && redisLock.argNum() >= 0) {
            if (StringUtils.isBlank(redisLock.param())) {
                lockName = args[redisLock.argNum()].toString();
            } else {
                try {
                    lockName = PropertyUtils.getProperty(args[redisLock.argNum()], redisLock.param()).toString();
                } catch (Exception var7) {
                    log.error("获取锁名称异常.e.getMessage={}", var7.getMessage());
                }
            }
        }

        if (StringUtils.isNotBlank(lockName)) {
            String preLockName = redisLock.lockNamePre();
            String postLockName = redisLock.lockNamePost();
            String separator = redisLock.separator();
            if (StringUtils.isNotBlank(preLockName)) {
                lockName = preLockName + separator + lockName;
            }

            if (StringUtils.isNotBlank(postLockName)) {
                lockName = lockName + separator + postLockName;
            }
        }

        return lockName;
    }
}
