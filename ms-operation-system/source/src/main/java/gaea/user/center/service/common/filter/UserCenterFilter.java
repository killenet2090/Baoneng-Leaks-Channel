package gaea.user.center.service.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.redis.IRedisProvider;
import gaea.user.center.service.common.CurrentUser;
import gaea.user.center.service.common.CurrentUserUtils;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

//注入spring容器
@Component
@Slf4j
public class UserCenterFilter implements Filter{

	@Autowired
	private IRedisProvider redisProvider;
	/**
	 * token名称
	 */
	private static final String AUTHORIZE_TOKEN = "token";
	/**
	 * 用户信息key的前缀
	 */
	private static final String USER_PREFIX = "system:user:info:";

	@SneakyThrows
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		try {
			HttpServletRequest request = (HttpServletRequest)servletRequest;
			String uri = request.getRequestURI();
			if(!(uri.contains("/user/login")
					||uri.contains("/user/logout")
					||uri.contains("swagger")
					||uri.contains("/doc.html")
					||uri.contains("/webjars/bycdao-ui")
					||uri.contains("/api-docs")
					||uri.contains("/actuator"))){
				String userId = request.getHeader("userId");
				String userName = request.getHeader("userName");
				log.info("当前登录的用户名:[{}]，用户ID : [ {} ]", userName,userId);
				if(!StringUtils.isEmpty(userId)) {
					Map userInfo = redisProvider.getObject(USER_PREFIX + userId, Map.class);
					if (null == userInfo) {
						throw new AdamException("A0303", BusinessStatusEnum.TOKEN_EXPIRED.getDescription());
					}
					CurrentUser currentUser = new CurrentUser();
					currentUser.setUserId(Long.valueOf(String.valueOf(userInfo.get("userId"))));
					currentUser.setUserName(String.valueOf(userInfo.get("userName")));
					CurrentUserUtils.setCurrentUser(currentUser);
				}
			}
		}catch (AdamException e){
			log.error("鉴权失败");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			JSONObject res = new JSONObject();
			res.put("respCode","A0303");
			res.put("respMsg", "鉴权失败！");
			res.put("respTime", System.currentTimeMillis() / 1000L);
			out.append(res.toString());
			return ;
		}
		filterChain.doFilter(servletRequest,servletResponse);
	}
}
