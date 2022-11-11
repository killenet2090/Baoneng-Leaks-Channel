package com.bnmotor.icv.tsp.cpsp.service;

import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;
import com.bnmotor.icv.tsp.cpsp.pojo.input.SceneListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneDetailsOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneExecutionOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneListOutput;

import java.util.List;

/**
 * @ClassName: ISceneService
 * @Description: 场景服务接口设计
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface ISceneService {
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
    SceneListOutput getSceneList(String title,Integer executionStyle,String geofenceId,String vin,Integer current,Integer pageSize);

    /**
     * 查询场景详情信息
     * @param sceneId 场景id
     * @param vin 车架号
     * @return
     */
    SceneDetailsOutput getSceneDetails(String sceneId,String vin);

    /**
     * 场景执行
     * @param sceneIds 场景ids
     * @param vin 车架号
     * @return
     */
    SceneExecutionOutput sceneExecution(List<String> sceneIds,String vin);

    /**
     * 场景关联地理围栏
     * @param vo
     * @return
     */
    void sceneGeofenceBind(SceneGeofenceVo vo);

    /**
     * 场景条件设置保存
     * @param vo
     */
    void sceneSave(SceneGeofenceVo vo);
}
