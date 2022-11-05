package com.bnmotor.icv.tsp.ble.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * @ClassName: DruidConfiguration
 * @Description: druid数据库连接池配置类
 * @author: zhoulong1
 * @date: 2020/4/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidConfiguration {
    /**
     * 数据源驱动
     */
    private String driverClassName;
    /**
     * 数据库连接URL
     */
    private String url;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 初始化大小
     */
    private Integer initialSize;
    /**
     * 最小连接池数量
     */
    private Integer minIdle;
    /**
     * 	最大连接池数量
     */
    private Integer maxActive;
    /**
     * 	获取连接时最大等待时间，单位毫秒
     */
    private Integer maxWait;
    /**
     * 配置间隔多久才进行一次检测
     */
    private Long timeBetweenEvictionRunsMillis;
    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private Long minEvictableIdleTimeMillis;
    /**
     * 	用来检测连接是否有效的sql
     */
    private String validationQuery;
    /**
     * 空闲连接检测
     */
    private Boolean testWhileIdle;
    /**
     * 申请连接时检测
     */
    private Boolean testOnBorrow;
    /**
     * 归还连接时检测
     */
    private Boolean testOnReturn;

    @Bean
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setTimeBetweenConnectErrorMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        return druidDataSource;
    }
}