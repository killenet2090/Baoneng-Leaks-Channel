package com.bnmotor.icv.tsp.operation.veh.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhoulong1
 * @ClassName: VehicleSalesVo
 * @Description: 车辆销售信息
 * @since: 2020/7/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleSalesVo {
    /**
     * 购买领域
     */
    private String purchaseArea="深圳市";
    /**
     * 购买方名称
     */
    private String purchaseName="智能网联研究院";
    /**
     * 销货单位名称
     */
    private String salesName="远望汽车园区";
    /**
     * 销货单位地址
     */
    private String salesAddress="深圳市龙华区宝能科技园";
    /**
     * 发票号码
     */
    private String invoiceNumber="3454534";
    /**
     * 发票代码
     */
    private String invocieCode="324234";
    /**
     * 身份证号码
     */
    private String idNumber="441564198905189087";
    /**
     * 开票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date  invocieDate=new Date();
    /**
     * 价税合计
     */
    private BigDecimal totalPriceAndTax;
    /**
     * 是否三包
     */
    private boolean threeGuarantees=true;
    /**
     * 经销商编码
     */
    private String distributorCode="890900";


}
