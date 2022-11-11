package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneListOutput;

/**
 * @ClassName: SmartHomeSceneListIService
 * @Description: 智能家居场景列表查询接口
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SmartHomeSceneListIService {
    /**
     * 智能家居查询场景信息列表
     */
    default SceneListOutput process(SceneListInput input) {
        return null;
    }
}
