package com.bnmotor.icv.tsp.ota.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ApprovalConfig.java 
 * @Description: 审批流配置
 * @author E.YanLonG
 * @since 2021-4-7 18:02:29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
@Data
@Slf4j
public class ApprovalConfig {

	/**
	 * 流程定义查询key:升级任务
	 */
	@Value("${approval.process.define.plan:update_plan_flow}")
	String processDefinePlan;
	
	/**
	 * 流程定义查询:策略审批
	 */
	@Value("${approval.process.define.strategy:update_strategy_flow}")
	String processDefineStrategy;

}