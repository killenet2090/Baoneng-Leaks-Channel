package com.bnmotor.icv.tsp.cpsp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.cpsp.domain.entity.SceneGeofencePo;
import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;
import com.bnmotor.icv.tsp.cpsp.mapper.SceneGeofenceMapper;
import com.bnmotor.icv.tsp.cpsp.mapstuct.SceneGeofenceVoMapper;
import com.bnmotor.icv.tsp.cpsp.service.ISceneGeofenceService;
import com.bnmotor.icv.tsp.cpsp.service.ISceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: SceneGeofenceImpl
 * @Description: 场景围栏关联关系实现类
 * @author: jiangchangyuan1
 * @date: 2021/3/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class SceneGeofenceImpl extends ServiceImpl<SceneGeofenceMapper, SceneGeofencePo> implements ISceneGeofenceService {

    @Autowired
    SceneGeofenceMapper sceneGeofenceMapper;

    /**
     * 查询围栏关联的场景列表
     * @param fenceId 围栏ID
     * @param eventType 事件类型：0-进围栏，1-出围栏
     * @return
     */
    @Override
    public List<SceneGeofenceVo> getSceneGeofenceList(String fenceId,Integer eventType) {
        SceneGeofenceVo sceneGeofenceVo = new SceneGeofenceVo();
        sceneGeofenceVo.setGeofenceId(fenceId);
        sceneGeofenceVo.setPrerequisite(eventType);
        List<SceneGeofencePo> sceneGeofencePoList =  sceneGeofenceMapper.getSceneGeofenceList(sceneGeofenceVo);
        List<SceneGeofenceVo> sceneGeofenceVos = SceneGeofenceVoMapper.INSTANCE.map(sceneGeofencePoList);
        return sceneGeofenceVos;
    }
}
