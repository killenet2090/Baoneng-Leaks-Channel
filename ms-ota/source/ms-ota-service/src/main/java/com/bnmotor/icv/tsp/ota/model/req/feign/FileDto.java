package com.bnmotor.icv.tsp.ota.model.req.feign;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "PKI文件加密", description = "PKI文件加密")
public class FileDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	Long pkgId;
	Long firmwareVerId;
	String fileInputPath;
	String fileOutputPath;
	String fileVersion;

}