package com.bnmotor.icv.tsp.ota.model.req.approval;

import java.util.Date;

import lombok.Data;

/**
 * @ClassName: TaskCreateRequest.java 
 * @Description: 审批流创建回调
 * @author E.YanLonG
 * @since 2021-3-22 9:42:41
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class TaskTerminateRequest {

	/**
	 * 流程实例id
	 */
	String processInstanceId;
	
	/**
	 * 流程名称
	 */
	String processName;
	
	/**
	 * 消息产生时间
	 */
	Date createTime;
	
	/**
	 * businessKey
	 */
	String businessKey;
	
	/**
	 * 要通知的用户账户
	 */
	String account;
	
	/**
	 * 上一个任务执行结果(有可能是审批 有可能是驳回)
	 */
	String result;
	
}
