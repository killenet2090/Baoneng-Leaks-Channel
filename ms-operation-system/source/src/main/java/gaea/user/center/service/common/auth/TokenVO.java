package gaea.user.center.service.common.auth;

import lombok.Data;

/**
 * 宝能汽车 版权所有 © Copyright 2020
 *
 * @Description:
 * @Project:
 * @CreateDate: 2020/02/30
 * @Author: <a href="poker0325@me.com">jiankang.xia</a>
 */
@Data
public class TokenVO
{
    /**
     * TODO should be named accessToken but api its token
     */
    private String token;

    private String refreshToken;

    private Long expireTime;

    private Long refreshExpireTime;
}
