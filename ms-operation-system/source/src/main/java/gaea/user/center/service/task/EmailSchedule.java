package gaea.user.center.service.task;

import gaea.user.center.service.service.IEmailRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: EmailSchedule
 * @Description: 补发邮件定时器
 * @author: jiangchangyuan1
 * @date: 2020/11/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class EmailSchedule {

    @Autowired
    IEmailRecordService emailRecordService;
    /**
     * 发送失败邮件补发定时器
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void emailScheduleTask(){
        emailRecordService.retryEmailSendTask();
    }
}
