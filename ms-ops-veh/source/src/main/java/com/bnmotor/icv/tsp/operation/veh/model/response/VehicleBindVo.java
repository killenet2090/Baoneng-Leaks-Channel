package com.bnmotor.icv.tsp.operation.veh.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zhoulong1
 * @ClassName: VehicleBindVo
 * @Description: 车辆绑定信息返回对象
 * @since: 2020/7/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleBindVo {
    /**
     * 用户uid
     */
    private Long uid = 10086L;
    /**
     * 用户手机号
     */
    private String phone = "13534987612";
    /**
     * 用户姓名
     */
    private String userName = "王小帅";
    /**
     * 用户身份证号
     */
    private String idCardNum = "441564198905189087";
    /**
     * 更新人
     */
    private String updateBy = "王小帅";
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
    /**
     * 原因
     */
    private String reason = "这就是原因";
    /**
     * 状态
     */
    private Integer status = 0;
}
