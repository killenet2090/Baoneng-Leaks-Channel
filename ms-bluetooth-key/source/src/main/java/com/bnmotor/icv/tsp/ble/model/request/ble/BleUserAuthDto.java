package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName: BleUidKeyDto
 * @Description: 根据多用户查询蓝牙钥匙
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "BleUidKeyDto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BleUserAuthDto {
    /**
     * 车辆VIN
     */
    private String deviceId;
    /**
     * 用户IDS
     */
    @NotEmpty(message = "人员列表不能为空")
    private List<Long> authList;
}
