package com.bnmotor.icv.tsp.cpsp.pojo.vo;

import lombok.Data;

/**
 * @ClassName: ExecutionVo
 * @Description: 场景下执行流程信息
 * @author: jiangchangyuan1
 * @date: 2021/2/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ExecutionVo {
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
     * 设备所处房间
     */
    private String room;
    /**
     * 设备执行的最终状态
     * 0-关闭，1-开启
     */
    private Integer status;
    /**
     * 设备是否支持远控
     * 0-不支持，1-支持
     */
    private String isControl;
    /**
     * 设备预设执行时间
     */
    private String executionTime;
    /**
     * 执行类型：0-立即执行(默认)，1-定时执行
     */
    private Integer type;
}
