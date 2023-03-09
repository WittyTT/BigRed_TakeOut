package TT.Filter;

import TT.Common.BaseContext;
import TT.Common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new  AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        String RequestURI=request.getRequestURI();
        log.info("拦截到请求：{}",request.getRequestURI());
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(urls, RequestURI);
        if(check) {
            log.info("请求：{}不需处理",request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，id为：{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }


        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，id为：{}",request.getSession().getAttribute("user"));
            Long userId= (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("用户未登录");
        return;



//        log.info("拦截到请求：{}",request.getRequestURI());

    }
    public boolean check(String[]urls, String RequestURI){
        for (String s : urls) {
            boolean match = PATH_MATCHER.match(s, RequestURI);
            if(match)
                return true;
        }
        return false;

    }
}
