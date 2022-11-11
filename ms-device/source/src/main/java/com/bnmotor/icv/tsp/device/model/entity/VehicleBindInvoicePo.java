package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ClassName: VehicleInvoicePo
 * @Description: 机动车销售发票信息 实体类
 * @author huangyun1
 * @since 2020-09-25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_bind_invoice")
public class VehicleBindInvoicePo extends BasePo{

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 多租户id
     */
    private String projectId;

    /**
     * 发票代码/机打代码
     */
    private String invoiceCode;

    /**
     * 发票号码/机打号码
     */
    private String invoiceNum;

    /**
     * 开票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime invoiceDate;

    /**
     * 车架号码
     */
    private String vin;

    /**
     * 发动机号码
     */
    private String engineNo;

    /**
     * 购买方名称
     */
    private String purchaser;

    /**
     * 购买方名称及身份证号码/组织机构代码
     */
    private String purchaserCode;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 版本号
     */
    private Integer version;

}
