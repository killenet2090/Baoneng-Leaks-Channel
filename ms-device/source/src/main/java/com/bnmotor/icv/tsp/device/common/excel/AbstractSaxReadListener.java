package com.bnmotor.icv.tsp.device.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: VehicleSaxListener
 * @Description: sax解析车辆导入文件流
 * @author: zhangwei2
 * @date: 2020/7/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public abstract class AbstractSaxReadListener<T> extends AnalysisEventListener<T> {
    private String sheetName;
    // 数据读取内存缓存
    private final List<T> datas = new ArrayList();
    // 配置sax解析，每批读取多少条目进行一次回调
    private int size = 1000;

    private String taskNo;

    @Override
    public void invoke(T o, AnalysisContext analysisContext) {
        if (o.getClass().isAnnotationPresent(Valid.class)) {
            checkData(o);
        }
        datas.add(o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
        if (datas.size() > size) {
            throw new AdamException(BusinessExceptionEnums.IMPORT_OVER_LIMIT);
        }
    }

    /**
     * 参数检查
     *
     * @return 返回失败原因
     */
    public void checkData(T o) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<T>> set = validator.validate(o);
        if (CollectionUtils.isNotEmpty(set)) {
            ConstraintViolation<T> constraintViolation=set.iterator().next();
            throw new AdamException(RespCode.USER_INVALID_INPUT, constraintViolation.getMessage() + "! sheet:" + sheetName + ", 行数:" + (datas.size() + 1));
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public abstract void processData();

    public abstract void complete();

    public List<T> getDatas() {
        return datas;
    }

    public String getTaskNo() {
        return taskNo;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        complete();
    }
}