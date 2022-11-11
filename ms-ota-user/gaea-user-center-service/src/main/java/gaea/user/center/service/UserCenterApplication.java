package gaea.user.center.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("gaea.user.center.service.dao")
public class UserCenterApplication 
{
    public static void main(String[] args )
    {
    	SpringApplication.run(UserCenterApplication.class,args);
    }
}
