package com.bnmotor.icv.tsp.cpsp.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.cpsp.domain.entity.SceneGeofencePo;
import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.SceneVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SceneGeofenceMapper
 * @Description: 场景关联地理围栏Dao
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SceneGeofenceMapper extends AdamMapper<SceneGeofencePo> {
    /**
     * 查询场景是否已关联地理围栏
     * @param vo 场景id
     * @return
     */
    SceneGeofencePo getSceneGeofence(@Param("vo") SceneGeofenceVo vo);

    /**
     * 查询符合条件的所有场景列表
     * @param sceneGeofenceVo
     * @return
     */
    List<SceneGeofencePo> getSceneGeofenceList(@Param("vo") SceneGeofenceVo sceneGeofenceVo);


    /**
     * 更新场景条件设置
     * @param vo
     */
    void updateScene(@Param("dto") SceneGeofenceVo vo);
}
