package com.bnmotor.icv.tsp.ota.model.req.v2;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.validate.Save;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
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
@Getter
@Setter
@ApiModel(description = "升级任务信息V2")
public class FotaPlanSimpleV2Dto extends BasePo {

	@ApiModelProperty(value = "任务ID", example = "1328997695695290369", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long id;

	@ApiModelProperty(value = "整个OTA开始时间 从版本检查更新开始", example = "2020-06-08 17:07:14", dataType = "Date", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@ApiModelProperty(value = "结束时间", example = "2020-06-08 17:07:14", dataType = "Date", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date finishTime;

	/**
	 * 整车版本号
	 */
	@ApiModelProperty(value = "整车版本号", example = "1000", dataType = "String", required = true)
	@NotEmpty(message = "整车版本号不能为空", groups = {Save.class, Update.class})
	private String entireVersionNo;

	@ApiModelProperty(value = "审核状态： 1免审批，2待审批、3审批中、4已审批、5未通过", example = "0", dataType = "Integer")
	private Integer approveStatus;

	@ApiModelProperty(value = "审核状态： 1免审批，2待审批、3审批中、4已审批、5未通过", example = "0", dataType = "String")
	private String approveStatusDesc;

	@ApiModelProperty(value = "发布状态： 1待发布 2发布中 3已发布 4已失效 5延迟发布", example = "0", dataType = "Integer")
	private Integer publishStatus;

	@ApiModelProperty(value = "发布状态： 1待发布 2发布中 3已发布 4已失效 5延迟发布", example = "0", dataType = "Integer")
	private String publishStatusDesc;

	@ApiModelProperty(value = "任务模式： 1正式任务 2=测试任务", example = "0", dataType = "Integer", required = true)
	private Integer planMode;

	@ApiModelProperty(value = "任务模式： 1正式任务 2=测试任务", example = "0", dataType = "String", required = true)
	private String planModeDesc;

	@ApiModelProperty(value = "升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)", example = "1", dataType = "Integer", required = true)
	private Integer upgradeMode;

	@ApiModelProperty(value = "升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)", example = "1", dataType = "String", required = true)
	private String upgradeModeDesc;

	@ApiModelProperty(value = "车辆数量", example = "1000", dataType = "Integer", required = true)
	private Integer objectListNum;

	@ApiModelProperty(value = "任务名称", example = "任务名称：这是一个测试任务的名称", dataType = "String", required = true)
	@Length(max = 255, message = "{UpgradeTaskReq.name.maxLength.message}")
	private String planName;
}