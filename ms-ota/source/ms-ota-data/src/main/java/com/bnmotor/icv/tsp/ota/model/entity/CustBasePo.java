package com.bnmotor.icv.tsp.ota.model.entity;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: CustBasePo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/11 10:48
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class CustBasePo extends BasePo {
    @ApiModelProperty(value = "主键")
    private Long id;
}
