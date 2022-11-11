package com.bnmotor.icv.tsp.ota.model.resp.app;

import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;

/**
 * @ClassName: UpgradeTerminateVo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/3/10 14:33
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class UpgradeTerminateVo extends VoidAppVo{
    public UpgradeTerminateVo(){
        super.setBusinessType(AppEnums.AppResponseTypeEnum.UPGRADE_TERMINATE_RESPONSE.getType());
    }
}
