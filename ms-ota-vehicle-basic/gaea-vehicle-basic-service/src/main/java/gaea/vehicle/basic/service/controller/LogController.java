package gaea.vehicle.basic.service.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="日志测试",tags={"日志测试"})
@Slf4j
@RestController
@RequestMapping("/pub")
public class LogController {

    @ApiOperation("日志测试")
    @GetMapping("/getLog")
    public String getLog(){
        log.debug("This is a debug message");
        log.info("This is an info message");
        log.warn("This is a warn message");
        log.error("This is an error message");
        log.info("This is an asyn---- info message");

        return "log....";
    }

}