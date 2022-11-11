package com.bnmotor.icv.tsp.device.service;

import com.bnmotor.icv.tsp.device.model.entity.VehicleTagPo;
import com.bnmotor.icv.tsp.device.model.request.tag.EditVehTagDto;

import java.util.List;

/**
 * @ClassName: IVehLabelService
 * @Description: 车辆标签管理接口
 * @author: zhangwei2
 * @date: 2020/7/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IVehTagService {
    /**
     * 编辑某台指定车辆对应的标签
     *
     * @param tagDto 指定车辆标签实体
     */
    void addVehTag(EditVehTagDto tagDto);

    /**
     * 根据车架号获取车辆标签
     *
     * @param vin 车架号
     * @return 车辆标签
     */
    List<VehicleTagPo> listByVin(String vin);
}
