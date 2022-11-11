package com.bnmotor.icv.tsp.device.model.response.vehDetail;

import com.bnmotor.icv.adam.core.view.BaseView;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: DeviceBindVo
 * @Description: 车辆绑定信息
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class DeviceBindVo {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 绑定时间
     */
    @JsonView(BaseView.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date bindTime;
    /**
     * 解绑时间
     */
    @JsonView(BaseView.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date unbindTime;
    /**
     * 操作人
     */
    private String operateBy;
}
