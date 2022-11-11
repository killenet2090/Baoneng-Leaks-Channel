package thea.vehicle.basic.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
//注入spring容器
@Component
//定义filterName 和过滤的url
@WebFilter(filterName = "iamFilter" ,urlPatterns = "/*")
public class IamFilter implements Filter{

	//@Autowired
	//private IiamService iamServiceImpl;
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
/*		String login = request.getHeader("login");
		if("login".equals(login)){
			filterChain.doFilter(servletRequest,servletResponse);
			return;
		}
		MDC.put("traceId", servletRequest.getParameter("traceId"));
		String userToken = request.getHeader("userToken");
		if(StringUtils.isEmpty(userToken)){
			throw new RuntimeException("Current user is not logged in");
		}*/

		filterChain.doFilter(servletRequest,servletResponse);
	}

}
