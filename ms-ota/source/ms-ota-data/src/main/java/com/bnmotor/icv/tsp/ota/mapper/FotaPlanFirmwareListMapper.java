/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanFirmwareListPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <pre>
 *   <b>表名</b>：tb_fota_plan_firmware_list
 *   该表用于定义一个OTA升级计划中需要升级哪几个软件
包含：
     1. 依赖的软件清单
                                               -数据操作对象
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Mapper
public interface FotaPlanFirmwareListMapper extends AdamMapper<FotaPlanFirmwareListPo> {
    /**
     * 批量插入升级固件列表
     * @param upgradeFirmwareList
     * @return
     */
    //int insertUpgradeFirmwareList(List<UpgradeFirmwareReq> upgradeFirmwareList);

    /**
     * 根据任务Id删除(历史原因，重新定义)
     * @param planId
     * @return
     */
    int deleteByPlanIdPhysical(Long planId);
}
