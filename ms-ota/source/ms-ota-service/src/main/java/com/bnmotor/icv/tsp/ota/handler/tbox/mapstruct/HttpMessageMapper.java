package com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct;

import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuConfigVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HttpMessageMapper {

	HttpMessageMapper INSTANCE = Mappers.getMapper(HttpMessageMapper.class);

	/**
	 * FotaFirmwarePo -- > EcuConfigVo
	 * @param fotaFirmwarePo
	 * @return
	 */
	@Mappings({ //
			@Mapping(source = "componentCode", target = "ecuId"), //
			@Mapping(source = "componentName", target = "ecuName"), //
			@Mapping(source = "diagnose", target = "ecuDid"), //
			@Mapping(source = "firmwareCode", target = "ecuSwid"),
			@Mapping(source = "responseId", target = "ecuResponseId"),
	})
	EcuConfigVo fotaFirmwarePo2EcuConfig(FotaFirmwarePo fotaFirmwarePo);

	/**
	 *
	 * @param source
	 * @return
	 */
	List<EcuConfigVo> fotaFirmwarePo2EcuConfig(List<FotaFirmwarePo> source);

}