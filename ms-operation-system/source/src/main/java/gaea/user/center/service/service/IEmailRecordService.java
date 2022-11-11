package gaea.user.center.service.service;

import gaea.user.center.service.model.entity.EmailRecordPo;

/**
 * @ClassName: IEmailRecordService
 * @Description: 邮件发送记录接口
 * @author: jiangchangyuan1
 * @date: 2020/11/2
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IEmailRecordService {
    /**
     * 保存邮件发送记录
     * @param emailRecordPo
     */
    void insertEmailRecord(EmailRecordPo emailRecordPo);

    /**
     * 定时任务-每五分钟重试一次发送失败的邮件
     */
    void retryEmailSendTask();
}
