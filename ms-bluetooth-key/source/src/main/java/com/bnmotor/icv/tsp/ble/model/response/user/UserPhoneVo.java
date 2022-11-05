package com.bnmotor.icv.tsp.ble.model.response.user;

import com.bnmotor.icv.tsp.ble.model.Model;
import lombok.Data;

/**
 * @ClassName: UserPhoneVo
 * @Description: 用户信息查询
 * @author: shuqi1
 * @date: 2020/8/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class UserPhoneVo extends Model {
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 设备名字
     */
    private String deviceName;
    /**
     * 设备品牌
     */
    private String deviceBrand;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 设备类型
     */
    private String osType;
    /**
     * 系统版本
     */
    private String osVersion;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备注册ID
     */
    private String pushRid;

}
