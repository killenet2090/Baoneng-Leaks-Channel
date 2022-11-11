package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: FotaUpgradeProcessPo
 * @Description: OTA升级由一系列的升级过程组成
OTA升级过程:
1:升级确认
2:升级包下载
                                             实体类
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_upgrade_process")
@ApiModel(value="FotaUpgradeProcessPo对象", description="")
public class FotaUpgradeProcessPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "升级记录ID")
    private Long upgradeRecordId;

    @ApiModelProperty(value = "过程类型IDOTA升级过程:1:升级确认2:升级包下载3:升级包安装确认4:升级包安装5:软件回滚(软件回滚后汇报该过程)")
    private String processId;

    @ApiModelProperty(value = "过程名称")
    private String processName;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "执行状态S-成功F-失败")
    private String status;

    @ApiModelProperty(value = "升级过程描述")
    private String remark;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
