package com.bnmotor.icv.tsp.operation.veh.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhoulong1
 * @ClassName: UserBindDetailVo
 * @Description: 用户中心车辆绑定记录
 * @since: 2020/7/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class UserBindDetailVo{
    /**
     * 车牌号
     */
    private String drivinglicPlate;
    /**
     * Vin
     */
    private String vin;
    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date bindTime;
    /**
     * 解绑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date unbindTime;
    /**
     * 绑定人
     */
    private String bindBy;
    /**
     * 解绑人
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String unbindBy;

    /**
     * 品牌
     */
    private String brandName;
    /**
     * 车系
     */
    private String vehSeriesName;
    /**
     * 车辆型号
     */
    private String vehModelName;
    /**
     * 年款
     */
    private String yearStyleName;
    /**
     * 配置
     */
    private String vehConfigName;
    /**
     * 车辆类型 1-纯电动 2-燃油车
     */
    private String  vehType;

    /**
     * 车辆是否在线
     */
    private boolean onlineStatus;

}
