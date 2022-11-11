package com.bnmotor.icv.tsp.common.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.common.data.model.entity.GeoTypePo;
import com.bnmotor.icv.tsp.common.data.mapper.GeoTypeMapper;
import com.bnmotor.icv.tsp.common.data.service.IGeoTypeService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: GeoTypeDo
 * @Description: 地理位置区域类型 服务实现类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class GeoTypeServiceImpl extends ServiceImpl<GeoTypeMapper, GeoTypePo> implements IGeoTypeService {

}
