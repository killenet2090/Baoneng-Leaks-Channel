package com.bnmotor.icv.tsp.device.model.response.vehicle;

import lombok.Data;

import java.util.Set;

/**
 * @ClassName: ProgressVo
 * @Description: 导入进度
 * @author: zhangwei2
 * @date: 2020/11/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ProgressVo {
    /**
     * 总条目
     */
    private Integer total = 0;
    /**
     * 当前进度
     */
    private Integer current = 0;
    /**
     * 成功条目
     */
    private Integer successed = 0;
    /**
     * 失败条目
     */
    private Integer failed = 0;

    /**
     * 本次导入的所有车
     */
    private Set<String> vins;

    public void addCurrent() {
        current++;
    }
}
