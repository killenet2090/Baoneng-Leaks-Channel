package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.EmailRecordPo;

import java.util.List;

/**
 * @ClassName: EmailRecordMapper
 * @Description: 邮件发送记录Dao
 * @author: jiangchangyuan1
 * @date: 2020/11/2
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface EmailRecordMapper extends AdamMapper<EmailRecordPo> {
    /**
     * 查询发送邮件记录列表
     * @param po
     * @return
     */
    List<EmailRecordPo> getEmailRecordList(EmailRecordPo po);
}
