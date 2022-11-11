package com.bnmotor.icv.tsp.device.model.response.feign;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: ExistVo
 * @Description: 判断是否存在实体
 * @author: zhangwei2
 * @date: 2020/5/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ExistVo {
    /**
     * 是否存在，true存在，false不存在
     */
    @JsonProperty(value = "isExist")
    private boolean exist;

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
