package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: BleKeyModifyDateVo
 * @Description: 蓝牙钥匙有效期修改json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BleKeyModifyDateVo {
    /**
     * 蓝牙钥匙生效时间
     */
    private Long bleEffectiveTime;

    /**
     * 蓝牙钥匙超时时间
     */
    public Long bleKeyExpireTime;

    /**
     * 蓝牙钥匙ID
     */
    public String bleKeyId;

}
