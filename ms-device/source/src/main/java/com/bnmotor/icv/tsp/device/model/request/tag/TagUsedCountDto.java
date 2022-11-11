package com.bnmotor.icv.tsp.device.model.request.tag;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: TagCountDto
 * @Description: 标签使用统计传输对象
 * @author: zhangwei2
 * @date: 2020/9/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class TagUsedCountDto {
    /**
     * 新增车辆使用的标签
     */
    private List<Long> addTagsIds;
    /**
     * 删除车辆使用的标签
     */
    private List<Long> deleteTagIds;

    public TagUsedCountDto(List<Long> addTagsIds, List<Long> deleteTagIds) {
        this.addTagsIds=addTagsIds;
        this.deleteTagIds=deleteTagIds;
    }
}
