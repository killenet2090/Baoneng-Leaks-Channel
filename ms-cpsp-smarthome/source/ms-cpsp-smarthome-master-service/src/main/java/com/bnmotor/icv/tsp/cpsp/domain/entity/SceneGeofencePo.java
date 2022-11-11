package com.bnmotor.icv.tsp.cpsp.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName: SceneGeofencePo
 * @Description: 场景关联地理围栏实体
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@AllArgsConstructor
@Builder
@Data
@ToString
@TableName("tb_cpsp_smarthome_scene_geofence_link")
public class SceneGeofencePo extends BasePo{
    /**
     * 记录ID
     */
    @ApiModelProperty(value = "id", name = "记录ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 场景id
     */
    private String sceneId;
    /**
     * 地理围栏id
     */
    private String geofenceId;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 执行条件：1-到达，2-离开
     */
    private Integer prerequisite;

    /**
     * 1-手动，2-自动
     */
    private Integer isAuto;
}
