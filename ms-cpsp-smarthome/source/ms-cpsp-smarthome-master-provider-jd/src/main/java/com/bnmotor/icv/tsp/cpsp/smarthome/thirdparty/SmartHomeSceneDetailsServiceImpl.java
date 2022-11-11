package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeSceneDetailsIService;
import com.bnmotor.icv.tsp.cpsp.api.SmartHomeSceneListIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneDetailsInput;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneDetailsOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneListOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.ExecutionVo;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.SceneVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: SmartHomeBindAccountServiceImpl
* @Description: 供应商场景实现类
* @author: jiangchangyuan1
* @date: 2021/2/5
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_SCENE_DETAILS)
public class SmartHomeSceneDetailsServiceImpl implements SmartHomeSceneDetailsIService, AbstractStrategy<SceneDetailsInput, SceneDetailsOutput> {

    @Override
    public SceneDetailsOutput call(SceneDetailsInput sceneDetailsInput) {
        SceneDetailsOutput output = this.process(sceneDetailsInput);
        return output;
    }

    @Override
    public SceneDetailsOutput process(SceneDetailsInput input) {
        SceneDetailsOutput output = new SceneDetailsOutput();
        SceneVo sceneVo = new SceneVo();
        sceneVo.setExecutionStyle(0);
        sceneVo.setPrerequisite(1);
        sceneVo.setSceneId("2345234");
        sceneVo.setTitle("我是场景标题");
        List<ExecutionVo> executionVoList = new ArrayList<>();
        ExecutionVo executionVo = new ExecutionVo();
        executionVo.setEquipmentId("35432424534");
        executionVo.setExecutionTime("2021-02-26 10:00:00");
        executionVo.setIsControl("1");
        executionVo.setLogo("https://xxx.logo.png");
        executionVo.setName("我是设备名称");
        executionVo.setRoom("主卧");
        executionVo.setStatus(1);
        executionVo.setType(0);
        executionVoList.add(executionVo);
        sceneVo.setExecutions(executionVoList);
        output.setScene(sceneVo);
        output.setVin(input.getVin());
        return output;
    }
}
