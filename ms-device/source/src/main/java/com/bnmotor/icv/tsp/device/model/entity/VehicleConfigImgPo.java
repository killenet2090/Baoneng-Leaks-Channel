package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: VehicleConfigImgPo
 * @Description: 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_config_img")
public class VehicleConfigImgPo extends BasePo {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置id
     */
    private Long configId;

    /**
     * 图片分类:1-外观；2-内饰;3-空间
     */
    private Integer imgCategory;

    /**
     * 图片类型
     */
    private Integer imgType;

    /**
     * 图片url
     */
    private String imgUrl;

    /**
     * 管理关系
     */
    private Long relationId;

    /**
     * 图片排序
     */
    private Integer imgOrder;

    /**
     * 版本号
     */
    private Integer version;
}
