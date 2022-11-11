package gaea.vehicle.basic.service.models.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

/**
 * <pre>
 *  提供抽象Api基础功能
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
@RequestMapping("api")
public abstract class BaseApi {
    /**
     * <pre>
     *  资源对象
     * </pre>
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * <pre>
     * 根据编码获取资源信息
     * </pre>
     *
     * @param code 编码
     * @return 资源对象
     */
    protected Msg getMsg(String code) {
        return this.getMsg(code, new Object[]{});
    }

    /**
     * <pre>
     *  根据参数获取资源信息.
     *  code 是properties的key。
     *  obj 是配置文件value的占位符,如: 0001=error{0},{1}
     * </pre>
     *
     * @param code 异常码
     * @param obj  占位符
     * @return 资源对象
     */
    protected Msg getMsg(String code, Object... obj) {
        Locale locale = LocaleContextHolder.getLocale();
        return new Msg(code, messageSource.getMessage(code, obj, locale));
    }

}
