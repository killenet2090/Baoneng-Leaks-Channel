package com.bnmotor.icv.tsp.ota.config;

import com.bnmotor.icv.adam.spring.cloud.AdamSpringCloudAutoConfiguration;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author xiajiankang1
 */
@Slf4j
@EnableSwaggerBootstrapUI
public class Swagger2Config extends AdamSpringCloudAutoConfiguration implements WebMvcConfigurer
{

	/**
	 *
	 * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
	 *
	 * @return Docket
	 */
	@Bean
	public Docket createRestApi()
	{
		//添加header参数
		ParameterBuilder ticketPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		ticketPar.name("userId").description("user id")
				.modelRef(new ModelRef("string")).parameterType("header")
				.required(false).build(); //header中的ticket参数非必填，传空也可以
		pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.any())
				//此包路径下的类，才生成接口文档
				.apis(RequestHandlerSelectors.basePackage("com.bnmotor.icv.tsp.ota"))
				//加了ApiOperation注解的类，才生成接口文档
				//不显示错误的接口地址1
				.paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
				.paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
				//.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(pars)
				.securitySchemes(Collections.singletonList(securityScheme()));
				//.globalOperationParameters(setHeaderToken());
	}

	/***
	 * oauth2配置
	 * 需要增加swagger授权回调地址
	 * http://localhost:8888/webjars/springfox-swagger-ui/o2c.html
	 * @return
	 */
	@Bean
    SecurityScheme securityScheme()
	{
		return new ApiKey(CommonConstant.X_ACCESS_TOKEN, CommonConstant.X_ACCESS_TOKEN, "header");
	}
	/**
	 * JWT token
	 * @return
	 */
	private List<Parameter> setHeaderToken()
	{
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name(CommonConstant.X_ACCESS_TOKEN).description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

	/**
	 * api文档的详细信息函数,注意这里的注解引用的是哪个
	 *
	 * @return
	 */
	private ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()
				// //大标题
				.title("OTA 后台服务API接口文档")
				// 版本号
				.version("1.0")
//				.termsOfServiceUrl("NO terms of service")
				// 描述
				.description("后台API接口")
				// 作者
				.contact(new Contact("智能车联网团队", "", ""))
                .license("The Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.build();
	}

}
