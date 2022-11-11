package com.bnmotor.icv.tsp.sms.component;

import com.bnmotor.icv.tsp.sms.common.Constant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName: ApplicationContextUtil
 * @Description: 应用上下文util
 * @author: huangyun1
 * @date: 2020/7/31
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class ApplicationContextComp implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextComp.applicationContext = applicationContext;
    }

    /**
     * 根据类型获取bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBeanOfType(Class<T> clazz) {
        Map<String,T> beans = applicationContext.getBeansOfType(clazz);
        // 获取一个类的非限定名称
        String name = clazz.getName();
        if (name.lastIndexOf(Constant.DOT_CHAR) > 0) {
            name = name.substring(name.lastIndexOf(Constant.DOT_CHAR) + 1);
        }
        final String clazzName = name;
        AtomicReference<T> returnBean = new AtomicReference<>();
        beans.entrySet().forEach(bean -> {
            if(clazzName.equalsIgnoreCase(bean.getKey())) {
                returnBean.set(bean.getValue());
            }
        });
        return returnBean.get();
    }
}
