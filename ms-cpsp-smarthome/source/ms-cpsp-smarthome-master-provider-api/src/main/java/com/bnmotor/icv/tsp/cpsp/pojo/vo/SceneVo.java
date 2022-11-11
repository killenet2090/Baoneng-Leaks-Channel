package com.bnmotor.icv.tsp.cpsp.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: SceneVo
 * @Description: 场景响应信息实体
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class SceneVo implements Serializable {
    /**
     * 场景id
     */
    private String sceneId;
    /**
     * 场景标题
     */
    private String title;
    /**
     * 场景执行方式：0-仅手动，1-自动
     */
    private Integer executionStyle;
    /**
     * 场景触发条件：0-离开，1-进入
     */
    private Integer prerequisite;
    /**
     * 场景下设备执行流程
     */
    private List<ExecutionVo> executions;
    /**
     * 地理围栏信息
     */
    private GeofenceVo geofence;

}
