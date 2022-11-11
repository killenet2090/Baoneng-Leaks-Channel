package com.bnmotor.icv.tsp.operation.veh.model.response;

import lombok.Data;

/**
 * @ClassName: VehProjectExportVo
 * @Description:
 * @author: wuhao1
 * @data: 2020-07-20
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehProjectExportVo
{

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目编码
     */
    private String code;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 型号数量
     */
    private Integer modelNum;

    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 车辆类型 1-纯电动；2-燃油车
     */
    private Integer vehType;

    /**
     * 车辆类型 1-纯电动；2-燃油车
     */
    private String vehTypeValue;
}
