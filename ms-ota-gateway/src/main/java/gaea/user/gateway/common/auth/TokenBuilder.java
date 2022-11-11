package gaea.user.gateway.common.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;

/**
 * 宝能汽车版权所有 © Copyright 2019 Bees360
 *
 * @Description: 默认token构造器
 * @Project: com.bnmotor.user.auth
 * @CreateDate: 2019/10/30
 * @Author: <a href="poker0325@me.com">jiankang.xia</a>
 */
@Component
public class TokenBuilder {

    public static final long  DEFAULT_TOKEN_EXPIRE_TIME  = 60 * 60 * 1000 * 24;

    public static final long  DEFAULT_REFRESH_TOKEN_EXPIRE_TIME  = DEFAULT_TOKEN_EXPIRE_TIME * 2;


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.iss}")
    private String iss;

    @Value("${jwt.unit.day}")
    private Long unitDay;

    @Value("${jwt.unit.times}")
    private Long unitTimes;

    public static String tokenSecret;

    public static String tokenIss;

    public static Long jwtUnitDay;

    public static Long jwtUnitTimes;

    public static Long expireTime;

    @PostConstruct
    private void init(){
        tokenSecret = secret;
        tokenIss = iss;
        jwtUnitDay = unitDay;
        jwtUnitTimes = unitTimes;
        expireTime = jwtUnitTimes * jwtUnitDay;
    }

    public static TokenDTO getTokenDTO(TokenDTO tokenDTO){
        if(null == tokenDTO){
            return null;
        }
        if(null != tokenDTO.getSecret()){
            tokenSecret = tokenDTO.getSecret();
        }
        if(null != tokenDTO.getIssuer()){
            tokenIss = tokenDTO.getIssuer();
        }
        if(null == tokenSecret){
            return null;
        }
        if(null == tokenIss){
            return null;
        }
        if(tokenDTO.getClaims() == null){
            tokenDTO.setClaims(new HashMap<>());
        }
        tokenDTO.setIssuedAt(new Date());
        if(null == tokenDTO.getExpireTime()){
            if(null == jwtUnitDay){
                tokenDTO.setExpireTime(DEFAULT_TOKEN_EXPIRE_TIME);
            }else{
                if(null == expireTime){
                    tokenDTO.setExpireTime(DEFAULT_TOKEN_EXPIRE_TIME);
                }else{
                    tokenDTO.setExpireTime(expireTime);
                }
            }
        }
        if(null == tokenDTO.getRefreshTokenTime()){
            if(null == jwtUnitDay){
                tokenDTO.setRefreshTokenTime(DEFAULT_REFRESH_TOKEN_EXPIRE_TIME);
            }else{
                if(null == expireTime){
                    tokenDTO.setRefreshTokenTime(DEFAULT_REFRESH_TOKEN_EXPIRE_TIME);
                }else {
                    tokenDTO.setRefreshTokenTime(expireTime);
                }
            }
        }
        tokenDTO.setSecret(tokenSecret);
        tokenDTO.setIssuer(tokenIss);
        tokenDTO.setSignatureAlgorithm(SignatureAlgorithm.HS512);
        return tokenDTO;
    }

    public static TokenDTO getDefaultTokenDTO(){
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setIssuedAt(new Date());
        tokenDTO.setExpireTime(DEFAULT_TOKEN_EXPIRE_TIME);
        tokenDTO.setRefreshTokenTime(DEFAULT_REFRESH_TOKEN_EXPIRE_TIME);
        if(null == tokenSecret){
            return null;
        }
        if(null == tokenIss){
            return null;
        }
        tokenDTO.setClaims(new HashMap<>());
        tokenDTO.setSecret(tokenSecret);
        tokenDTO.setIssuer(tokenIss);
        tokenDTO.setSignatureAlgorithm(SignatureAlgorithm.HS512);
        return tokenDTO;
    }
}
