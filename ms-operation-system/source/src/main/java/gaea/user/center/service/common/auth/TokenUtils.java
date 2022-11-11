package gaea.user.center.service.common.auth;


import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Team Mybrainsbox © Copyright 2019
 *
 * @Description:  <高扩展性>Token工具类</高扩展性>
 * @Project: com.bnmotor.user.auth
 * @CreateDate: 2019/10/28
 * @Author: <a href="poker0325@me.com">jiankang.xia</a>
 */
@Slf4j
@Component
public class TokenUtils
{


    /**
     * 生成Token
     * @Author <jiankang.xia@bngrp.com>
     * @CreateTime 2019/10/28
     * @Param  tokenDTO 实体
     * @Return TokenUserVO
     */
    public static TokenUserVO getAccessTokenAndRefreshToken(TokenDTO tokenDTO){
        if(null == tokenDTO){
            return null;
        }
        TokenUserVO tokenUserVO = new TokenUserVO();
        TokenVO refreshTokenVO ;
        TokenVO accessTokenVO = createToken(tokenDTO);
        if(null != tokenDTO.getRefreshTokenTime()){
            tokenDTO.setExpireTime(tokenDTO.getRefreshTokenTime());
            refreshTokenVO = createToken(tokenDTO);
            tokenUserVO.setRefreshToken(refreshTokenVO.getToken());
            tokenUserVO.setRefreshExpireTime(tokenDTO.getRefreshTokenTime());
        }
        tokenUserVO.setToken(accessTokenVO.getToken());
        tokenUserVO.setExpireTime(tokenDTO.getExpireTime());
        return tokenUserVO;
    }

//    public static Jws<Claims> verify(String token) {
//            Jws<Claims> claimsJws = null;
//            if (!StringUtils.isEmpty(token)) {
//                try {
//                    claimsJws = Jwts.parser().setSigningKey(TokenBuilder.tokenSecret).parseClaimsJws(token);
//                } catch (SignatureException e) {
//                    log.error("Invalid JWT signature -> Message: [{}] ", e);
//                    throw new AdamException(BusinessStatusEnum.TOKEN_EXPIRED.getCode(),BusinessStatusEnum.TOKEN_EXPIRED.getDescription());
//                } catch (MalformedJwtException e) {
//                    log.error("Invalid JWT token -> Message: [{}]", e);
//                    throw new AdamException(BusinessStatusEnum.TOKEN_EXPIRED.getCode(),BusinessStatusEnum.TOKEN_EXPIRED.getDescription());
//                } catch (ExpiredJwtException e) {
//                    log.error("Expired JWT token -> Message: [{}]", e);
//                    throw new AdamException(BusinessStatusEnum.TOKEN_EXPIRED.getCode(),BusinessStatusEnum.TOKEN_EXPIRED.getDescription());
//                } catch (UnsupportedJwtException e) {
//                    log.error("Unsupported JWT token -> Message: [{}]", e);
//                    throw new AdamException(BusinessStatusEnum.TOKEN_EXPIRED.getCode(),BusinessStatusEnum.TOKEN_EXPIRED.getDescription());
//                } catch (IllegalArgumentException e) {
//                    log.error("JWT claims string is empty -> Message: [{}]", e);
//                    throw new AdamException(BusinessStatusEnum.TOKEN_EXPIRED.getCode(),BusinessStatusEnum.TOKEN_EXPIRED.getDescription());
//                }
//            }
//            return claimsJws;
//    }

    public static TokenVO createToken(TokenDTO tokenDTO){
        if(null == tokenDTO){
            return null;
        }
        String token = Jwts.builder()
                // 放入ID
                .setId(tokenDTO.getId()+"")
                // 主题
                .setSubject(tokenDTO.getSubject())
                // 签发时间
                .setIssuedAt(DefaultClock.INSTANCE.now())
                // 签发者
                .setIssuer(tokenDTO.getIssuer())
                // 自定义属性
                .setClaims(tokenDTO.getClaims())
                // 失效时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenDTO.getExpireTime()))
                // 签名算法和密钥 SignatureAlgorithm.HS512
                .signWith(tokenDTO.getSignatureAlgorithm(), tokenDTO.getSecret())
                .compact();
        TokenVO tokenVO= new TokenVO();
        tokenVO.setExpireTime(tokenDTO.getExpireTime());
        tokenVO.setToken(token);
        return tokenVO;
    }
}
