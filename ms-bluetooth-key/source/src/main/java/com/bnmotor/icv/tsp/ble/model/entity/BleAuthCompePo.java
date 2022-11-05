package com.bnmotor.icv.tsp.ble.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shuqi1
 * @ClassName: TspBleAuthCompe
 * @Description: 车主蓝牙授权角色
 * 蓝牙授权时授予的角色 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-20
 */
@Data
@Builder
@TableName("tb_tsp_ble_auth_compe")
public class BleAuthCompePo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * "无效"	"0"
     * "左前门解锁"	"1"
     * "左前门闭锁"	"2"
     * "四门解锁"	"3"
     * "四门闭锁"	"4"
     * "后备箱解锁"	"5"
     * "后备箱闭锁"	"6"
     * "升窗"	"7"
     * "降窗"	"8"
     * "开空调"	"9"
     * "关空调"	"10"
     * "寻车"	"11"
     * "闪灯"	"12"
     * "闪灯鸣笛"	"13"
     * "PE"	"14"
     * "PS"	"15"
     */
    private String serviceName;

    /**
     * 服务代码
     * "无效"	"0"
     * "左前门解锁"	"1"
     * "左前门闭锁"	"2"
     * "四门解锁"	"3"
     * "四门闭锁"	"4"
     * "后备箱解锁"	"5"
     * "后备箱闭锁"	"6"
     * "升窗"	"7"
     * "降窗"	"8"
     * "开空调"	"9"
     * "关空调"	"10"
     * "寻车"	"11"
     * "闪灯"	"12"
     * "闪灯鸣笛"	"13"
     * "PE"	"14"
     * "PS"	"15"
     */
    private String serviceCode;

    /**
     * 服务描述
     */
    private String serviceDesc;

    /**
     * 删除标志
     * 0 - 正常
     * 1 - 删除
     */
    private Integer delFlag;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
     * 登录帐号
     */
    private String createBy;

    private Date createTime;

    /**
     * 修改人
     * 登录的帐号
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TspBleAuthCompe{" +
                ", id=" + id +
                ", projectId=" + projectId +
                ", serviceName=" + serviceName +
                ", serviceCode=" + serviceCode +
                ", serviceDesc=" + serviceDesc +
                ", delFlag=" + delFlag +
                ", version=" + version +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                "}";
    }
}
