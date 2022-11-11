package com.bnmotor.icv.tsp.ota.model.req.device;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: VehTag
 * @Description: 车辆标签
 * @author: zhangwei2
 * @date: 2020/11/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehTagDto {
    /**
     * 标签分类id
     */
    private Long categoryId;
    /**
     * 标签名称
     */
    private String categoryName;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 操作类型：1=新增，2=更新，3=删除
     */
    private Integer action;

    /**
     * 标签id集合
     */
    private List<Long> tagIds;
}
