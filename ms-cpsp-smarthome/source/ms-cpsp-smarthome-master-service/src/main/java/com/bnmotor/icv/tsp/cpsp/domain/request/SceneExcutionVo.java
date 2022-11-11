package com.bnmotor.icv.tsp.cpsp.domain.request;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: SceneExcutionVo
 * @Description: 场景执行参数实体
 * @author: jiangchangyuan1
 * @date: 2021/3/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class SceneExcutionVo {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 场景ids
     */
    private List<String> sceneIds;
}
