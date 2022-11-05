package com.bnmotor.icv.tsp.ble.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: TspBleAckPush
 * @Description: 记录下发数据类型和流水号，用于消息推送 实体类
 * @author shuqi1
 * @since 2020-08-13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@TableName("tb_tsp_ble_alter_temp")
@Data
@Builder
public class BleAlterTempPo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增长ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 蓝牙钥匙ID
     */
    private Long bleId;

    /**
     * 授权ID
     */
    private Long bleAuthId;

    /**
     * 权限ID
     */
    private Long serviceId;

    /**
     * 开始时间
     */
    private Date effectiveTime;

    /**
     * 超时时间
     */
    private Date expireDate;

    /**
     * app端蓝牙钥匙
     */
    private String encryptAppBleKey;

    /**
     * app端签名
     */
    private String encryptAppBleKeySign;

    /**
     * 授权凭证
     */
    private String authVoucher;

    /**
     * 授权凭证签名
     */
    private String authVoucherSign;

    /**
     * 并发控制
     */
    private int version;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除标志
     */
    private Integer delFlag;
}
