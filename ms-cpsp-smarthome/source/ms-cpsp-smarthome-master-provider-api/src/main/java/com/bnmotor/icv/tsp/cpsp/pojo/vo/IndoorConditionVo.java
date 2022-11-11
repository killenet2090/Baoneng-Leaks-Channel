package com.bnmotor.icv.tsp.cpsp.pojo.vo;

import lombok.Data;

/**
 * @ClassName: IndoorCondition
 * @Description: 采集结果信息
 * @author: jiangchangyuan1
 * @date: 2021/3/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class IndoorConditionVo {
    /**
     * 采集项logo图片地址
     */
    private String logoUrl;
    /**
     * 采集项名称
     */
    private String condItem;
    /**
     * 采集项状态描述
     */
    private String condStatus;
}
