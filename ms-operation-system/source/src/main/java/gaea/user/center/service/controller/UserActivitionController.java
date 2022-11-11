package gaea.user.center.service.controller;

import gaea.user.center.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: UserActivitionController
 * @Description: 用户激活控制器
 * @author: jiangchangyuan1
 * @date: 2020/12/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
@RequestMapping("/user/activation")
public class UserActivitionController {

    @Autowired
    private IUserService userService;
    /**
     * 用户账户激活
     * @param phone
     * @return
     * @throws Exception
     */
    @GetMapping("/url")
    public String activationUrl(String phone) throws Exception {
        return userService.activationUrl(phone);
    }
}
