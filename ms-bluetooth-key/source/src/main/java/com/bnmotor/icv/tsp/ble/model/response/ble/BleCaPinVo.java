package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
/**
 * @ClassName: TspBleCaPinVo
 * @Description: 蓝牙钥匙证书pin码json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleCaPinVo {
    /**
     * 主键ID
     */
    private Long  id;

    /**
     *'项目ID'
     */
    private String projectId;
    /**
     *'设备ID
     */
    private String  deviceId;
    /**
     *'设备Mac地址'
     */
    private String  deviceMac;
    /**
     *'蓝牙连接ID'
     */
    private String  bleConId;
    /**
     *'蓝牙连接ID'
     */
    private String  userType;
    /**
     *'蓝牙连接pin'
     */
    private String  bleConPin;
    /**
     *'删除标志
     */
    private Integer delFlag;
    /**
     *'版本号',
     */
    private Integer version;
    /**
     *'创建人'
     */
    private String  createBy;
    /**
     * 创建时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long createTime;
    /**
     * 修改人
     */
    private String  updateBy;
    /**
     * 修改时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long updateTime;
}
