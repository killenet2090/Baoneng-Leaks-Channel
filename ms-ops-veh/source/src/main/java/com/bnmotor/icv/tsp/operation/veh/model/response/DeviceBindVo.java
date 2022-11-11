package com.bnmotor.icv.tsp.operation.veh.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zhoulong1
 * @ClassName: DeviceBindVo
 * @Description: 设备绑定信息返回对象
 * @since: 2020/7/17
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
    private Date bindTime;
    /**
     * 解绑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date unbindTime;
    /**
     * 操作人
     */
    private String operateBy;
}
