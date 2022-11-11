package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeSceneDetailsIService;
import com.bnmotor.icv.tsp.cpsp.api.SmartHomeSceneExecutionIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneDetailsInput;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneExecutionInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneDetailsOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneExecutionOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.ExecutionVo;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.SceneVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: SmartHomeBindAccountServiceImpl
* @Description: 供应商场景执行实现类
* @author: jiangchangyuan1
* @date: 2021/2/5
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_SCENE_EXECUTION)
public class SmartHomeSceneExecutionServiceImpl implements SmartHomeSceneExecutionIService, AbstractStrategy<SceneExecutionInput, SceneExecutionOutput> {


    @Override
    public SceneExecutionOutput call(SceneExecutionInput sceneExecutionInput) {
        SceneExecutionOutput output = this.process(sceneExecutionInput);
        return output;
    }

    @Override
    public SceneExecutionOutput process(SceneExecutionInput input) {
        SceneExecutionOutput output = new SceneExecutionOutput();
        output.setVin(input.getVin());
        return output;
    }
}
