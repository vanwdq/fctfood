package com.food.fctfood.interceptor;

import com.food.fctfood.util.JRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: LoginInterceptor
 * </p>
 * <p>
 * Description: 登陆拦截器
 * </p>
 *
 * @author libiao
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private JRedisUtil jRedisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("token: 登陆拦截");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String requestPath = request.getRequestURI();
        if (requestPath.contains("/sendEmail") ||
                requestPath.contains("/register") ||
                requestPath.contains("/swagger") ||
                requestPath.contains("/voteload") ||
                requestPath.contains("/getUserInfo") ||
                requestPath.contains("/login")) {
            return true;
        }
        String token = request.getHeader("token");
        log.debug("token: " + token);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

}
