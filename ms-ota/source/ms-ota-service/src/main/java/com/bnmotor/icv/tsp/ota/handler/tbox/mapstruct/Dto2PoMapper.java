package com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct;

import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.FileUploadReq;
import com.bnmotor.icv.tsp.ota.model.req.device.VehTagDto;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadCachedInfo;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadDto;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanV2Req;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaFirmwareDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyPreConditionDto;
import com.bnmotor.icv.tsp.ota.model.resp.PlanObjectListDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * @ClassName: Dto2PoMapper
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/16 10:35
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Mapper
public interface Dto2PoMapper {
    Dto2PoMapper INSTANCE = Mappers.getMapper(Dto2PoMapper.class);

    /**
     * UploadDto 到 UploadCachedInfo
     * @param uploadDto
     * @return
     */
    UploadCachedInfo uploadDto2UploadCachedInfo(UploadDto uploadDto);

    /**
     * UploadDto 到 FileUploadReq
     * @param uploadDto
     * @return
     */
    FileUploadReq uploadDto2FileUploadReq(UploadDto uploadDto);

    /**
     * vehTagDto 到 FotaObjectLabelPo
     * @param vehTagDto
     * @return
     */
    FotaObjectLabelPo vehTagDto2FotaObjectLabelPo(VehTagDto vehTagDto);

    /**
     * FotaStrategyDto --> FotaStrategyPo
     * @param fotaStrategyDto
     * @return
     */
    FotaStrategyPo fotaStrategyDto2FotaStrategyPo(FotaStrategyDto fotaStrategyDto);

    /**
     * FotaFirmwareDto --> FotaFirmwarePo
     * @param fotaFirmwareDto
     * @return
     */
    FotaFirmwarePo fotaFirmwareDto2FotaFirmwarePo(FotaFirmwareDto fotaFirmwareDto);

    /**
     * FotaStrategyPreConditionDto --> FotaStrategyPreConditionPo
     * @param fotaStrategyPreConditionDto
     * @return
     */
    FotaStrategyPreConditionPo fotaStrategyPreConditionDto2FotaStrategyPreConditionPo(FotaStrategyPreConditionDto fotaStrategyPreConditionDto);

    PlanObjectListDetailVo map2PlanObjectListDetailVo(Map<String, Object> mapParams);

    /**
     *
     * @param fotaPlanV2Req
     * @return
     */
    @Mappings({
    	@Mapping(source = "versionTips", target = "newVersionTips"), //
    })
    
    FotaPlanPo fotaPlanV2Req2FotaPlanPo(FotaPlanV2Req fotaPlanV2Req);
}
