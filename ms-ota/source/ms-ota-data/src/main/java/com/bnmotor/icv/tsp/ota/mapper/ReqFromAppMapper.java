package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.tsp.ota.model.entity.ReqFromAppPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ReqFromAppPo
* @Description: 存储来自APP的请求 dao层
 * @author xuxiaochang1
 * @since 2020-12-21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface ReqFromAppMapper extends BaseMapper<ReqFromAppPo> {

}
