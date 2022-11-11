package com.bnmotor.icv.tsp.vehstatus.aop.aspect;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.vehstatus.aop.annotation.ParamTransfer;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnPack;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.PriorityOrder;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.dto.BasePack;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @ClassName: ParamTransferAspect
 * @Description: ParamTransfer自定义注解
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Aspect
@Component
@Slf4j
public class ParamTransferAspect {

    @Autowired
    private Map<String, List<String>> vehStatusGroupMap;

    private static List<Class<? extends ColumnPack>> columnPackList = new ArrayList<>();


    @Pointcut("@annotation(com.bnmotor.icv.tsp.vehstatus.aop.annotation.ParamTransfer)")
    public void annotationPointcut() {

    }

    @Around("annotationPointcut() && @annotation(paramTransfer)")
    public Object around(ProceedingJoinPoint pjp, ParamTransfer paramTransfer) throws Throwable {
        int columnIdx = paramTransfer.columnsParamIdx();
        int groupIdx = paramTransfer.groupsParamIdx();
        Object[] args = pjp.getArgs();
        Set<String> columnNames = (Set<String>)args[columnIdx];
        Set<String> groupNames = (Set<String>)args[groupIdx];

        if(columnNames != null || groupNames != null) {
            ColumnPack columnPack = null;
            try {
                columnPack = BasePack.class.getConstructor().newInstance();
                for(Class<? extends ColumnPack> pack : columnPackList) {
                    if(!BasePack.class.equals(pack)) {
                        columnPack = pack.getConstructor(ColumnPack.class).newInstance(columnPack);
                        columnPack.setVehStatusGroupMap(vehStatusGroupMap);
                    }
                }
            } catch (Exception e) {
                log.error("参数转换发生异常[{}]", e.getMessage());
                throw new AdamException(RespCode.UNKNOWN_ERROR);
            }
            columnNames = columnPack.packColumn((columnNames == null ? new LinkedHashSet<>() : columnNames), groupNames);
        }
        args[columnIdx] = columnNames;
        Object retVal = pjp.proceed(args);
        return retVal;
    }

    /**
     * 初始化columnPackList
     */
    @PostConstruct
    private void initColumnPackList() {
        //获取ColumnPack所有实现类
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(ColumnPack.class));
        Set<BeanDefinition> components = provider.findCandidateComponents(ColumnPack.class.getPackageName());
        List packClazz = new ArrayList<>();
        components.stream().forEach(component -> {
            try {
                packClazz.add(Class.forName(component.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        columnPackList.addAll(packClazz);
        Collections.sort(columnPackList, new Comparator<Class>() {
            @Override
            public int compare(Class thisObj, Class compareObj) {
                int weightThis = PriorityOrder.class.isAssignableFrom(thisObj) ? 1 : 0;
                int weightCompare = PriorityOrder.class.isAssignableFrom(compareObj) ? 1 : 0;
                return weightThis - weightCompare;
            }
        });
    }
}
