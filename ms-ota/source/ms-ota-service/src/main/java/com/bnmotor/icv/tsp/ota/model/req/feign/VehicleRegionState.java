package com.bnmotor.icv.tsp.ota.model.req.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
@ApiModel(value = "车辆区域查询响应参数", description = "车辆区域查询响应参数")
public class VehicleRegionState {

	 Long id;
	 
	 @ApiModelProperty(value = "车辆vin", example = "OTA20200924BN0002")
     String vin;
	 
	 @ApiModelProperty(value = "经度", example = "44.15646")
     double lng; // 44.15646
	 
	 @ApiModelProperty(value = "纬度", example = "101.125625")
     double lat; // 101.125625
	 
	 @ApiModelProperty(value = "省地区编码", example = "440000")
     String provinceCode; // 440000
	 
	 @ApiModelProperty(value = "市地区编码", example = "440300")
     String cityCode; // 440300
	 
	 @ApiModelProperty(value = "市地区名称", example = "深圳市")
	 String cityName;
	 
	 @ApiModelProperty(value = "区编码", example = "440304")
     String districtCode; // 440304
	 
	 @ApiModelProperty(value = "更新时间", example = "2020-11-30 19:32:33")
     String updateTime; // "2020-11-30 19:32:33"
}
