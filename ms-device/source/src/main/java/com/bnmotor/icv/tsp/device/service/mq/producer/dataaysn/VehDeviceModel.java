package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.model.entity.VehModelDevicePo;
import lombok.Data;

/**
 * @ClassName: Device
 * @Description: 零部件
 * @author: zhangwei2
 * @date: 2020/11/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehDeviceModel {
    /**
     * 车型组织关系id
     */
    private Long orgId;
    /**
     * 零件代码区分不同零件的代号
     */
    private String componentType;
    /**
     * 零件代码区分不同零件的代号
     */
    private String componentTypeName;
    /**
     * 零件型号
     */
    private String componentModel;
    /**
     * 零件名称
     */
    private String componentName;

    public static VehDeviceModel transform(VehModelDevicePo devicePo, VehLocalCache localCache) {
        VehDeviceModel vehDeviceModel = new VehDeviceModel();
        vehDeviceModel.setComponentType(String.valueOf(devicePo.getDeviceType()));
        vehDeviceModel.setComponentTypeName(localCache.getDeviceTypeName(devicePo.getDeviceType()));
        vehDeviceModel.setComponentModel(devicePo.getDeviceModel());
        vehDeviceModel.setOrgId(devicePo.getOrgId());
        return vehDeviceModel;
    }

    /**
     * 消息转换
     */
    public static DataSysnMessage<VehDeviceModel> decorateMsg(Integer businessType, Integer dataType, VehDeviceModel vehDeviceModel) {
        DataSysnMessage<VehDeviceModel> message = new DataSysnMessage();
        message.setType(dataType);
        message.setBusinessType(businessType);
        message.setAction(1);
        message.setData(vehDeviceModel);
        return message;
    }
}
