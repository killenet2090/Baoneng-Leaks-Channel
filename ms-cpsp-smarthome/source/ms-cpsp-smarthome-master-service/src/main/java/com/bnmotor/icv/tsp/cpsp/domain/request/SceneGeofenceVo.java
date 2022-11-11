package com.bnmotor.icv.tsp.cpsp.domain.request;

import lombok.Data;

/**
 * @ClassName: SceneGeofenceVo
 * @Description: 场景关联地理围栏请求实体
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class SceneGeofenceVo {

    private String id;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 场景id
     */
    private String sceneId;
    /**
     * 地理围栏id
     */
    private String geofenceId;
    /**
     * 执行条件：0-进入，1-离开
     */
    private Integer prerequisite;

    /**
     * 1-手动，2-自动
     */
    private Integer isAuto;

    private String uid;
}
