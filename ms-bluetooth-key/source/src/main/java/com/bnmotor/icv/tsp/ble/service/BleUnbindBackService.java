package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleUnbindBackPo;

import java.util.List;

/**
 * @ClassName: TspBleAuthCompe
 * @Description: 查询权限信息
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface BleUnbindBackService extends IService<BleUnbindBackPo> {
    /**
     * 查询
     * @param bleUnbindBackPo
     * @return
     */
    BleUnbindBackPo queryUnbindBack(BleUnbindBackPo bleUnbindBackPo);

    /**
     * 查询多条
     * @param bleUnbindBackPo
     * @return
     */
    List<BleUnbindBackPo> queryUnbindByStausList(BleUnbindBackPo bleUnbindBackPo);

    /**
     * 添加
     * @param bleAckPushPo
     * @return
     */
    void addBleUnbind(BleAckPushPo bleAckPushPo);

    /**
     * 更新
     * @param bleUnbindBackPo
     * @return
     */
    int updateBleUnbind(BleUnbindBackPo bleUnbindBackPo);

    /**
     * 删除
     * @param bleUnbindBackPo
     * @return
     */
    int delBleUnbind(BleUnbindBackPo bleUnbindBackPo);


    /**
     * XXL JOb 定时调度回调失败的数据，再次回调成功就删除
     * @param bleUnbindBackPo
     */
    void syncCallSuccessAndDel(BleUnbindBackPo bleUnbindBackPo);
}
