package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleKeyServicePo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserCheckVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;

/**
 * @ClassName: BleUserService
 * @Description: 与用户中心交互接口类
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleUserService extends IService<BleKeyServicePo> {
    /**
     * 根据用户ID查询用户名字
     * @param user_id
     * @return
     */
    UserVo getUserName(String user_id);

    /**
     * 根据电话号码检查用户是否存在
     * @param userId
     * @return
     */
    UserCheckVo getUserCheck(String userId);
}
