package com.bnmotor.icv.tsp.ble.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: TspBleAckInfo
 * @Description:  实体类
 * @author shuqi1
 * @since 2020-09-16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@TableName("tb_tsp_ble_ack_info")
public class BleAckInfoPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID,value = "id" )
    private Long id;

    private String projectId;

    private String deviceId;

    private Long bleKeyId;

    private Integer userType;

    private Date destroyTime;

    private String ackText;

    private Integer status;

    private Integer version;

    private Integer delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}
