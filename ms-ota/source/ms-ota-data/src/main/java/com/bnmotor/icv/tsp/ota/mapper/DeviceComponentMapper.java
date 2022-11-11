package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: DeviceComponentPo
* @Description: OTA升级硬件设备信息库 dao层
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface DeviceComponentMapper extends BaseMapper<DeviceComponentPo> {

}
