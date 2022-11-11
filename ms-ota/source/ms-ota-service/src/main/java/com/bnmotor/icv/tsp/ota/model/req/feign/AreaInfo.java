package com.bnmotor.icv.tsp.ota.model.req.feign;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
@ApiModel(value = "地区码对应的地区", description = "地区码对应的地区")
public class AreaInfo {

	String id; // "32861729910296576",
	String parentId; // "32811736214147078",
	String typeId; // "32811736214147073",
	String type; // "直辖市",
	String name; // "北京市",
	String code; // "110000",
	String abbreviation; // "北京",
	String firstLetter; // "B",
	Double lng; // 116.40717,
	Double lat; // 39.90469,
	String path; // null
}
