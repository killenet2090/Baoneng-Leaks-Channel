package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qiqi1
 * @ClassName: VehiclePo
 * @Description: 车辆信息 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle")
public class VehiclePo extends BasePo {
    /**
     * id
     */
    private Long id;

    /**
     * 项目代码
     */
    private String projectId;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 所属组织关系id
     */
    private Long orgId;

    /**
     * 品牌
     */
    private Long brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 车系
     */
    private Long vehSeriesId;

    /**
     * 车型名称
     */
    private String vehSeriesName;

    /**
     * 车型
     */
    private Long vehModelId;

    /**
     * 车型名称
     */
    private String vehModelName;

    /**
     * 年款
     */
    private Long yearStyle;

    /**
     * 年款名称
     */
    private String yearStyleName;

    /**
     * 配置
     */
    private Long configurationId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 颜色
     */
    private Long colorId;

    /**
     * 车辆状态： 1-已创建， 2-已销售，3-已报废
     */
    private Integer vehStatus;

    /**
     * 工作模式：0 - 工厂模式；1 - 展车模式；2 - 销售模式；3 - 用户模式；4 - 报废模式
     */
    private Integer vehLifecircle;

    /**
     * mno认证状态 0-未认证，1-已认证
     */
    private Integer certificationStatus;

    /**
     * 认证日期
     */
    private LocalDateTime certificateDate;

    /**
     * 绑定状态 0-未绑定；1-已绑定 3-绑定中 4-解绑中
     */
    private Integer bindStatus;

    /**
     * 绑定日期
     */
    private LocalDateTime bindDate;

    /**
     * 质检状态
     */
    private Integer qualityInspectStatus;

    /**
     * 激活状态：0-未激活，1-成功，2-失败，3-进行中
     */
    private Integer activationStatus;

    /**
     * 激活时间
     */
    private LocalDateTime activationDate;

    /**
     * 关闭激活时间
     */
    private LocalDateTime unactiveDate;

    /**
     * 发动机号
     */
    private String engineNo;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 车辆类型: 1-燃油；2-纯电动；3-混动车
     */
    private Integer vehType;

    /**
     * 车牌号
     */
    private String drivingLicPlate;

    /**
     * 生产商
     */
    private String manufacturer;

    /**
     * 生产时间
     */
    private LocalDateTime productTime;

    /**
     * 下线日期
     */
    private LocalDateTime downlineDate;

    /**
     * 出厂日期
     */
    private LocalDateTime outFactoryTime;

    /**
     * 发布日期
     */
    private LocalDateTime publishDate;

    /**
     * 保修年限
     */
    private Integer warrantyYear;

    /**
     * 保修最大里程, w公里
     */
    private Integer warrantyMaxMileage;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 原绑定状态
     */
    @JsonIgnore
    @TableField(exist = false)
    private Integer oldBindStatus;

    /**
     * old mno认证状态 0-未认证，1-已认证
     */
    @JsonIgnore
    @TableField(exist = false)
    private Integer oldCertificationStatus;
}
