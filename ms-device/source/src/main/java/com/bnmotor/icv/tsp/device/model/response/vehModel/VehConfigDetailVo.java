package com.bnmotor.icv.tsp.device.model.response.vehModel;

import com.bnmotor.icv.adam.core.view.BaseView;
import com.bnmotor.icv.tsp.device.model.response.device.ModelDeviceVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: VehConfigDetailVo
 * @Description: 车辆配置详细信息
 * @author: zhangwei2
 * @date: 2020/8/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode
public class VehConfigDetailVo implements Serializable {
    private static final long serialVersionUID = 1338497827640583755L;

    /**
     * 项目名称
     */
    @JsonView(BaseView.class)
    private String projectName;

    /**
     * 车型名称
     */
    @JsonView(BaseView.class)
    private String modelName;

    /**
     * 车型编号
     */
    @JsonView(BaseView.class)
    private String modelCode;

    /**
     * 能源类型
     */
    @JsonView(BaseView.class)
    private Integer vehType;

    /**
     * 能源类型
     */
    @JsonView(BaseView.class)
    private String vehTypeValue;

    /**
     * 生产商
     */
    @JsonView(BaseView.class)
    private String supplierName;

    /**
     * 零部件型号列表
     */
    @JsonView(BaseView.class)
    private List<ModelDeviceVo> devices;
}
