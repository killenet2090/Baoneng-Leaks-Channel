package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;

/**
 * @ClassName: IFotaPlanPublishService.java 
 * @Description: 判断任务发布状态
 * @author E.YanLonG
 * @since 2021-1-20 17:31:17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IFotaPlanPublishService {

	void selectPublishState(FotaPlanPo fotaPlanPo);
	
	boolean editable(Long otaPlanId, Integer isEnable);
	
	boolean isEffectiveFotaPlan(FotaPlanPo fotaPlanPo);
	
	boolean canDeleteFotaPlan(FotaPlanPo fotaPlanPo);

}