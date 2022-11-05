package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BleKeyModifyAuthDto {
    @NotBlank(message = "蓝牙钥匙ID不能为空")
    @ApiModelProperty(value = "蓝牙钥匙ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 19)
    public String  bleKeyId;

    @ApiModelProperty(value = "蓝牙钥匙权限列表",required = true, dataType = "String", example = "[1,2,3,5,6]")
    @NotEmpty(message = "权限列表不能为空")
    public List<Long> authList;
}
