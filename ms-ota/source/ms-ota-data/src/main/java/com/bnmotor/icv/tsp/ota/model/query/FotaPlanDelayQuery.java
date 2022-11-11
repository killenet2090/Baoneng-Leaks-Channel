package com.bnmotor.icv.tsp.ota.model.query;

import java.util.Date;
import java.util.List;

import com.bnmotor.icv.tsp.ota.model.req.Page;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 针对还未到发布时间的任务
 * 可以增加其它功能，比较前10分钟开始推送更新缓存等
 * </pre>
 *
 * @author E.YanLonG
 * @since 2021-02-07 14:50:00
 * @version 1.0.0
 */
@ApiModel(description = "OTA升级计划V2")
@Getter
@Setter
public class FotaPlanDelayQuery extends Page {

	private static final long serialVersionUID = 1L;

	private Date datetime;
	
	private List<Integer> publishStatus;
	
	private List<Integer> approvedStatus;
	
	private List<Integer> enableState;

}