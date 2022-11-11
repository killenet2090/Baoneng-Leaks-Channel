package com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct;

import com.bnmotor.icv.adam.sdk.ota.domain.EcuConfig;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessageHeader;
import com.bnmotor.icv.adam.sdk.ota.domain.UpgradeCondition;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownConfigCheckResponse;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaUpgradeConditionPo;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuConfigVo;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuFirmwareConfigListVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName: OtaMessageMapper
 * @Description: Ota message mapper request information
 * @author: xuxiaochang1
 * @date: 2020/7/27 17:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Mapper
public interface OtaMessageMapper {
    OtaMessageMapper INSTANCE = Mappers.getMapper(OtaMessageMapper.class);

    /**
     * 上行消息转换到下行消息
     * @param oaProtocol
     * @return
     */
    //OtaProtocol req2Resp(OtaProtocol oaProtocol);

    /**
     * 上行消息转换到下行消息
     * @param otaMessageHeader
     * @return
     */
    OtaMessageHeader reqHeader2RespHeader(OtaMessageHeader otaMessageHeader);

    /**
     * ota消息头对象转换
     * @param otaMessageHeader
     * @return
     */
    OtaMessageHeader otaMessageHeader2OtaMessageHeader(OtaMessageHeader otaMessageHeader);

    /**
     * 前置条件值转换
     * @param fotaUpgradeConditionPo
     * @return
     */
    @Mapping(source = "condCode", target = "condCode")
    @Mapping(source = "value", target = "condValue")
    @Mapping(source = "valueType", target = "condValueType")
    @Mapping(source = "operatorType", target = "operatorType")
    @Mapping(source = "operatorValue", target = "operatorValue")
    UpgradeCondition fotaUpgradeConditionPo2OtaUpgradeCondition(FotaUpgradeConditionPo fotaUpgradeConditionPo);

    @Mapping(source = "ecus", target = "ecuConfigs")
    @Mapping(source = "confVersion", target = "confVersion")
    OtaDownConfigCheckResponse eEcuFirmwareConfigListVo2OtaDownConfigCheckResponse(EcuFirmwareConfigListVo eEcuFirmwareConfigListVo);

    /**
     *
     * @param ecuConfigVo
     * @return
     */
    EcuConfig eEcuFirmwareConfigListVo2OtaDownConfigCheckResponse(EcuConfigVo ecuConfigVo);

    /**
     *
     * @param ecuConfigVo
     * @return
     */
    List<EcuConfig> eEcuFirmwareConfigListVo2OtaDownConfigCheckResponse(List<EcuConfigVo> ecuConfigVo);

    /**
     *
     * @param fotaCarInfoDto
     * @return
     */
    FotaObjectPo fotaCarInfoDto2FotaObjectDo(FotaCarInfoDto fotaCarInfoDto);
}
