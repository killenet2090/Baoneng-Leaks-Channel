package com.bnmotor.icv.tsp.ble.model.response.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName: BleKeyModifyAuthVo
 * @Description: 蓝牙钥匙权限修改json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BleKeyModifyAuthVo {
    /**
     * 车辆设备ID
     */
    private String deviceId;

    /**
     * 蓝牙钥匙ID
     */
    public String  bleKeyId;

    /**
     * 蓝牙钥匙名字
     */
    public String  bleKeyName;

    /**
     * 蓝牙钥匙权限列表
     */
    public Map<Long,String> authList;
}
