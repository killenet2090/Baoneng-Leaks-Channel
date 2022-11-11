package com.bnmotor.icv.tsp.operation.veh.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName: VehiclePo
 * @Description: 车辆管理列表-内部调用接收对象
 * @author: qiqi1
 * @date: 2020/8/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class VehiclePo implements Serializable {
    private static final long serialVersionUID = -101865439469201184L;

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
     * 激活状态：0-未激活，1-成功，2-失败，3-进行中
     */
    private Integer activationStauts;

    /**
     * 激活时间
     */
    private Date activationDate;

    /**
     * 关闭激活时间
     */
    private Date unactiveDate;

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
}
