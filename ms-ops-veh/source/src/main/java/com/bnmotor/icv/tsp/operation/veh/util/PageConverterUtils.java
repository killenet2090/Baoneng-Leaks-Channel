package com.bnmotor.icv.tsp.operation.veh.util;

import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.tsp.operation.veh.model.request.PageDto;

/**
 * @ClassName: PageConverterUtils
 * @Description: 分页整体切换之前调用该转换类,适配不同的分页对象
 * @author: wuhao1
 * @data: 2020-07-18
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class PageConverterUtils
{

    private PageConverterUtils(){}

    /**
     * <p>适用业务:</p>
     * <pre>MgtVehicleServiceImplMgtVehicleServiceImplMgtVehicleServiceImpl
     * MgtVehicleServiceImpl+listModelVos
     * </pre>
     * @param pageRequest
     * @return
     */
    public static PageDto pageRequestToPageDto(PageRequest pageRequest) {
        PageDto pageDto = new PageDto();
        pageDto.setCurrent(pageRequest.getCurrent());
        pageDto.setPageSize(pageRequest.getPageSize());
        return pageDto;
    }
}
