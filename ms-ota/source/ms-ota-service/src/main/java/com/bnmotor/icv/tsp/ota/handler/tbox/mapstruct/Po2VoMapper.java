package com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct;

import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyFirmwareListDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyPreConditionDto;
import com.bnmotor.icv.tsp.ota.model.resp.FotaUpgradeConditionVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanDetailV2Vo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanV2Vo;
import com.bnmotor.icv.tsp.ota.model.resp.web.FotaStrategySelectableFirmwareVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName: Po2VoMapper 普通的Po2Vo转换类
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/12 10:39
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Mapper
public interface Po2VoMapper {
    Po2VoMapper INSTANCE = Mappers.getMapper(Po2VoMapper.class);

    /**
     * FotaUpgradeConditionPo 到 FotaUpgradeConditionVo
     * @param fotaUpgradeCondition
     * @return
     */
    FotaUpgradeConditionVo fotaUpgradeConditionPo2FotaUpgradeConditionVo(FotaUpgradeConditionPo fotaUpgradeCondition);

    /**
     * FotaStrategyPo --> FotaStrategyDto
     * @param fotaStrategyPo
     * @return
     */
    FotaStrategyDto fotaStrategyPo2FotaStrategyDto(FotaStrategyPo fotaStrategyPo);

    /**
     * FotaStrategyFirmwareListPo --> FotaStrategyFirmwareListDto
     * @param fotaStrategyFirmwareListPo
     * @return
     */
    FotaStrategyFirmwareListDto fotaStrategyPo2FotaStrategyDto(FotaStrategyFirmwareListPo fotaStrategyFirmwareListPo);

    /**
     * FotaFirmwarePo --> FotaStrategySelectableFirmwareVo
     * @param ftaFirmwareDo
     * @return
     */
    FotaStrategySelectableFirmwareVo fotaFirmwarePo2FotaStrategySelectableFirmwareVo(FotaFirmwarePo ftaFirmwareDo);

    /**
     *
     * @param fotaStrategyPreConditionPo
     * @return
     */
    FotaStrategyPreConditionDto fotaStrategyPreConditionPo2FotaStrategyPreConditionDto(FotaStrategyPreConditionPo fotaStrategyPreConditionPo);

    /**
     *
     * @param fotaPlanPo
     * @return
     */
    @Mappings({
    	@Mapping(source = "newVersionTips", target = "versionTips"), //
    })
    FotaPlanDetailV2Vo fotaPlanPo2FotaPlanDetailV2Vo(FotaPlanPo fotaPlanPo);

    /**
     * @param fotaPlanPo
     * @return
     */
    @Mappings({ //
		@Mapping(source = "planStartTime", target = "planStartTime"), //
		@Mapping(source = "planEndTime", target = "planEndTime"), //
		@Mapping(source = "planMode", target = "planMode"), //
		@Mapping(source = "upgradeMode", target = "upgradeMode"), //
		@Mapping(source = "approveState", target = "approveStatus"), //
		@Mapping(source = "publishState", target = "publishStatus"), //
		@Mapping(source = "isEnable", target = "isEnable"), //
		@Mapping(source = "batchSize", target = "batchSize"), //
		
		
})
    FotaPlanV2Vo fotaPlanPo2FotaPlanV2Vo(FotaPlanPo fotaPlanPo);
}
