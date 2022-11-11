package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneDetailsOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneExecutionOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: SceneListInput
 * @Description: 场景执行请求实体
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneExecutionInput extends CPSPInput<SceneExecutionOutput> {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 场景id
     */
    private List<String> sceneIds;

}
