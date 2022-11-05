package com.bnmotor.icv.tsp.ble.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: TspBlePin
 * @Description: 设备蓝牙PIN码表 实体类
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@TableName("tb_tsp_ble_pin")
public class BleCaPinPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 用户类型名称
            1.车主
            2.家人
            3.朋友
            4.其他
     */
    private String bleDeviceId;

    /**
     * 1.车主
            2.家人
            3.朋友
            4.其他
     */
    private Long userTypeId;



    /**
     * pin码
     */
    private String pinCode;

    /**
     * 删除标志
            0 - 正常
            1 - 删除
     */
    private Integer delFlag;

    /**
     * 状态标志
     0 - 正常
     1 - 删除
     */
    private Integer status;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
            登录帐号
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
            登录的帐号
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

}
