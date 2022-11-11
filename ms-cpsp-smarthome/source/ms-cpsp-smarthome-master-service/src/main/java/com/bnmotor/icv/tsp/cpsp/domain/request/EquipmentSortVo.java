package com.bnmotor.icv.tsp.cpsp.domain.request;

import lombok.Data;

import java.security.KeyStore;
import java.util.List;

/**
 * @ClassName: EquipmentSortVo
 * @Description: 设备卡片排序参数实体
 * @author: jiangchangyuan1
 * @date: 2021/3/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class EquipmentSortVo {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 设备ids
     */
    private List<String> equipmentIds;
}
