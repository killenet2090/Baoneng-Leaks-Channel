package com.bnmotor.icv.tsp.ota.model.resp.app;

import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;
import lombok.Data;

/**
 * @ClassName: InstalledResultVo
 * @Description： 安装结果
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class InstalledResultVo extends BaseAppVo<InstalledResultBodyVo> {
    /**
     * 构造函数
     */
    public InstalledResultVo(){
        super.setBusinessType(AppEnums.AppResponseTypeEnum.INSTALLED_VERIFIED_RESPONSE.getType());
    }
}
