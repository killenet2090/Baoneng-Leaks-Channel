package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneDetailsInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneDetailsOutput;

/**
 * @ClassName: SmartHomeSceneDetailsIService
 * @Description: 智能家居场景详情查询接口
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SmartHomeSceneDetailsIService {
    /**
     * 智能家居查询场景详情
     */
    default SceneDetailsOutput process(SceneDetailsInput input) {
        return null;
    }
}
