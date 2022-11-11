package com.bnmotor.icv.tsp.ota.controller.approval;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class ApprovalPair {

	/**
	 *  1 策略 2任务
	 */
	Integer type;
	
	/**
	 * 是否是测试任务，测试任务免审批（不经过审批）
	 */
	boolean isTest;
	
	Long primaryKey;
}