package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: VehicleLicensePo
 * @Description: 行驶证信息 实体类
 * @author huangyun1
 * @since 2020-09-25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_license")
public class VehicleLicensePo extends BasePo{
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 多租户id
     */
    private String projectId;

    /**
     * 车辆识别代号 VIN
     */
    private String vin;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 所有人
     */
    private String userName;

    /**
     * 发动机号
     */
    private String engineNo;

    /**
     * 注册日期
     */
    private String registerDate;

    /**
     * 发证日期
     */
    private String issueDate;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 版本号
     */
    private Integer version;
}
