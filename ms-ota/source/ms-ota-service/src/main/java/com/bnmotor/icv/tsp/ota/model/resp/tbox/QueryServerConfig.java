package com.bnmotor.icv.tsp.ota.model.resp.tbox;

import java.util.List;

import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.HttpMessageMapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Deprecated
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class QueryServerConfig {

	/*private String confVersion;

	private List<EcuConfigVo> ecus;*/

	/*public static QueryServerConfig trnaslate(EcuFirmwareConfigListVo ecuFirmwareConfigListVo) {
		QueryServerConfig of = new QueryServerConfig();
		
		List<EcuConfigVo> ecus = HttpMessageMapper.INSTANCE.fotaFirmwarePo2EcuConfig(ecuFirmwareConfigListVo.getFotaFirmwarePos());
//		List<EcuConfig> ecus = Lists.newArrayList();
//		ecuList.forEach(firmware -> {
//			ecus.add(HttpMessageMapper.INSTANCE.fotaFirmwarePo2ecuConfig(firmware));
//		});
		of.setEcus(ecus);
		of.setConfVersion(ecuFirmwareConfigListVo.getConfVersion());
		return of;
	}*/

//	@Deprecated
//	public static EcuConfig buildEcuConfig(fotaFirmwarePo fotaFirmwarePo) {
//		EcuConfig conf = EcuConfig.of();
//		return conf;
//	}

}