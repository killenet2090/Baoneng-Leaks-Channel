package com.bnmotor.icv.tsp.cpsp.domain.request;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: SceneGeofenceVo
 * @Description: 场景关联地理围栏请求实体
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class RemoteVehInfoVo {

    /**
     * 车辆识别号
     */
    private String vin;

    /**
     * 请求来源（5-第三方合作平台）
     */
    private int reqSource;

    /**
     * 查询字段名称列表
     */
    private List<String> columnNames;

    /**
     * 查询组名称列表
     */
    private List<String> groupNames;

}
