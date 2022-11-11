package com.bnmotor.icv.tsp.ota.model.req.v2;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.validate.Save;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FotaPlanV2Req.java FotaPlanV2Req
 * @Description: V2版本新建升级任务
 * @author E.YanLonG
 * @since 2020-11-30 10:29:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "升级任务信息V2")
@Data
public class FotaPlanV2Req extends BasePo {

	@ApiModelProperty(value = "任务ID", example = "1328997695695290369", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@NotNull(message = "任务ID不能为空", groups = {Update.class})
	private Long id;

	@ApiModelProperty(value = "升级策略Id", example = "100", dataType = "Long", required = true)
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@NotNull(message = "任务ID不能为空", groups = {Save.class, Update.class})
	private Long fotaStrategyId;

	@ApiModelProperty(value = "项目ID", example = "bngrp", dataType = "String")
	private String projectId;

	@ApiModelProperty(value = "任务名称", example = "任务名称：这是一个测试任务的名称", dataType = "String", required = true)
	@Length(max = 255, message = "任务名称超过最大允许范围")
	@NotEmpty(message = "任务名称不能为空", groups = {Save.class, Update.class})
	private String planName;

	@ApiModelProperty(value = "任务开始时间", example = "2020-06-08 17:07:14", dataType = "Date", required = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //返回时间类型
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收时间类型
//	@NotNull(message = "任务开始时间不能为空", groups = {Save.class, Update.class})
	private Date planStartTime;

	@ApiModelProperty(value = "结束时间", example = "2020-06-08 17:07:14", dataType = "Date", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收时间类型
//	@NotNull(message = "任务结束时间不能为空", groups = {Save.class, Update.class})
	private Date planEndTime;

	@ApiModelProperty(value = "任务模式： 0测试任务 1正式任务", example = "0", dataType = "Integer", required = true)
	@NotNull(message = "任务模式不能为空", groups = {Save.class, Update.class})
	private Integer planMode;

	@ApiModelProperty(value = "升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)", example = "1", dataType = "Integer", required = true)
	@NotNull(message = "升级模式不能为空", groups = {Save.class, Update.class})
	private Integer upgradeMode;

	@ApiModelProperty(value = "版本说明", example = "版本说明: 这是一个小范围批量测试的升级版本", dataType = "String", required = true)
	@NotEmpty(message = "版本说明不能为空", groups = {Save.class, Update.class})
	private String versionTips;

	@ApiModelProperty(value = "免责声明", example = "免责声明：这里是免责声明的内容", dataType = "String", required = true)
	@NotEmpty(message = "免责声明不能为空", groups = {Save.class, Update.class})
    private String disclaimer;

	@ApiModelProperty(value = "设备树节点Id", example = "1278883228990668801", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@NotNull(message = "设备树节点不能为空", groups = {Save.class, Update.class})
	private Long treeNodeId;

//	@ApiModelProperty(value = "标签+地区方式查询参数;selectObjectWay=1时必传", example = "1", dataType = "ByLabelAreaReq")
//	private ByFilterConditionReq filterConditionReq;

	@ApiModelProperty(value = "标签列表", example = "['1310495342958923778']", dataType = "List", hidden = true)
    List<LabelInfo> labels0;
	
	@ApiModelProperty(value = "标签列表", example = "['1310495342958923778']", dataType = "List", hidden = true)
    List<String> labels;
	
	@ApiModelProperty(value = "地域编码列表", example = "['110000']", dataType = "List")
    List<String> regions;
	
	// @ApiModelProperty(value = "车辆vin列表（车辆列表上传使用）", example = "['LWFRTFA578GHJ9K06', 'HEQIAOLING2020001']", dataType = "List")
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	List<String> vins;
	
	@ApiModelProperty(value = "操作类型 1根据条件关联车辆 2根据上传文件关联车辆", example = "1", dataType = "Integer")
	Integer operateType;
	
	@ApiModelProperty(value = "文件名称", example = "vehicle.xlsx", required = false)
	String fileName;
	
	@ApiModelProperty(value = "更新任务时是否包括新增加的车辆 1包含 0不包含", example = "true", required = false)
	Boolean isIncludeNewVehicle = true;

	@ApiModelProperty(value = "接口操作类型 1新增 2修改", hidden = true)
	Integer restfulType = 1;
	
}