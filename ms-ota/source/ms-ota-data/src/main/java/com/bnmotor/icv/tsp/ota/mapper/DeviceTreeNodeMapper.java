package com.bnmotor.icv.tsp.ota.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: DeviceTreeNodePo
* @Description: 设备分类树
该信息从车辆数据库中同步过来 dao层
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface DeviceTreeNodeMapper extends BaseMapper<DeviceTreeNodePo> {

}
