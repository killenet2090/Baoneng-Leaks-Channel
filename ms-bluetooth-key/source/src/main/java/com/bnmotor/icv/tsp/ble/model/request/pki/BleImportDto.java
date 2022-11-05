package com.bnmotor.icv.tsp.ble.model.request.pki;

import com.bnmotor.icv.tsp.ble.util.RandomUnit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: BleVerSignDto
 * @Description: 验证签名
 * @author shuqi1
 * @since 2020-11-1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleImportDto {
    /**
     * 算法
     */
    private String groupId="Group-2";
    /**
     * 应用程序名字
     */
    private byte[] key;;
    /**
     * 待验签名的原文字节数据
     */
    private Integer keyId=31;
}
