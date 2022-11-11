package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.ApproveStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.RebuildFlagEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @ClassName: MyBusinessUtil
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/18 11:26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public final class MyBusinessUtil {
    private MyBusinessUtil(){}

    /**
     * 任务是否有效
     * @param fotaPlanPo
     * @return
     */
    public static boolean validPlanPo(final FotaPlanPo fotaPlanPo){
        if(Objects.isNull(fotaPlanPo)){
            log.info("升级任务为空,无法判断有效性。");
            return false;
        }
        
        RebuildFlagEnum rebuilFlag = RebuildFlagEnum.select(fotaPlanPo.getRebuildFlag(), RebuildFlagEnum.V1);
	     if (RebuildFlagEnum.isV1(rebuilFlag)) {
	    	 boolean flag = MyDateUtil.inRange(fotaPlanPo.getPlanStartTime(), fotaPlanPo.getPlanEndTime());
	        log.info("任务生效周期判断：result={}, startTime={}, endTime={}", flag, fotaPlanPo.getPlanStartTime(), fotaPlanPo.getPlanEndTime());
	        /*flag &= Enums.PlanStatusTypeEnum.isValid(fotaPlanPo.getPlanStatus());
	        log.info("任务发布状态判断：result={}, fotaPlanPo.getPlanStatus()={}", flag, fotaPlanPo.getPlanStatus());*/
	        flag &= (1 == fotaPlanPo.getIsEnable());
	        log.info("任务开启状态判断：result={}, fotaPlanPo.getIsEnable()={}", flag, fotaPlanPo.getIsEnable());
	        return flag;
	     }
	     
	     boolean flag = MyDateUtil.inRange(fotaPlanPo.getPlanStartTime(), fotaPlanPo.getPlanEndTime());
	     log.info("任务生效周期判断：result={}, startTime={}, endTime={}", flag, fotaPlanPo.getPlanStartTime(), fotaPlanPo.getPlanEndTime());
	     flag &= ApproveStateEnum.approved(fotaPlanPo.getApproveState());
	     log.info("任务发布状态判断：result={}, fotaPlanPo.getApproveState()={}", flag, fotaPlanPo.getApproveState());
	     flag &= (1 == fotaPlanPo.getIsEnable());
	     log.info("任务开启状态判断：result={}, fotaPlanPo.getIsEnable()={}", flag, fotaPlanPo.getIsEnable());
	     // 增加发布状态判断
	     flag &= (PublishStateEnum.inPublishState(fotaPlanPo.getPublishState()));
	     log.info("任务发布状态判断：result={}, fotaPlanPo.getPublishState()={}", flag, fotaPlanPo.getPublishState());
	     return flag;
     }

	/**
	 * 替换内网地址
	 * @param originUrl
	 * @return
	 */
	public static String relaceDownloadUrl(String Urldomain, String originUrl){
		if(StringUtils.isEmpty(originUrl)){
			return originUrl;
		}

		if(originUrl.startsWith(CommonConstant.PUBLIC_HTTP_URL_PREFIX)){
			String newOriginUrl = originUrl.replace(CommonConstant.PUBLIC_HTTP_URL_PREFIX, "");
			String url = Urldomain + newOriginUrl.substring(newOriginUrl.indexOf(CommonConstant.BUCKET_OTA_NAME)-1, newOriginUrl.length());
			//String url = minIoConfig.getTboxPkgDownloadDomain() + newOriginUrl.substring(newOriginUrl.indexOf(CommonConstant.BUCKET_OTA_NAME)-1, newOriginUrl.length());
			log.info("url={}", url);
			return url;
		}
		return originUrl;
	}
}
