package gaea.user.center.service.task;

import gaea.user.center.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: UserSchedule
 * @Description: 用户信息定时任务
 * @author: jiangchangyuan1
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class UserSchedule {
    @Autowired
    private IUserService userService;

    /**
     * 账户过期禁用定时任务
     */
    @Scheduled(cron = "01 00 00 * * ?")
    public void userExpireTimeScheduleTask(){
        userService.userExpireTimeTask();
    }

    /**
     * 账户超过60天未登录休眠定时任务
     */
    @Scheduled(cron = "01 00 00 * * ?")
    public void userLastLoginTimeoutTask(){
        userService.userLastLoginTimeoutTask();
    }
}
