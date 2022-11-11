package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value="DateReportVo对象", description="日期报告")
public class DateReportVo {
    private String date;
    private int status;
    private int count;
}
