package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @ClassName: MyValidationUtil
 * @Description:    参数校验工具
 * @author: xuxiaochang1
 * @date: 2020/7/16 16:43
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyValidationUtil {
    private MyValidationUtil(){}

    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 参数校验工具
     * @param obj
     * @param <T>
     * @throws AdamException
     */
    public static <T> void validate(T obj){
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            throw new AdamException(OTARespCodeEnum.PARAMETER_VALIDATION_ERROR.getCode(), String.format("parameter validate failed:%s", constraintViolations.iterator().next().getMessage()));
        }
    }
}
