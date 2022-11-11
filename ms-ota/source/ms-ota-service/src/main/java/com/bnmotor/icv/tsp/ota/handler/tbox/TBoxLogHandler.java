package com.bnmotor.icv.tsp.ota.handler.tbox;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaVersionCheckVerifyPo;
import com.bnmotor.icv.tsp.ota.model.tbox.upgrade.UpgradeLog;
import com.bnmotor.icv.tsp.ota.service.IFotaVersionCheckVerifyDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaVersionCheckVerifyService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @ClassName: TBoxUpgradeLogHandler.java TBoxUpgradeLogHandler
 * @Description: tbox 上传升级日志=> 汇聚平台存储 => ota 消费并关联记录
 * @author E.YanLonG
 * @since 2020-10-21 19:51:58
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@OtaMessageAttribute(topics = { "tsp-oss-ota-upgrade" }, msgtype = UpgradeLog.class)
@Slf4j
public class TBoxLogHandler implements KafkaHandlerManager.KafkaHandler<UpgradeLog> {
	
	@Autowired
	private IFotaVersionCheckVerifyService fotaVersionCheckVerifyService;

	@Autowired
	private IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;
	
	@Override
	public void onMessage(UpgradeLog message) {
		Long transId = message.getTransId();
		String vin = message.getVin();
		log.info("查询FotaVersionCheckVerifyPo.transId|{}", transId);
		
		FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getById(transId);
		MyAssertUtil.notNull(fotaVersionCheckVerifyPo, OTARespCodeEnum.DATA_NOT_FOUND);
		
		String vin0 = fotaVersionCheckVerifyPo.getVin();
		MyAssertUtil.isTrue(StringUtils.equalsIgnoreCase(vin, vin0), OTARespCodeEnum.PARAMETER_VALIDATION_ERROR);	
		
		try {
			String upgradeLog = JsonUtil.toJson(message);
			fotaVersionCheckVerifyPo.setUpgradeLog(upgradeLog);
			if (fotaVersionCheckVerifyDbService.getBaseMapper().updateById(fotaVersionCheckVerifyPo) > 0) {
				log.info("更新tbox升级日志持久化信息 成功|{}", transId);
			} else {
				log.warn("更新tbox升级日志持久化信息 失败|{}", transId);
			}
		} catch (JsonProcessingException e) {
			log.error("序列化tbox升级日志持久化信息异常|{}", e.getMessage(), e);
		}
		
	}

}