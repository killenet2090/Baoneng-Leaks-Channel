package com.bnmotor.icv.tsp.ota.model.resp.v2;


import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanExtraData;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务详情实体
 */
@Data
public class FotaPlanDetailV2Vo extends AbstractBase {
    @ApiModelProperty(value = "任务ID", example = "1328997695695290369", dataType = "Long")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;

    @ApiModelProperty(value = "升级策略Id", example = "100", dataType = "Long", required = true)
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long fotaStrategyId;

    @ApiModelProperty(value = "项目ID", example = "bngrp", dataType = "String")
    private String projectId;

    @ApiModelProperty(value = "任务名称", example = "任务名称：这是一个测试任务的名称", dataType = "String", required = true)
    @Length(max = 255, message = "任务名称不能为空")
    private String planName;

    @ApiModelProperty(value = "整个OTA开始时间 从版本检查更新开始", example = "2020-06-08 17:07:14", dataType = "Date", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    @ApiModelProperty(value = "结束时间", example = "2020-06-08 17:07:14", dataType = "Date", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    @ApiModelProperty(value = "任务模式： 0测试任务 1正式任务", example = "0", dataType = "Integer", required = true)
    private Integer planMode;

    @ApiModelProperty(value = "升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)", example = "1", dataType = "Integer", required = true)
    private Integer upgradeMode;

    @ApiModelProperty(value = "版本说明", example = "版本说明: 这是一个小范围批量测试的升级版本", dataType = "String", required = true)
    private String versionTips;

    @ApiModelProperty(value = "免责声明", example = "免责声明：这里是免责声明的内容", dataType = "String", required = true)
    private String disclaimer;

    @ApiModelProperty(value = "设备树节点Id", example = "1343131995563388930", dataType = "Long")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long treeNodeId;

    @ApiModelProperty(value = "车型路径", example = "宝能/GX16/GX16-A/2020/高配", dataType = "String")
    private String treeNodeDesc;

//    @ApiModelProperty(value = "选择的车辆列表", example = "", dataType = "Array", required = true)
//    List<FotaPlanObjListV2Vo> fotaPlanObjListVos;
//
//    @ApiModelProperty(value = "选择的车辆列表", example = "", dataType = "Array", required = true)
//    IPage<FotaPlanObjListV2Vo> fotaPlanObjListVoIPage;
    
    @ApiModelProperty(value = "附加信息", example = "{'':''}", dataType = "", required = false)
    FotaPlanExtraData fotaPlanExtraData;
    
    @ApiModelProperty(value = "设备树顺序结构（根节点->子节点的顺序）", example = "['1343131995563388930','1343131995563388931']", dataType = "Array", required = false)
//    @JsonSerialize(using = LongJsonSerializer.class)
//    @JsonDeserialize(using = LongJsonDeserializer.class)
    List<String> vehicleOptions;
    
}