package com.bnmotor.icv.tsp.ota.aop.aspect;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 2020-09-07 移除掉框架注入的aop (controllerAspect)
 * 
 * @ClassName: CustomBeanDefinitionRegistryPostProcessor
 * @Description: 绕过aspect
 * @author: zhangwei2
 * @date: 2020/6/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private List<String> specialOverrideBeanList = Lists.newArrayList("controllerAspect", "otaMessageHandler", "redisLockAspect");

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for(String beanName : specialOverrideBeanList){
            boolean isContainsSpecialBean = ((DefaultListableBeanFactory) registry).containsBean(beanName);
            if (isContainsSpecialBean) {
                AnnotatedBeanDefinition aspectBeanDefinition = (AnnotatedBeanDefinition) registry.getBeanDefinition(beanName);
                if (aspectBeanDefinition != null) {
                    AnnotatedGenericBeanDefinition customBeanDefinition = new AnnotatedGenericBeanDefinition(aspectBeanDefinition.getMetadata());
                    //设置自定义的bean class
                    try {
                        log.info("remove bean.beanName={}", beanName);
                        customBeanDefinition.resolveBeanClass(Thread.currentThread().getContextClassLoader());
                        registry.registerBeanDefinition(beanName, customBeanDefinition);
                        registry.removeBeanDefinition(beanName);
                    } catch (ClassNotFoundException e) {
                        log.error("移除组件类异常", e);
                    }
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
