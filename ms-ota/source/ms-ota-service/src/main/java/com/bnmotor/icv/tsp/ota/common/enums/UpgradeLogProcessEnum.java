package com.bnmotor.icv.tsp.ota.common.enums;

import com.bnmotor.icv.tsp.ota.model.entity.FotaVersionCheckVerifyPo;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: UpgradeLogProcessEnum.java UpgradeLogProcessEnum
 * @Description: 用来判断tb_upgrade_verify的当前状态，如果当前结果不为空，需要判断前一个状态是否为空（当前状态可能为默认值）
 * @author E.YanLonG
 * @since 2020-10-26 10:28:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum UpgradeLogProcessEnum {

	CHECK_ACK_VERIFY_STATUS("check_ack_verify_status", 0, "客户端升版本检查状态："), // 1=确认已经获收到版本检查结果:0=未收到版本检查结果
	
	UPGRADE_VERIFY_STATUS("upgrade_verify_status", 1, "客户端升级确认状态："), // 客户端升级确认(远程下载)消息状态:1=确认升级，2=放弃下载

	DOWNLOAD_STATUS("download_status", 2, "升级包下载阶段："), // 下载完成状态：0=未开始，1=下载中，2=下载中止，3下载等待，4=下载完成，5=校验成功，6=校验失败

	INSTALLED_ACK_VERIFY_STATUS("installed_ack_verify_status", 3, "TBOX升级确认："),

	INSTALLED_VERIFY_STATUS("installed_verify_status", 4, "TBOX安装确认："),

	INSTALLED_STATUS("installed_status", 5, "安装状态："),

	INSTALLED_COMPLETE_STATUS("installed_complete_status", 6, "安装结果："),

	READY_FOR_UPDATE("准备升级", 0, "准备升级："),;

	UpgradeLogProcessEnum(String period, Integer order, String desc) {
		this.period = period;
		this.order = order;
		this.desc = desc;
	}

//	public static void main(String[] args) {
//
//		FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = new FotaVersionCheckVerifyPo();
//		fotaVersionCheckVerifyPo.setInstalledCompleteStatus(null);
//		fotaVersionCheckVerifyPo.setInstalledStatus(0);
//		fotaVersionCheckVerifyPo.setInstalledVerifyStatus(1); // 现在升级
//
//		Pair target = UpgradeLogProcessEnum.selectUpgradeState(fotaVersionCheckVerifyPo);
//		System.err.println(target);
//	}

	public static Pair selectUpgradeState(FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo) {
		
		Integer upgradeVerifyStatus = fotaVersionCheckVerifyPo.getUpgradeVerifyStatus();
		Integer downloadStatus = fotaVersionCheckVerifyPo.getDownloadStatus();

		Integer installedAckVerifyStatus = fotaVersionCheckVerifyPo.getInstalledAckVerifyStatus();
		Integer installedVerifyStatus = fotaVersionCheckVerifyPo.getInstalledVerifyStatus();
		Integer installedStatus = fotaVersionCheckVerifyPo.getInstalledStatus();
		Integer installedCompleteStatus = fotaVersionCheckVerifyPo.getInstalledCompleteStatus();
		Integer checkAckVerifyStatus = fotaVersionCheckVerifyPo.getCheckAckVerifyStatus();
		
		Pair p0 = Pair.of().setPeriod(INSTALLED_COMPLETE_STATUS).setState(installedCompleteStatus); // 无默认值
		Pair p1 = Pair.of().setPeriod(INSTALLED_STATUS).setState(installedStatus); // 默认值0 未开始
		Pair p2 = Pair.of().setPeriod(INSTALLED_VERIFY_STATUS).setState(installedVerifyStatus); // 无默认值
		Pair p3 = Pair.of().setPeriod(INSTALLED_ACK_VERIFY_STATUS).setState(installedAckVerifyStatus); // 默认值 0
		Pair p4 = Pair.of().setPeriod(DOWNLOAD_STATUS).setState(downloadStatus); // 无默认值
		Pair p5 = Pair.of().setPeriod(UPGRADE_VERIFY_STATUS).setState(upgradeVerifyStatus); // 无默认值
		Pair p6 = Pair.of().setPeriod(CHECK_ACK_VERIFY_STATUS).setState(checkAckVerifyStatus); // 默认值0
		
		if (Objects.nonNull(installedCompleteStatus)) { // 有值，已经到最后一步
			return Pair.selectStateFirst(p0);
		}
		
		if (Objects.nonNull(installedStatus) && installedStatus.intValue() != 0) {
			return Pair.selectStateFirst(p1);
		}
		
		if (Objects.nonNull(installedVerifyStatus)) {
			return Pair.selectStateFirst(p2);
		}
		
		if (Objects.nonNull(installedAckVerifyStatus) && installedAckVerifyStatus.intValue() != 0) {
			return Pair.selectStateFirst(p3);
		}
		
		if (Objects.nonNull(downloadStatus)) {
			return Pair.selectStateFirst(p4);
		}
		
		if (Objects.nonNull(upgradeVerifyStatus)) {
			return Pair.selectStateFirst(p5);
		}
		
		if (Objects.nonNull(upgradeVerifyStatus)) {
			return Pair.selectStateFirst(p6);
		}
		
		return Pair.of().setPeriod(READY_FOR_UPDATE).setState(-1).setRemark("准备升级");
	}
	
	public static Pair selectUpgradeState0(FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo) {
		Integer checkAckVerifyStatus = fotaVersionCheckVerifyPo.getCheckAckVerifyStatus();
		Integer upgradeVerifyStatus = fotaVersionCheckVerifyPo.getUpgradeVerifyStatus();
		Integer downloadStatus = fotaVersionCheckVerifyPo.getDownloadStatus();

		Integer installedAckVerifyStatus = fotaVersionCheckVerifyPo.getInstalledAckVerifyStatus();
		Integer installedVerifyStatus = fotaVersionCheckVerifyPo.getInstalledVerifyStatus();
		Integer installedStatus = fotaVersionCheckVerifyPo.getInstalledStatus();
		Integer installedCompleteStatus = fotaVersionCheckVerifyPo.getInstalledCompleteStatus();

		Pair p0 = Pair.of().setPeriod(INSTALLED_COMPLETE_STATUS).setState(installedCompleteStatus); // 无默认值
		Pair p1 = Pair.of().setPeriod(INSTALLED_STATUS).setState(installedStatus); // 默认值0 未开始
		Pair p2 = Pair.of().setPeriod(INSTALLED_VERIFY_STATUS).setState(installedVerifyStatus); // 无默认值
		Pair p3 = Pair.of().setPeriod(INSTALLED_ACK_VERIFY_STATUS).setState(installedAckVerifyStatus); // 默认值 0
		Pair p4 = Pair.of().setPeriod(DOWNLOAD_STATUS).setState(downloadStatus); // 无默认值
		Pair p5 = Pair.of().setPeriod(UPGRADE_VERIFY_STATUS).setState(upgradeVerifyStatus); // 无默认值
		Pair p6 = Pair.of().setPeriod(CHECK_ACK_VERIFY_STATUS).setState(checkAckVerifyStatus); // 默认值0

		p0.next(p1.next(p2.next(p3.setNext(p4.next(p5.setNext(p6))))));
		return Pair.selectStateFirst(p0, p1 ,p2, p3, p4, p5, p6);
	}

	String period;
	String desc;
	Integer order;

	@NoArgsConstructor(staticName = "of")
	@Accessors(chain = true)
	@Data
	public static class Pair {
		final static Map<UpgradeLogProcessEnum, List<Pair>> UPGRADE_LOG_PROCESS_SET = Maps.newHashMap();

		static {

			UPGRADE_LOG_PROCESS_SET.put(UPGRADE_VERIFY_STATUS, Arrays.asList( //
					Pair.of().setPeriod(UPGRADE_VERIFY_STATUS).setState(1).setRemark("确认升级").setFinish(false), //
					Pair.of().setPeriod(UPGRADE_VERIFY_STATUS).setState(2).setRemark("放弃下载").setFinish(true)));
			UPGRADE_LOG_PROCESS_SET.put(DOWNLOAD_STATUS, Arrays.asList( //

					Pair.of().setPeriod(DOWNLOAD_STATUS).setState(0).setRemark("下载未开始").setFinish(false), //
					Pair.of().setPeriod(DOWNLOAD_STATUS).setState(1).setRemark("下载中").setFinish(false), //
					Pair.of().setPeriod(DOWNLOAD_STATUS).setState(2).setRemark("下载中止").setFinish(true), //
					Pair.of().setPeriod(DOWNLOAD_STATUS).setState(3).setRemark("下载等待").setFinish(false), //
					Pair.of().setPeriod(DOWNLOAD_STATUS).setState(4).setRemark("下载完成").setFinish(false), //
					Pair.of().setPeriod(DOWNLOAD_STATUS).setState(5).setRemark("下载校验成功").setFinish(false), //
					Pair.of().setPeriod(DOWNLOAD_STATUS).setState(6).setRemark("下载校验失败").setFinish(true) //
			));
			UPGRADE_LOG_PROCESS_SET.put(INSTALLED_ACK_VERIFY_STATUS, Arrays.asList( //
					Pair.of().setPeriod(INSTALLED_ACK_VERIFY_STATUS).setState(1).setRemark("升级确认已收到").setFinish(false), //
					Pair.of().setPeriod(INSTALLED_ACK_VERIFY_STATUS).setState(2).setRemark("升级确认未收到").setFinish(false).setDefaut(true)//
			));
			UPGRADE_LOG_PROCESS_SET.put(INSTALLED_VERIFY_STATUS, Arrays.asList( //

					Pair.of().setPeriod(INSTALLED_VERIFY_STATUS).setState(1).setRemark("开始升级").setFinish(false), //
					Pair.of().setPeriod(INSTALLED_VERIFY_STATUS).setState(2).setRemark("预约升级").setFinish(false), //
					Pair.of().setPeriod(INSTALLED_VERIFY_STATUS).setState(2).setRemark("放弃安装").setFinish(true) //
			));

			UPGRADE_LOG_PROCESS_SET.put(INSTALLED_STATUS, Arrays.asList( //

					Pair.of().setPeriod(INSTALLED_STATUS).setState(0).setRemark("安装未开始").setFinish(false).setDefaut(true), //
					Pair.of().setPeriod(INSTALLED_STATUS).setState(1).setRemark("安装前置条件检查失败").setFinish(true), //
					Pair.of().setPeriod(INSTALLED_STATUS).setState(2).setRemark("安装升级中").setFinish(false), //
					Pair.of().setPeriod(INSTALLED_STATUS).setState(3).setRemark("HU取消升级").setFinish(true) //
			));
			UPGRADE_LOG_PROCESS_SET.put(INSTALLED_COMPLETE_STATUS, Arrays.asList( //安装结果：1=升级完成（成功），2=升级未完成，3=升级失败

					Pair.of().setPeriod(INSTALLED_COMPLETE_STATUS).setState(1).setRemark("升级完成（成功）").setFinish(true), //
					Pair.of().setPeriod(INSTALLED_COMPLETE_STATUS).setState(2).setRemark("升级未完成").setFinish(true), // 有部分回滚失败的情况
					Pair.of().setPeriod(INSTALLED_COMPLETE_STATUS).setState(3).setRemark("升级失败").setFinish(true) //
			));
			
			UPGRADE_LOG_PROCESS_SET.put(CHECK_ACK_VERIFY_STATUS, Arrays.asList( //安装结果：1=升级完成（成功），2=升级未完成，3=升级失败
					Pair.of().setPeriod(CHECK_ACK_VERIFY_STATUS).setState(0).setRemark("版本检查（收到结果）").setFinish(false), //
					Pair.of().setPeriod(CHECK_ACK_VERIFY_STATUS).setState(1).setRemark("版本检查（未收到结果）").setFinish(false) // 有部分回滚失败的情况
			));
			
			
		}

		public static Pair selectPeriod(Pair pair) {
			UpgradeLogProcessEnum en = pair.getPeriod();
			Integer state = pair.getState();
			Pair target = UPGRADE_LOG_PROCESS_SET.get(en).stream().filter(it -> it.getState().equals(state)).findFirst().orElse(null);
			if (Objects.nonNull(target) && target.isDefaut() && Objects.nonNull(target.prev) ) {
				// 找到前一个
				return selectPeriod(target.prev);
			} 
			
			return target;
		}

		/**
		 * 选择第一个不为空的数据
		 * @param pairs
		 * @return
		 */
		public static Pair selectStateFirst(Pair... pairs) {
			for (Pair pair : pairs) {
				Pair target = selectPeriod(pair);
				if (Objects.nonNull(target)) {
					return target;
				}
			}
			return Pair.of().setPeriod(READY_FOR_UPDATE).setState(-1).setRemark("准备升级");
		}
		
		@Override
		public String toString() {
			boolean finish = this.finish;
			return (finish ? "" :  "") + this.getRemark();
		}
		
		public Pair next(Pair next) {
			next.setPrev(this);
			this.next = next;
			return this;
		}
		
		public Pair next() {
			return next;
		}
		
		public Pair prev(Pair prev) {
			prev.next = this;
			this.prev = prev;
			return this;
		}
		
		Pair next;
		Pair prev;
		
		UpgradeLogProcessEnum period;
		Integer state;
		String remark;
		boolean finish = false;
		boolean defaut = false;
	}

}