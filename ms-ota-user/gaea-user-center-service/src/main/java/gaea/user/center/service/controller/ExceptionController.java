package gaea.user.center.service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import gaea.user.center.service.exception.BusinessException;


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
