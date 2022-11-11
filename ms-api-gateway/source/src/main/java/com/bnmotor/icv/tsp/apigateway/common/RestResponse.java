package com.bnmotor.icv.tsp.apigateway.common;

import com.bnmotor.icv.tsp.apigateway.common.base.BaseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @ClassName: RestResponse
 * @Description: 返回对象
 * @author: hao.xinyue
 * @date: 2020/3/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class RestResponse<T> implements Serializable
{
    /**
     * 返回的编码
     */
    @JsonProperty("respCode")
    private String respCode;

    /**
     * 返回的消息
     */
    @JsonProperty("respMsg")
    private String respMsg;

	/** 返回的数据 */
	@JsonProperty("respData")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private T respData;

	@JsonProperty("respTime")
	private long responseTime = System.currentTimeMillis() / 1000;

    public RestResponse(T body, String respMsg, String respCode)
	{
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.respData = body;
	}

	public static <T> ResponseEntity<RestResponse<T>> ok(T data)
	{
		RestResponse restResponse = RestResponse.builder()
				.respCode(RespCode.SUCCESS.getValue())
				.responseTime(System.currentTimeMillis() / 1_000L)
				.respData(data)
				.build();

		return ResponseEntity.ok(restResponse);
	}

	public RestResponse()
	{
	}

	private RestResponse(String respCode, String respMsg, T body, Long responseTime)
	{
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.respData = body;
		this.responseTime = responseTime;
	}

	public static <T> ResponseEntity<RestResponse<T>> error(BaseEnum<String> resp)
	{
		return error(resp.getValue(), resp.getDescription());
	}

	public static <T> ResponseEntity<RestResponse<T>> error(BaseEnum<String> resp, String message)
	{
		return error(resp.getValue(), message);
	}

	public static <T> ResponseEntity<RestResponse<T>> error(String respCode, String message)
	{
		RestResponse restResponse = RestResponse.builder()
				.respCode(respCode)
				.respMsg(message)
				.responseTime(System.currentTimeMillis() / 1_000L)
				.build();
		return ResponseEntity.ok(restResponse);
	}
}
