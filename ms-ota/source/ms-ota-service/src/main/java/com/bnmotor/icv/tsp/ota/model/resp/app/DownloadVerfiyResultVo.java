package com.bnmotor.icv.tsp.ota.model.resp.app;

import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;

/**
 * @ClassName: DownloadVerfiyResultVo
 * @Description： 下载确认结果对象实
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
/*@Builder
@Data*/
public class DownloadVerfiyResultVo extends BaseAppVo<DownloadVerfiyResultBodyVo>{

    public DownloadVerfiyResultVo(){
        super.setBusinessType(AppEnums.AppResponseTypeEnum.DOWNLOAD_VERIFIED_RESPONSE.getType());
    }
}
