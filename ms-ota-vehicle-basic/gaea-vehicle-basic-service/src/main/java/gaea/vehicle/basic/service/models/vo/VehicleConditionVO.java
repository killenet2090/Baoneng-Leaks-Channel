package gaea.vehicle.basic.service.models.vo;

import gaea.vehicle.basic.service.models.domain.VehicleBrand;
import gaea.vehicle.basic.service.models.domain.VehicleModel;
import gaea.vehicle.basic.service.models.domain.VehicleSeries;
import gaea.vehicle.basic.service.models.domain.VehicleSubModel;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @author: 宝能汽车公司-智能网联部
 * @author: xiajiankang1
 * @date: 2020/4/9
 * @Copyright: 2020 www.bnmotor.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
public class VehicleConditionVO {

    private List<VehicleBrand> vehicleBrands;

    private List<VehicleSeries> vehicleSeries;

    private List<VehicleModel> vehicleModels;

    private List<VehicleSubModel> vehicleSubModels;


}
