package com.bnmotor.icv.tsp.cpsp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.domain.entity.SceneGeofencePo;
import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;
import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.mapper.SceneGeofenceMapper;
import com.bnmotor.icv.tsp.cpsp.mapstuct.SceneGeofenceVoMapper;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneDetailsInput;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneExecutionInput;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneDetailsOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneExecutionOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneListOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.GeofenceVo;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.SceneVo;
import com.bnmotor.icv.tsp.cpsp.service.ISceneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: SceneServiceImpl
 * @Description: 场景服务接口实现类
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service
public class SceneServiceImpl extends ServiceImpl<SceneGeofenceMapper, SceneGeofencePo> implements ISceneService {

    @Autowired
    private CPSPProxy cpspProxy;
    @Autowired
    SceneGeofenceMapper sceneGeofenceMapper;

    /**
     * 查询场景信息列表
     * @param title 场景标题
     * @param executionStyle 执行方式：0-仅手动，1-自动,默认-全部
     * @param geofenceId 地理围栏id
     * @param vin 车架号
     * @param current 当前页数
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public SceneListOutput getSceneList(String title,Integer executionStyle,String geofenceId,String vin,Integer current,Integer pageSize) {
        SceneListInput input = SceneListInput.builder().build();
        if(!StringUtils.isEmpty(title)){
            input.setTitle(title);
        }
        if(null != executionStyle){
            input.setExecutionStyle(executionStyle);
        }
        if(!StringUtils.isEmpty(geofenceId)){
            input.setGeofenceId(geofenceId);
        }
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        if(null != current){
            input.setCurrent(current);
        }
        if(null != pageSize){
            input.setPageSize(pageSize);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_SCENE_LIST);
//        input.setCacheKey(String.format(RedisKey.SMART_HOME_SCENE_LIST, sorting));
//        input.setCacheClass(SceneListOutput.class);
        SceneListOutput output = cpspProxy.call(input);
        //TODO:地理围栏功能开发完毕后需要添加地理围栏信息到响应体
        List<SceneVo> sceneVoList = output.getScenes();
        List<SceneVo> sceneVos = new ArrayList<>();
        for(SceneVo sceneVo : sceneVoList){
            GeofenceVo geofenceVo = new GeofenceVo();
            geofenceVo.setGeofenceId("32452345");
            geofenceVo.setAddress("广东省深圳市宝安区新安街道");
            geofenceVo.setTitle("我是地理围栏标题");
            geofenceVo.setLogo("https://xxxxx");
            geofenceVo.setVin("224328759356");
            geofenceVo.setRadius("500");
            geofenceVo.setLng("28.662558");
            geofenceVo.setLat("115.909546");
            sceneVo.setGeofence(geofenceVo);
            sceneVos.add(sceneVo);
        }
        output.setScenes(sceneVos);
        return output;
    }

    /**
     * 查询场景详情
     * @param sceneId 场景id
     * @param vin 车架号
     * @return
     */
    @Override
    public SceneDetailsOutput getSceneDetails(String sceneId,String vin) {
        SceneDetailsInput input = SceneDetailsInput.builder().build();
        if(!StringUtils.isEmpty(sceneId)){
            input.setSceneId(sceneId);
        }
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_SCENE_DETAILS);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_SCENE_DETAILS_KEY, sceneId));
        input.setCacheClass(SceneDetailsOutput.class);
        SceneDetailsOutput output = cpspProxy.call(input);
        //TODO:地理围栏功能开发完毕后需要添加地理围栏信息到响应体
        SceneVo sceneVo = output.getScene();
        GeofenceVo geofenceVo = new GeofenceVo();
        geofenceVo.setGeofenceId("32452345");
        geofenceVo.setAddress("广东省深圳市宝安区新安街道");
        geofenceVo.setTitle("我是地理围栏标题");
        geofenceVo.setLogo("https://xxxxx");
        geofenceVo.setVin("224328759356");
        geofenceVo.setRadius("500");
        geofenceVo.setLng("28.662558");
        geofenceVo.setLat("115.909546");
        sceneVo.setGeofence(geofenceVo);
        output.setScene(sceneVo);
        return output;
    }

    /**
     * 场景执行
     * @param sceneIds 场景ids
     * @param vin 车架号
     * @return
     */
    @Override
    public SceneExecutionOutput sceneExecution(List<String> sceneIds,String vin) {
        SceneExecutionInput input = SceneExecutionInput.builder().build();
        if(null != sceneIds && sceneIds.size() > 0){
            input.setSceneIds(sceneIds);
        }
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_SCENE_EXECUTION);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_SCENE_EXECUTION_KEY, sceneIds));
        input.setCacheClass(SceneDetailsOutput.class);
        SceneExecutionOutput output = cpspProxy.call(input);
        return output;
    }

    /**
     * 场景关联地理围栏
     * @param vo
     */
    @Override
    public void sceneGeofenceBind(SceneGeofenceVo vo) throws AdamException{
        //判断关系是否已存在
        SceneGeofencePo sceneGeofencePoOld = sceneGeofenceMapper.getSceneGeofence(vo);
        if(null != sceneGeofencePoOld){
            //编辑
            sceneGeofencePoOld.setVin(vo.getVin());
            sceneGeofencePoOld.setGeofenceId(vo.getGeofenceId());
            sceneGeofencePoOld.setPrerequisite(vo.getPrerequisite());
            sceneGeofenceMapper.updateById(sceneGeofencePoOld);
        }else{
            //新增
            SceneGeofencePo po = SceneGeofenceVoMapper.INSTANCE.revertMap(vo);
            sceneGeofenceMapper.insert(po);
        }
    }

    @Override
    public void sceneSave(SceneGeofenceVo vo) {
        log.info("SceneServiceImpl call sceneSave, the vo is {}", JSONObject.toJSONString(vo));
        if (Objects.isNull(vo.getId())) {
            SceneGeofencePo po = SceneGeofencePo.builder().geofenceId(vo.getGeofenceId()).isAuto(vo.getIsAuto())
                    .prerequisite(vo.getPrerequisite()).sceneId(vo.getSceneId()).vin(vo.getVin()).build();
            po.setUpdateBy(vo.getUid());
            po.setCreateBy(vo.getUid());
            sceneGeofenceMapper.insert(po);
        } else {
            sceneGeofenceMapper.updateScene(vo);
        }
    }
}
