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
 * @ClassName: FotaVersionCheckPo
 * @Description:  实体类
 * @author xxc
 * @since 2020-07-15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_version_check")
@ApiModel(value="FotaVersionCheckPo对象", description="")
public class FotaVersionCheckPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "汽车vin码")
    private String vehicleId;

    @ApiModelProperty(value = "服务端与客户端流程交互消息序列号")
    private Long businessId;

    @ApiModelProperty(value = "客户端发起请求时间")
    private Date reqTime;

    @ApiModelProperty(value = "检查结果:0=没有新版本，1=存在新版本")
    private Integer checkResult;

    @ApiModelProperty(value = "版本检查请求来源：1=HU发起，2=TBOX主动发起检测，3=APP客户端发起，4、云端下发通知主动检测")
    private Integer checkSourceType;

    @ApiModelProperty(value = "检查请求参数")
    private String checkParam;

    @ApiModelProperty(value = "检查请求结果")
    private String checkResponse;

    @ApiModelProperty(value = "数据并发控制字段")
    private Integer version;
}
