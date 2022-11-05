package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleUserTypePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: TspBleuthPoMapper
 * @Description: 蓝牙钥匙申请业务，用mybatis访问数据库的mapper文件
 * @author: shuqi1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface BleUserTypeMapper extends BaseMapper<BleUserTypePo> {

}
