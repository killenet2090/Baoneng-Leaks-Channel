package com.bnmotor.icv.tsp.cpsp.pojo.vo;

import lombok.Data;

/**
 * @ClassName: EquipmentVo
 * @Description: 智能家居设备实体
 * @author: jiangchangyuan1
 * @date: 2021/2/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class EquipmentVo {
    /**
     * 设备logo
     */
    private String logo;
    /**
     * 设备id
     */
    private String equipmentId;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备房间名称
     */
    private String room;
    /**
     * 设备状态：0-离线，1-开启，2-关闭
     */
    private Integer status;
    /**
     * 是否远控：0-否，1-是
     */
    private Integer isControl;
    /**
     * 设备卡片展示的默认关键字段
     * 如空调温度等
     */
    private String remark;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 设备详情页H5访问路径
     */
    private String detailUrl;
}
