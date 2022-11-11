package com.bnmotor.icv.tsp.sms.component;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @ClassName: EnvironmentUtil
 * @Description: 环境上下文util
 * @author: huangyun1
 * @date: 2020/7/31
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class EnvironmentComp implements EnvironmentAware {

    private Environment environmentComp;

    /**
     * 获取配置项
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProperty(String name, Class<T> clazz) {
        return environmentComp.getProperty(name, clazz);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environmentComp = environment;
    }
}
