package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.ReqFromAppPo;
import com.bnmotor.icv.tsp.ota.mapper.ReqFromAppMapper;
import com.bnmotor.icv.tsp.ota.service.IReqFromAppDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ReqFromAppPo
 * @Description: 存储来自APP的请求 服务实现类
 * @author xuxiaochang1
 * @since 2020-09-16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class ReqFromAppDbServiceImpl extends ServiceImpl<ReqFromAppMapper, ReqFromAppPo> implements IReqFromAppDbService {

}
