package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthCompePo;
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
public interface BleAuthCompeMapper extends BaseMapper<BleAuthCompePo> {
    /**
     * 查询所有权限
     * @return 所有权限
     */
    List<BleAuthCompePo> queryBleCompe();

    /**
     * 查询所有权限
     * @return 所有权限
     */
    List<Long> queryBleCompeId();

    /**
     * 查询权限编码
     * @return
     */
    List<Long> queryBleCompeServerId();

    /**
     * 根据权限ID查询权限编码
     * @param list
     * @return
     */
    List<Map> queryBleCompeServerByIds(List list);
}
