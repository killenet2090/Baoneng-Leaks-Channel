package com.bnmotor.icv.tsp.cpsp.service;

import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;

import java.util.List;

/**
 * @ClassName: SceneGeofenceService
 * @Description: 场景围栏绑定关系接口
 * @author: jiangchangyuan1
 * @date: 2021/3/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface ISceneGeofenceService {
    /**
     * 根据围栏及类型查询绑定场景IDs
     * @param fenceId 围栏id
     * @param eventType 事件类型：0-进围栏，1-出围栏
     * @return
     */
    List<SceneGeofenceVo> getSceneGeofenceList(String fenceId, Integer eventType);
}
