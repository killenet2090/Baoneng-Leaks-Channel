package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "token密码验证", description = "token密码验证")
public class TokenDto {
    /**
     * token
     */
     private  String servicePwd;
    /**
     * 类型
     */
    private  Integer type=1;
}
