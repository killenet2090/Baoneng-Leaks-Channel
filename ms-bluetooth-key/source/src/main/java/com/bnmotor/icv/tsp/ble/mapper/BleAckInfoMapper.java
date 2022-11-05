package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckInfoPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: TspBleAckInfo
* @Description:  dao层
 * @author shuqi1
 * @since 2020-09-16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface BleAckInfoMapper extends BaseMapper<BleAckInfoPo> {
    /**
     * 根据条件查询
     * @param bleAckInfoPo
     * @return
     */
    BleAckInfoPo queryBleAckInfo(BleAckInfoPo bleAckInfoPo);

    /**
     * 根据条件查询多条记录
     * @param bleAckInfoPo
     * @return
     */
    List<BleAckInfoPo> queryBleAckInfoList(BleAckInfoPo bleAckInfoPo);
    /**
     * 根据条件查询多条记录
     * @return
     */
    List<BleAckInfoPo> queryDelBleAckInfoList();

    /**
     * 根据条件查询多条记录
     * @return
     */
    List<BleAckInfoPo> queryActiveBleAckInfoList();

    /**
     * 添加记录
     * @param bleAckInfoPo
     * @return
     */
    int addBleAckInfo(BleAckInfoPo bleAckInfoPo);

    /**
     * 删除记录
     * @param bleAckInfoPo
     * @return
     */
    int deleteBleAckInfo(BleAckInfoPo bleAckInfoPo);

    /**
     * 按条件移动记录到历史表
     * @param bleAckInfoPo
     * @return
     */
    int moveBleAckInfoToHis(BleAckInfoPo bleAckInfoPo);

    /**
     * 更新记录
     * @param bleAckInfoPo
     * @return
     */
    int updateBleAckInfo(BleAckInfoPo bleAckInfoPo);


    /**
     * 移动tbox上报历史数据
     * @param bleAckInfoPo
     * @return
     */
    int moveAckInfoData(BleAckInfoPo bleAckInfoPo);
}
