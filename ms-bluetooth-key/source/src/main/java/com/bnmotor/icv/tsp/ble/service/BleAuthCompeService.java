package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthCompePo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TspBleAuthCompe
 * @Description: 查询权限信息
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface BleAuthCompeService extends IService<BleAuthCompePo> {
    /**
     * 查询所有权限
     * @return
     */
    Map<Long,String> queryAllCompe();

    /**
     * 根据权限ID查询
     * @param compeList
     * @return
     */
    Map<Long,String> queryLimitCompe(List<Long> compeList);

    /**
     * 查询授权
     * @return
     */
    List<Long> queryBleCompeId();

    /**
     * 添加权限
     * @param bleAuthCompePo
     * @return
     */
    int addCompe(BleAuthCompePo bleAuthCompePo);

    /**
     * 更新权限
     * @param bleAuthCompePo
     * @return
     */
    int updateCompe(BleAuthCompePo bleAuthCompePo);

    /**
     * 删除某个权限记录
     * @param bleAuthCompePoList
     * @return
     */
    int delCompe(List<BleAuthCompePo> bleAuthCompePoList);

}
