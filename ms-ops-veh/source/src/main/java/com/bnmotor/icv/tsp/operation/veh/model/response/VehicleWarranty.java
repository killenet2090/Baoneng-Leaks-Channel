package com.bnmotor.icv.tsp.operation.veh.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zhoulong1
 * @ClassName: VehicleWarranty
 * @Description: 车辆保修信息
 * @since: 2020/7/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleWarranty {
    /**
     *车辆保修开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date warrantyBeginDate=new Date();
    /**
     * 保修年限
     */
    private Integer warrantyYears=4;
    /**
     * 保修最大里程
     */
    private Integer warrantMaxMile=100000;
}
