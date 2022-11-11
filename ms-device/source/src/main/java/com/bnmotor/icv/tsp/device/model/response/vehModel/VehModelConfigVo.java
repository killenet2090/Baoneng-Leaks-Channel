package com.bnmotor.icv.tsp.device.model.response.vehModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @ClassName: VehModelDetailVo
 * @Description: 车辆型号详情
 * @author: zhangwei2
 * @date: 2020/7/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehModelConfigVo {
    /**
     * 配置id
     */
    private String id;

    /**
     * 年款名称
     */
    private String yearStyleName;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 车辆类型
     */
    @JsonIgnore
    private Integer vehType;

    /**
     * 车辆类型 1-纯电动；2-燃油车
     */
    private String vehTypeValue;
}
