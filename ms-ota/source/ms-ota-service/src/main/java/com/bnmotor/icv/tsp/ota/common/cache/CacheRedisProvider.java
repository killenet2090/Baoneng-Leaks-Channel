package com.bnmotor.icv.tsp.ota.common.cache;

import com.bnmotor.icv.adam.data.redis.AbstractRedisProvider;
import com.bnmotor.icv.tsp.ota.util.OtaJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import com.bnmotor.icv.adam.core.utils.JsonUtil;


/**
 * @ClassName: StringRedisProvider
 * @Description: 基于String的Redis客户端封装
 * @author: hao.xinyue
 * @date: 2020/3/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 * 
 * @fix: 2020-12-24
 * 使用自定义OtaJsonUtil.java 基础框架中的JsonUtil反序列化LocalDateTime时会报错
 */
@Component("otaRedisProvider")
@Slf4j
public class CacheRedisProvider extends AbstractRedisProvider
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    public StringRedisProvider(StringRedisTemplate stringRedisTemplate)
//    {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }

    /**
     * 获取redis数据
     *
     * @param key
     * @return
     */
    @Override
    public String getStr(String key)
    {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取多个Redis数据
     *
     * @param keys
     * @return
     */
    @Override
    public List<String> getMultiStr(List<String> keys)
    {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 存储redis数据
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    @Override
    public void setStr(String key, String value, long timeout, TimeUnit unit)
    {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 存储Redis数据
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @param <T>     序列化对象
     * @throws IOException
     */
    @Override
    public <T> void setObject(String key, T value, long timeout, TimeUnit unit) throws IOException
    {
        String strJson = OtaJsonUtil.toJson(value);
        setStr(key, strJson, timeout, unit);
    }

    /**
     * 获取对象
     *
     * @param key
     * @param clazz 序列化对象
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T getObject(String key, Class<T> clazz) throws IOException
    {
        String strJson = getStr(key);
        if (StringUtils.isBlank(strJson))
        {
            return null;
        }

        return OtaJsonUtil.toObject(strJson, clazz);
    }

    /**
     * 存储Redis数据并且返回旧值
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    @Override
    public String getAndSetStr(String key, String value, long timeout, TimeUnit unit)
    {
        String valueOld = stringRedisTemplate.opsForValue().getAndSet(key, value);
        stringRedisTemplate.expire(key, timeout, unit);
        return valueOld;
    }

    /**
     * 存储Redis数据并且返回旧值
     *
     * @param key
     * @param value
     * @param clazz
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T getAndSetObject(String key, T value, Class<T> clazz, long timeout, TimeUnit unit) throws IOException
    {
        String strJson = OtaJsonUtil.toJson(value);
        String strValueOld = stringRedisTemplate.opsForValue().getAndSet(key, strJson);
        stringRedisTemplate.expire(key, timeout, unit);

        if (StringUtils.isBlank(strValueOld))
        {
            return null;
        }

        return OtaJsonUtil.toObject(strJson, clazz);
    }

    /**
     * 删除Redis键值
     *
     * @param key
     * @return
     */
    @Override
    public Boolean delete(String key)
    {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit)
    {
        return stringRedisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param date
     * @return
     */
    @Override
    public Boolean expireAt(String key, Date date)
    {
        return stringRedisTemplate.expireAt(key, date);
    }

    /**
     * 是否存在Key
     *
     * @param key
     * @return
     */
    @Override
    public Boolean hasKey(String key)
    {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 自增长，步长为1
     *
     * @param key
     * @return
     */
    @Override
    public Long increment(String key)
    {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增长
     *
     * @param key
     * @param delta
     * @return
     */
    @Override
    public Long increment(String key, Long delta)
    {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 获取redis Hash值
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object getHash(String key, String hashKey)
    {
        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取redis 多个Hash值
     *
     * @param key
     * @param hashKeys
     * @return
     */
    @Override
    public List<Object> getMultiHash(String key, String... hashKeys)
    {
        return stringRedisTemplate.opsForHash().multiGet(key, Arrays.asList(hashKeys));
    }

    /**
     * 获取redis 所有Hash键值
     *
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> getHashAll(String key)
    {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置redis Hash值
     *
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void setHash(String key, String hashKey, Object value)
    {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 设置批量设置redis Hash值
     *
     * @param key
     * @param maps
     */
    @Override
    public void setMultiHash(String key, Map<Object, Object> maps)
    {
        stringRedisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 是否存在redis Hash键
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Boolean hasHashKey(String key, String hashKey)
    {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 删除Hash键
     *
     * @param key
     * @param hashKeys
     * @return
     */
    @Override
    public Long deleteHashKey(String key, String... hashKeys)
    {
        return stringRedisTemplate.opsForHash().delete(key, Arrays.asList(hashKeys));
    }

    /**
     * 自增长Hash
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Long incrementHash(String key, String hashKey)
    {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, 1L);
    }

    /**
     * 自增长Hash
     *
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    @Override
    public Long incrementHash(String key, String hashKey, Long delta)
    {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 自增长Hash
     *
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    @Override
    public Double incrementHash(String key, String hashKey, Double delta)
    {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeUnit
     * @return
     */
    @Override
    public Long getExpire(String key, TimeUnit timeUnit)
    {
        return stringRedisTemplate.getExpire(key, timeUnit);
    }

//    /**
//     * 使用分布式锁执行方法
//     * @param func
//     * @param key
//     * @param waitTime
//     * @param leaseTime
//     * @param timeUnit
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public <T> T doWithLock(Supplier<T> func, String key, long waitTime, long leaseTime, TimeUnit timeUnit) throws Exception
//    {
//        T t = null;
//        RLock rLock = redissonClient.getLock(key);
//        try
//        {
//            log.info("线程:" + Thread.currentThread().getId() + "尝试获取Redis锁，lockName:" + key);
//            rLock = true ? redissonClient.getFairLock(key) : redissonClient.getLock(key);
//            boolean isLocked = rLock.tryLock(waitTime, leaseTime, timeUnit);
//
//            if (isLocked)
//            {
//                log.info("线程:" + Thread.currentThread().getId() + "成功获得到Redis锁，lockName:" + key);
//                t = func.get();
//            }
//            else
//            {
//                log.info("线程:" + Thread.currentThread().getId() + "没有获得到Redis锁，lockName:" + key);
//                throw new Exception("Redis锁没有获取到，lockName:" + key);
//            }
//        }
//        catch (Exception e)
//        {
//            throw e;
//        }
//        finally
//        {
//            //isHeldByCurrentThread()，加锁后的业务逻辑处理完毕后，会进行“释放锁”操作，在释放的时候，有可能因为业务逻辑的处理时间较长，超过预设的leaseTime，默认5s，锁会被自动释放，这时若执行释放锁的逻辑，会抛一个类似“xx锁不被该线程所持有”的异常
//            if (rLock != null && rLock.isLocked() && rLock.isHeldByCurrentThread())
//            {
//                rLock.unlock();
//                log.info("线程:" + Thread.currentThread().getId() + "成功释放Redis锁，lockName:" + key);
//            }
//        }
//
//        return t;
//    }
}