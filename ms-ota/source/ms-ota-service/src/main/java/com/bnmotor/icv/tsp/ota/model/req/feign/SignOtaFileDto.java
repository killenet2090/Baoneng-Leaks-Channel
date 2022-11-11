package com.bnmotor.icv.tsp.ota.model.req.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "PKIOTA文件签名", description = "PKIOTA文件签名")
public class SignOtaFileDto {

	byte[] hash;
	String applicationName;
}