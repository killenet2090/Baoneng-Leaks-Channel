package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeSceneListIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneListOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.ExecutionVo;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.SceneVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: SmartHomeBindAccountServiceImpl
* @Description: 供应商设备控制实现类
* @author: jiangchangyuan1
* @date: 2021/2/5
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_SCENE_LIST)
public class SmartHomeSceneListServiceImpl implements SmartHomeSceneListIService, AbstractStrategy<SceneListInput, SceneListOutput> {

    @Override
    public SceneListOutput call(SceneListInput input) {
        SceneListOutput output = this.process(input);
        return output;
    }

    @Override
    public SceneListOutput process(SceneListInput input) {
        SceneListOutput output = new SceneListOutput();
        List<SceneVo> sceneVoList = new ArrayList<>();
        SceneVo sceneVo = new SceneVo();
        sceneVo.setExecutionStyle(0);
        sceneVo.setPrerequisite(1);
        sceneVo.setSceneId("2345234");
        sceneVo.setTitle("我是场景标题");
        List<ExecutionVo> executions = new ArrayList<>();
        ExecutionVo executionVo = new ExecutionVo();
        executionVo.setEquipmentId("35432424534");
        executionVo.setExecutionTime("2021-02-26 10:00:00");
        executionVo.setIsControl("1");
        executionVo.setLogo("https://xxx.logo.png");
        executionVo.setName("我是设备名称");
        executionVo.setRoom("主卧");
        executionVo.setStatus(1);
        executionVo.setType(0);
        executions.add(executionVo);
        sceneVo.setExecutions(executions);
        sceneVoList.add(sceneVo);
        output.setScenes(sceneVoList);
        output.setVin(input.getVin());
        return output;
    }
}
