package com.bnmotor.icv.tsp.device.model.response.sim;

import lombok.Data;

/**
 * @ClassName: TokenVo1
 * @Description: 认证返回实体基础类
 * @author: huangyun1
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class JwtTokenVo {
    /**
     * 鉴权token
     */
    private String accessToken;

    /**
     * 重刷token
     */
    private String refreshToken;
}
