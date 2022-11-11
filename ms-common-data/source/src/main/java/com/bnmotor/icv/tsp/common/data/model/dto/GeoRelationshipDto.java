package com.bnmotor.icv.tsp.common.data.model.dto;

import lombok.Data;

/**
 * @ClassName: GeoDo
 * @Description: 地理位置关联关系表 实体类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class GeoRelationshipDto {

    /**
     * 祖先节点
     */
    private Long ancestor;
    /**
     * 后代节点
     */
    private Long descendant;

    private Integer distance;

    /**
     * distance比较逻辑，通过 CompareEnum 来赋值
     */
    private String distanceCompare;

}
