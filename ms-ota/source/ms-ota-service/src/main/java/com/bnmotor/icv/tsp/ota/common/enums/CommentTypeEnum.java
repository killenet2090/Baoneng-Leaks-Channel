package com.bnmotor.icv.tsp.ota.common.enums;

/**
 * @ClassName: CommentTypeEnum.java 
 * @Description: 审批平台侧的审批状态枚举
 * 对于目前阶段的需求，审批任务对应的状态只有'审批'和'驳回'
 * <pre>审批平台侧的审批状态定义：审批类型 0 待提交 1 审批中 2 审批完成 3 审批未通过 4 驳回待提交</pre>
 * <pre>OTA平台侧的审批状态定义：1 免审批，2待审批、3审批中、4已审批、5拒绝 6驳回 7撤回</pre>
 * 
 * @author E.YanLonG
 * @since 2021-3-30 10:54:28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum CommentTypeEnum {
	SP("审批"), //当前任务审批通过
	BH("驳回"), //当前任务审批驳回
	CH("撤回"), //
	CX("撤销"), // 兼容zebra-approval-platform
	ZC("暂存"), //
	QS("签收"), //
	WP("委派"), //
	ZH("知会"), //
	ZY("转阅"), //
	YY("已阅"), //
	ZB("转办"), //
	QJQ("前加签"), //
	HJQ("后加签"), //
	XTZX("系统执行"), //
	TJ("提交"), //
	CXTJ("重新提交"), //
	SPJS("审批结束"), //
	LCZZ("流程终止"), //
	SQ("授权"), //
	CFTG("重复跳过"), //
	XT("协同"), //
	PS("评审"), //
	CSH("初始化"); //

	private String key; // 审批平台侧的审批状态
	private int val; // 业务平台侧的审批状态

	CommentTypeEnum(String key) {
		this.key = key;
	}
	
	/**
	 * 获取ota对应的业务状态
	 * @param result
	 * @return
	 */
	public static int otaKey(String result) {
		if (SP.key.equals(result)) {
			return 2;
		}
		
		if (BH.key.equals(result)) {
			return 6;
		}
		
		if (SPJS.key.equals(result)) {
			return 4;
		}
		
		if (LCZZ.key.equals(result)) {
			return 5;
		}
		
		if (CH.key.equals(result)) { // 平台侧撤回，OTA审批状态修改为'待审批'
			return 7;
		}
		
		if (CX.key.equals(result)) { // 平台侧撤销，OTA审批状态修改为'待审批'
			return 7;
		}
		
		return -1;
	}
}