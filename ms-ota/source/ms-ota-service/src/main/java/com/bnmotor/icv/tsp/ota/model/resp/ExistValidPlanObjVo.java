package com.bnmotor.icv.tsp.ota.model.resp;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

/**
 * @ClassName: ExistValidPlanObj
 * @Description: 存在有效的升级任务对象信息
 * @author: xuxiaochang1
 * @date: 2020/7/29 16:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class ExistValidPlanObjVo {
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long otaPlanId;
    
	private String otaPlanName;
    
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long otaObjectId;
    
    private String vin;

}