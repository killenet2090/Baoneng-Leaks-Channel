package com.bnmotor.icv.tsp.ble.model.request.ble;

import com.bnmotor.icv.tsp.ble.util.BleKeyUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: BleKeyGeneratorDto
 * @Description: 蓝牙钥匙生成过程实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleKeyGeneratorDto {
    /**
     * 加密的APP端蓝牙钥匙
     */
    private String encrypAppBleKey;
    /**
     * 签名的蓝牙钥匙用于APP端
     */
    private byte[] appSign;
    /**
     * 蓝牙钥匙原文
     */
    private String bleSrc;

    /**
     * 加密的TBOX端蓝牙钥匙
     */
    private String encrypTboxBleKey;
    /**
     * 签名的蓝牙钥匙用于Tbox端
     */
    private byte[] tboxSign;
    /**
     * 蓝牙钥匙用于Tbox端
     */
    private byte[] tboxByteSrc;

    /**
     * 授权凭证
     */
    private byte[] authVoucher;
    /**
     * 授权凭证签名
     */
    private byte[] authVoucherSign;

    /**
     * 权限
     */
    private long bleRight;

    /**
     * app端签名原文,测试用
     */
    private String byteSign;

    /**
     * 测试用
     */
    private  byte[] byteAppBleKey;
    /**
     * 测试
     */
    private String blekeyByte;

}
