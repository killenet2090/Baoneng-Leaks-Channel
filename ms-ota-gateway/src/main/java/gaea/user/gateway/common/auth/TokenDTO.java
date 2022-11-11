package gaea.user.gateway.common.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;
import java.util.Map;

/**
 * 宝能汽车版权所有 © Copyright 2020
 *
 * @Description:   <高扩展性>Token 实体类</高扩展性>
 * @Project:
 * @CreateDate: 2020/03/30
 * @Author: <a href="poker0325@me.com">jiankang.xia</a>
 */
@Data
public class TokenDTO {

    private String token;

    private String refreshToken;

    @NonNull
    private Long expireTime;

    private Long refreshTokenTime;

    @NonNull
    private String subject;

    @NonNull
    private String audience;

    @NonNull
    private Date issuedAt;

    @NonNull
    private String issuer;

    @NonNull
    private Long id;

    @NonNull
    private Map<String, Object> claims;

    @NonNull
    private SignatureAlgorithm signatureAlgorithm;

    @NonNull
    private String secret;

    public TokenDTO() {
    }
}
