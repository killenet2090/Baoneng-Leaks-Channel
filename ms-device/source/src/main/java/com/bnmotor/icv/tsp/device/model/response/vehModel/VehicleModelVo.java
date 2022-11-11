package com.bnmotor.icv.tsp.device.model.response.vehModel;

import lombok.Data;

@Data
public class VehicleModelVo {
    private Long orgId;

    private Long vehSeriesId;

    private String vehSeriesName;

    private Long brandId;

    private String brandName;

    private Long vehModelId;

    private String vehModelName;

    private Long yearStyle;

    private String yearStyleName;

    private Long configId;

    private String configName;
}
