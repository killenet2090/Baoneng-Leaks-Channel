package com.bnmotor.icv.tsp.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.sms.model.entity.ChannelRulePo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: ChannelRuleDo
* @Description: 短信渠道规则表 dao层
 * @author huangyun1
 * @since 2020-07-29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface ChannelRuleMapper extends BaseMapper<ChannelRulePo> {
    /**
     * 查询规则列表
     * @param queryBean
     * @return
     */
    List<ChannelRulePo> queryRuleList(ChannelRulePo queryBean);
}
