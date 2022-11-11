package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneExecutionInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneExecutionOutput;

/**
 * @ClassName: SmartHomeSceneExecutionIService
 * @Description: 智能家居场景执行接口
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SmartHomeSceneExecutionIService {
    /**
     * 智能家居执行场景
     */
    default SceneExecutionOutput process(SceneExecutionInput input) {
        return null;
    }
}
