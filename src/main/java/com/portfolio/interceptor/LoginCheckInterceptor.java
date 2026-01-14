package com.portfolio.interceptor;

import com.portfolio.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        User loginUser = (session == null) ? null :
                (User) session.getAttribute("loginUser");

        // 로그인 안 된 경우
        if (loginUser == null) {

            // AJAX 요청인지 판단
            String requestedWith = request.getHeader("X-Requested-With");
            boolean isAjax = "XMLHttpRequest".equals(requestedWith);

            if (isAjax) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.sendRedirect("/users/login");
            }
            return false;
        }

        return true; // 통과
    }
}
