package gaea.user.gateway.filter;

import java.util.HashMap;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import gaea.user.gateway.common.auth.TokenBuilder;
import gaea.user.gateway.common.auth.TokenDTO;
import gaea.user.gateway.common.auth.TokenUtils;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

	private static final String AUTHORIZE_TOKEN = "Authorization";
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie cookie = cookies.getFirst(AUTHORIZE_TOKEN);
        if (null == cookie || StringUtils.isEmpty(cookie.getValue())) {
        	long id = 123;
        	String phone = "15817314125";
    		//模拟token
    		TokenDTO tokenDTO = TokenBuilder.getDefaultTokenDTO();
    		tokenDTO.setId(id);
    		tokenDTO.setAudience(phone);
    		tokenDTO.setIssuer("bnmotor");
    		HashMap<String,Object> map = new HashMap<>();
    		map.put("username","test");
    		tokenDTO.setClaims(map);
    		tokenDTO.setSecret("bnmotor");
    		String token = TokenUtils.getAccessTokenAndRefreshToken(tokenDTO).getToken();
    		response.addCookie(ResponseCookie.from("Authorization",token).build());
        }
       
		return chain.filter(exchange);
	}

}
