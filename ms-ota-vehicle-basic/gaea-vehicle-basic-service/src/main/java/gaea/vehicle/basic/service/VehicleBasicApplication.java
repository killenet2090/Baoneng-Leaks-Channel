package gaea.vehicle.basic.service;

import lombok.Data;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Data
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan( basePackages = "gaea.vehicle.basic.service.dao")
public class VehicleBasicApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(VehicleBasicApplication.class,args);
    }
}
