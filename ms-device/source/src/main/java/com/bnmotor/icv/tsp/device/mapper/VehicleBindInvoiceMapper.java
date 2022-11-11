package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleBindInvoicePo;
import org.apache.ibatis.annotations.Mapper;


/**
 * @ClassName: VehicleInvoicePo
* @Description: 机动车销售发票信息 dao层
 * @author huangyun1
 * @since 2020-09-25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface VehicleBindInvoiceMapper extends BaseMapper<VehicleBindInvoicePo> {

    /**
     * 根据车辆vin删除机动车销售发票
     */
    int deleteVehBindInvoiceByVin(VehicleBindInvoicePo vehicleBindInvoicePo);
}
