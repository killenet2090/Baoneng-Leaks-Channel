package com.bnmotor.icv.tsp.ble.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//注解开启 swagger2 功能
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    Environment environment;
    //是否开启swagger，正式环境一般是需要关闭的
    private static final String SPLITOR = ";";
    /**
     * 切割扫描的包生成Predicate<RequestHandler>
     * @param basePackage
     * @return
     */
    public static Predicate<RequestHandler> scanBasePackage(final String basePackage) {
        if(StringUtils.isBlank(basePackage)){
            throw new NullPointerException("basePackage不能为空，多个包扫描使用"+SPLITOR+"分隔");
        }
        String[] controllerPack = basePackage.split(SPLITOR);
        Predicate<RequestHandler> predicate = null;
        for (int i = controllerPack.length -1; i >= 0 ; i--) {
            String strBasePackage = controllerPack[i];
            if(StringUtils.isNotBlank(strBasePackage)){
                Predicate<RequestHandler> tempPredicate = RequestHandlerSelectors.basePackage(strBasePackage);
                predicate = predicate == null ? tempPredicate : Predicates.or(tempPredicate,predicate);
            }
        }
        if(predicate == null){
            throw new NullPointerException("basePackage配置不正确，多个包扫描使用"+SPLITOR+"分隔");
        }
        return predicate;
    }
    /**
     * 自定义Docket
     * groupName需配置，否则会报java.lang.IllegalStateException: Multiple Dockets with the same group name are not supported. The following duplicate groups were discovered
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket customDocket(){
        Profiles profiles = Profiles.of("dev","test");
        boolean isEable = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(isEable)
                .groupName( "demo-custom")
                .apiInfo(apiInfo())
                .select()
                .apis(scanBasePackage("com.bnmotor.icv.tsp.ble.controller"
                        +SPLITOR+"com.bnmotor.icv.tsp.ble.controller" ))
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("内容：接口文档")
                .description("描述：微信公众号开发")
                .contact(new Contact("蓝牙钥匙开发者", "http://127.0.0.1:8080", "shuqi1@bngrp.com"))
                .version("版本号:" + 1.0)
                .build();
    }

}
