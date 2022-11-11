package com.bnmotor.icv.tsp.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.sms.model.entity.ChannelRosterPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: ChannelRosterDo
* @Description: 短信渠道发送名单设置表 dao层
 * @author huangyun1
 * @since 2020-07-29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface ChannelRosterMapper extends BaseMapper<ChannelRosterPo> {
    /**
     * 查询黑白名单分页列表
     * @param firstPage
     * @param channelRosterPo
     * @return
     */
    Page queryRosterPageList(IPage firstPage, @Param("channelRosterPo") ChannelRosterPo channelRosterPo);
}
