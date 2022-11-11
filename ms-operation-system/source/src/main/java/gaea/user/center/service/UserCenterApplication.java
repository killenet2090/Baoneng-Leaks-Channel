package gaea.user.center.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@MapperScan("gaea.user.center.service.mapper")
@EnableFeignClients
public class UserCenterApplication 
{
    public static void main(String[] args )
    {
    	SpringApplication.run(UserCenterApplication.class,args);
    }
}
