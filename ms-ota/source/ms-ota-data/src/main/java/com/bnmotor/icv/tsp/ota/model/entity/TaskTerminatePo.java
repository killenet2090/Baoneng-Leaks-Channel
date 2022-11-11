/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <pre>
 *	<b>表名</b>：tb_task_terminate
 *  任务终止条件，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
/*@Getter
@Setter
@Api("任务终止条件")*/

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_task_terminate")
@ApiModel(value="TaskTerminateDo对象", description="任务终止条件表")
public class TaskTerminatePo extends BasePo {
	@ApiModelProperty("")
    private Long id;

	@ApiModelProperty("升级计划id")
    private Long otaPlanId;

	@ApiModelProperty("")
    private Integer version;

	@ApiModelProperty("最大下载失败次数")
    private Integer maxDownloadFailed;

	@ApiModelProperty("最大安装失败次数")
    private Integer maxInstallFailed;

	@ApiModelProperty("最大验证失败次数")
    private Integer maxVerifyFailed;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
