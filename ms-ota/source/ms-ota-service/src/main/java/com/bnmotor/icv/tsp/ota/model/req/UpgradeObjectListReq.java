/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <pre>
 *  OTA升级计划对象清单
定义一次升级中包含哪些需要升级的车辆接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "OTA升级计划对象清单 定义一次升级中包含哪些需要升级的车辆")
public class UpgradeObjectListReq extends BasePo {
	/**
	 * <pre>
	 *  升级对象清单集合
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "[{\"taskId\":1,\"objectId\":1,\"vin\":1123,\"currentArea\":\"广州\",\"saleArea\":\"深圳\"}]", dataType = "Long")
	@NotNull(message = "{UpgradeObjectListReq.upgradeTaskObjectReqList.notNull.message}", groups = { Update.class })
    private List<UpgradeTaskObjectReq> upgradeTaskObjectReqList;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
