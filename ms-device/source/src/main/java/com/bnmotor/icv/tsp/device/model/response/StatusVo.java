package com.bnmotor.icv.tsp.device.model.response;

import lombok.Data;

/**
 * @ClassName: StatusVo
 * @Description: 状态实体
 * @author: zhangwei2
 * @date: 2020/7/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class  StatusVo {
    /**
     * 状态
     */
    private Integer status;
    /**
     * 描叙
     */
    private String desp;
}
