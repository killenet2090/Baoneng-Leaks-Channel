package com.bnmotor.icv.tsp.ota.model.resp.feign;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Deprecated
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignOtaFileVo implements Serializable {

	private static final long serialVersionUID = 1L;

	String respData;

}