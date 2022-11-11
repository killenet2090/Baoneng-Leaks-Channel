package com.bnmotor.icv.tsp.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.sms.model.entity.SmsInfoPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: MsgInfoMapper
 * @Description: 消息mapper
 * @author: hyun
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface SmsInfoMapper extends BaseMapper<SmsInfoPo> {
}
