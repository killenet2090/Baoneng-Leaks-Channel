package com.bnmotor.icv.tsp.device.model.response.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: TagVo
 * @Description: 车辆信息管理-车辆属性（标签组）VO
 * @author: qiqi1
 * @date: 2020/8/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class TagVo implements Serializable {
    private static final long serialVersionUID = 4711661078523432334L;
    /**
     * 分类id
     */
    private String categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标签名称
     */
    @JsonProperty("id")
    private String tagId;

    /**
     * 标签名称
     */
    private String tagName;
}
