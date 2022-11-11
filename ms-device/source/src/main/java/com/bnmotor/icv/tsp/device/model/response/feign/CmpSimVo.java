package com.bnmotor.icv.tsp.device.model.response.feign;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * cmp sim卡对象
 */
@Data
public class CmpSimVo {
    private Long id;

    private String iccid;

    private String msisdn;

    private String imsi;

    private String vin;

    private EnumViewVo operatorType;

    public static SimVo transfromToSimVo(CmpSimVo cmpSimVo) {
        SimVo simVo = new SimVo();
        BeanUtils.copyProperties(cmpSimVo, simVo);
        simVo.setImei(cmpSimVo.msisdn);
        EnumViewVo operatorTypeView = cmpSimVo.getOperatorType();
        if (operatorTypeView != null) {
            simVo.setNetworkOperator(operatorTypeView.getValue());
        }
        return simVo;
    }
}
