package gaea.user.gateway.common.auth;

import lombok.Data;

/**
 * 宝能汽车 版权所有 © Copyright 2020
 *
 * @Description:
 * @Project:
 * @CreateDate: 2019/10/29
 * @Author: <a href="poker0325@me.com">jiankang.xia</a>
 */
@Data
public class TokenUserVO extends TokenVO{

    private Long userId;

    private String userName;

}
