package gaea.vehicle.basic.service.controller;

import java.util.HashMap;
import java.util.Map;

import gaea.vehicle.basic.service.exception.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionController {
	
    @ExceptionHandler(value = BusinessException.class)
    public Map BusinessExceptionHandler(BusinessException ex) {
        Map map = new HashMap();
        map.put("code", ex.getCode());
        map.put("message", ex.getMsg());
        return map;
    }
}
