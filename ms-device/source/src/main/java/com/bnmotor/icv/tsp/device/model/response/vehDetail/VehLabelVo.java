package com.bnmotor.icv.tsp.device.model.response.vehDetail;

import lombok.Data;

/**
 * @ClassName: VehLabelVo
 * @Description: 车辆标签信息
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehLabelVo {
    /**
     * 车辆标签id
     */
    private Long labelId;
    /**
     * 车辆标签名称
     */
    private String labelName;
    /**
     * 备注
     */
    private String note;
}
