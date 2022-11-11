package com.bnmotor.icv.tsp.common.data.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoTypePo;
import org.springframework.stereotype.Component;

/**
 * @ClassName: GeoTypeDo
* @Description: 地理位置区域类型 dao层
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public interface GeoTypeMapper extends AdamMapper<GeoTypePo> {

}
