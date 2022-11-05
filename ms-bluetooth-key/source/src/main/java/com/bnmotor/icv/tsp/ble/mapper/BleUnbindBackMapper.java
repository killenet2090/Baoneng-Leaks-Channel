package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthCompePo;
import com.bnmotor.icv.tsp.ble.model.entity.BleUnbindBackPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TspBleAuthCompe
* @Description: 查询所有权限
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface BleUnbindBackMapper extends BaseMapper<BleUnbindBackPo> {
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
     * @param bleUnbindBackPo
     * @return
     */
   int addBleUnbind(BleUnbindBackPo bleUnbindBackPo);

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
}
