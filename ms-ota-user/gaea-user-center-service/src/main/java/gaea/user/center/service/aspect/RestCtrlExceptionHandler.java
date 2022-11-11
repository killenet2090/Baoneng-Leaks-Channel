package gaea.user.center.service.aspect;

import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.models.response.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xnio.Result;

/**
 * @ClassName: RestCtrlExceptionHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: jiankang
 * @date: 2020/4/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@RestControllerAdvice
public class RestCtrlExceptionHandler {

    @ExceptionHandler(AdamException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Resp<Object> handleXCloudException(AdamException e) {
        if (e!=null){
            log.error("System Business Error : code -  {} , message - {}",e.getCode(), e.getMessage());
        }
        return new Resp<>().setErrMsg(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Resp<Object> handleException(Exception e) {

        String errorMsg = "Exception";
        if (e!=null){
            errorMsg = e.getMessage();
            log.error(e.toString());
        }
        return new Resp<>().setErrMsg("500", errorMsg);
    }
}
