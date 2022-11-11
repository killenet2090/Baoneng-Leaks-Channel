package com.bnmotor.icv.tsp.ota.event;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import lombok.Builder;
import lombok.Data;

import java.util.function.Supplier;

/**
 * @ClassName: OtaUpgradeNewVersionMessage
 * @Description:    ota升级过程信息
 * @author: xuxiaochang1
 * @date: 2020/11/25 14:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class OtaUpgradeNewVersionMessage{
    private OtaProtocol otaProtocol;
    private Long transId;
    private Long reqId;
    private Supplier<FotaObjectPo> fotaObjectPoSupplier;
    private Supplier<FotaPlanPo> fotaPlanPoSupplier;
    private Supplier<OtaDownVersionCheckResponse> otaDownVersionCheckResponseSupplier;
    private Boolean newVersion;
}
