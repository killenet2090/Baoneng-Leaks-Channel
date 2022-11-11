package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassName: CommonUtil
 * @Description:    常用工具类
 * @author: xuxiaochang1
 * @date: 2020/6/29 11:45
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public class CommonUtil {
    private CommonUtil(){}

    private final static String FIELD_SER = "serialVersionUID";

    /**
     * 去掉bean中所有属性为字符串的前后空格
     * @param bean
     * @throws Exception
     */
    public static void beanAttributeValueTrim(Object bean) throws Exception {
        if(bean!=null){
            //获取所有的字段包括public,private,protected,private
            Field[] fields = bean.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                if (f.getType().equals(String.class)) {
                    String key = f.getName();//获取字段名
                    //排除掉所有序列化的字段
                    if(key.equals(FIELD_SER)){
                        continue;
                    }
                    Object value = getFieldValue(bean, key);

                    if (value == null) {
                        continue;
                    }

                    setFieldValue(bean, key, value.toString().trim());
                }
            }
        }
    }

    /**
     * 利用反射通过get方法获取bean中字段fieldName的值
     * @param bean
     * @param fieldName
     * @return
     * @throws Exception
     */
    private static Object getFieldValue(Object bean, String fieldName)
            throws Exception {
        StringBuffer result = new StringBuffer();
        String methodName = result.append("get")
                .append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1)).toString();

        Object rObject = null;
        Method method = null;

        @SuppressWarnings("rawtypes")
        Class[] classArr = new Class[0];
        method = bean.getClass().getMethod(methodName, classArr);
        if(Objects.isNull(method)){
            return null;
        }
        rObject = method.invoke(bean, new Object[0]);

        return rObject;
    }

    /**
     * 利用发射调用bean.set方法将value设置到字段
     * @param bean
     * @param fieldName
     * @param value
     * @throws Exception
     */
    private static void setFieldValue(Object bean, String fieldName, Object value)
            throws Exception {
        StringBuffer result = new StringBuffer();
        String methodName = result.append("set")
                .append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1)).toString();

        /**
         * 利用发射调用bean.set方法将value设置到字段
         */
        Class[] classArr = new Class[1];
        classArr[0] = "java.lang.String".getClass();
        Method method = bean.getClass().getMethod(methodName, classArr);
        if(Objects.nonNull(method)) {
            method.invoke(bean, value);
        }
    }

    /**
     * 移除空格
     * @param obj
     */
    public static void beanAttributeValueTrimPo(Object obj){
        try {
            beanAttributeValueTrim(obj);
        } catch (Exception e) {
            log.error("beanAttributeValueTrim error. e.getMessage={}", e.getMessage(), e);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 返回默认的项目Id
     * @param projectId
     * @return
     */
    public static String buildProjectId(String projectId){
        return StringUtils.isEmpty(projectId) ? CommonConstant.PROJECT_ID_DEFAULT : projectId;
    }

    /**
     * 用CommonConstant.USER_ID_SYSTEM值设置BasePo
     * @param basePo
     * @param isSave
     */
    public static void wrapBasePo(final BasePo basePo, boolean isSave){
        wrapBasePo(basePo, CommonConstant.USER_ID_SYSTEM, LocalDateTime.now(), isSave);
    }

    /**
     * 用外部userId值设置BasePo
     *
     * @param basePo
     * @param userId
     * @param isSave
     */
    public static void wrapBasePo(final BasePo basePo, String userId, boolean isSave){
        wrapBasePo(basePo, userId, LocalDateTime.now(), isSave);
    }

    /**
     * 用外部userId和localDateTime值设置BasePo
     * @param basePo
     * @param userId
     * @param now
     * @param isSave
     */
    public static void wrapBasePo(final BasePo basePo, String userId, LocalDateTime now, boolean isSave){
        if(Objects.nonNull(basePo)){
            userId = Objects.nonNull(userId) ? userId : CommonConstant.USER_ID_SYSTEM;
            if(isSave) {
                basePo.setCreateBy(userId);
                basePo.setCreateTime(now);
                basePo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            }
            basePo.setUpdateBy(userId);
            basePo.setUpdateTime(now);
        }
    }

    /**
     * 补充创建日期
     * @param basePo
     * @param now
     * @param isSave
     */
    public static void wrapBasePo4Date(final BasePo basePo, LocalDateTime now, boolean isSave){
        if(Objects.nonNull(basePo)){
            now = (Objects.isNull(now)) ? LocalDateTime.now() : now;
            if(isSave) {
                basePo.setCreateTime(now);
                basePo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            }
            basePo.setUpdateTime(now);
        }
    }

    /**
     * 包装更新
     * @param basePo
     * @param userId
     */
    public static void wrapBasePo4Update(final BasePo basePo, String userId, LocalDateTime now){
        if(Objects.nonNull(basePo)){
            userId = (Objects.isNull(userId)) ? CommonConstant.USER_ID_SYSTEM: userId;
            basePo.setUpdateBy(userId);

            now = (Objects.isNull(now)) ? LocalDateTime.now() : now;
            basePo.setUpdateTime(now);
        }
    }

    /**
     *
     * @param basePo
     */
    public static void wrapBasePo4Update(final BasePo basePo){
        wrapBasePo4Update(basePo, null, null);
    }


    /*public static void wrapBasePo4Date(final BasePo basePo, LocalDateTime now, boolean isSave){
        if(Objects.nonNull(basePo)){
            now = (Objects.isNull(now)) ? LocalDateTime.now() : now;
            if(isSave) {
                basePo.setCreateTime(now);
            }
            basePo.setUpdateTime(now);
            basePo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
        }
    }*/

    public static void main(String[] args) throws Exception {
    }
}


