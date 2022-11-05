package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "token密码验证", description = "token密码验证")
public class TokenCheck {
    /**
     * token
     */
    private String servicePwd;
}
