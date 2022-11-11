package com.bnmotor.icv.tsp.device.model.response.vehDetail;

import com.bnmotor.icv.adam.core.view.BaseView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

/**
 * @ClassName: MgtUserVo
 * @Description: 管理后台用户信息
 * @author: zhangwei2
 * @date: 2020/7/2
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class MgtUserVo {
    /**
     * 用户唯一标识
     */
    @JsonView(BaseView.class)
    private String uid;
    /**
     * 手机号码
     */
    @JsonView(BaseView.class)
    private String phone;
    /**
     * 用户姓名
     */
    @JsonView(BaseView.class)
    private String userName;

    /**
     * 车辆VIN码
     */
    @JsonView(BaseView.class)
    private String vin;

    /**
     * 身份证号码
     */
    @JsonView(BaseView.class)
    private String idCardNum;


    /**
     * 绑定状态
     */
    @JsonView(BaseView.class)
    private Integer status;
}
