package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneListOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SceneListInput
 * @Description: 场景列表请求体
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneListInput extends CPSPInput<SceneListOutput> {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 场景标题
     */
    private String title;
    /**
     * 场景执行方式：0-仅手动，1-自动,默认-全部
     */
    private Integer executionStyle;
    /**
     * 地理围栏id
     */
    private String geofenceId;
    /**
     * 第几页
     */
    private Integer current;
    /**
     * 每页条数
     */
    private Integer pageSize;

}
