package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FotaObjectVo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/8/5 20:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaObjectVo {
    private String id;
    private String brandName;
    private String currentArea;
    private String modelName;
    private String modelYear;
    private String saleArea;
    private String seriesName;
    private String subModelName;
    private String vin;
    private Integer current;
    private Integer pageSize;
    private Integer startRow;
    private Integer disabled;
//    private String lastVersion;
}
