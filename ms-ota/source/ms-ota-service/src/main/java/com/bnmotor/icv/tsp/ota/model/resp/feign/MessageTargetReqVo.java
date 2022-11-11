package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Data;

/**
 * @ClassName: MessageTargetPo
 * @Description:  实体类
 * @author zhangjianghua1
 * @since 2020-09-04
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
public class MessageTargetReqVo {

    /**
     * 用户_ID
     */
    private Long uid;

    /**
     * 推送类型：
            11. SMS-短信
            21. APP-极光推送
            31. 车机-IHU通道
            32. 车机-TBOX
     */
    private Integer channel;

    /**
     * 推送目标
     */
    private String target;

}
