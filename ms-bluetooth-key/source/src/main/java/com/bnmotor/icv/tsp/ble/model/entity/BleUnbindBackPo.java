package com.bnmotor.icv.tsp.ble.model.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@TableName("tb_tsp_ble_unbind_back")
public class BleUnbindBackPo implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 移动设备ID
     */
	private String deviceId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 命令序列号
     */
    private String serialNum;
    /**
     * 返回状态吗
     */
    private String respCode;
    /**
     * 返回状态
     */
    private String status;
    /**
     * 删除标志
     */
	private String delFalg;
    /**
     * 创建者
     */
	private String createBy;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
	private Date updateTime;
}
