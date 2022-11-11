package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

import com.bnmotor.icv.tsp.device.model.entity.VehicleTagPo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @ClassName: VehTag
 * @Description: 车辆标签
 * @author: zhangwei2
 * @date: 2020/11/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehTag {
    /**
     * 标签分类id
     */
    private Long categoryId;
    /**
     * 标签名称
     */
    private String categoryName;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 转换对象
     *
     * @param vehicleTagPo 车辆标签
     * @return 车辆标签对象
     */
    public static VehTag transform(VehicleTagPo vehicleTagPo) {
        VehTag vehTag = new VehTag();
        BeanUtils.copyProperties(vehicleTagPo, vehTag);
        return vehTag;
    }

    /**
     * 包装消息实体
     */
    public static DataSysnMessage<VehTag> decorateMsg(Integer businessType, Integer dataType, VehTag vehTag) {
        DataSysnMessage<VehTag> message = new DataSysnMessage();
        message.setType(dataType);
        message.setBusinessType(businessType);
        message.setData(vehTag);
        return message;
    }
}
