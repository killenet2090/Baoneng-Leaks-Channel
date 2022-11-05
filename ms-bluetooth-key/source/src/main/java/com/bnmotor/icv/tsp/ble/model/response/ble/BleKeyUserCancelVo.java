package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName: BleKeyUserCancelVo
 * @Description: 用户蓝牙钥匙取消胡vo
记录用户申请到的蓝牙钥匙 实体类
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BleKeyUserCancelVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆ID
     */
    private String deviceId;

    /**
     * 状态
     */
    private int status;


}
