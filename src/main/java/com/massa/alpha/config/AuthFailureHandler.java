package com.massa.alpha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private HttpSession httpSession;
    private static final String CAPTCHA_CNT = "captchaCnt";
    private static final String USER_ID = "userId";
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        int captchaCnt = 0;

        if(this.httpSession.getAttribute(CAPTCHA_CNT) != null) {
            captchaCnt = Integer.parseInt(this.httpSession.getAttribute(CAPTCHA_CNT).toString())+1;
            this.httpSession.setAttribute(CAPTCHA_CNT,captchaCnt);
            logger.info("captchaCnt : " + captchaCnt);
        } else {
            this.httpSession.setAttribute(CAPTCHA_CNT,"1");
            logger.info("new captchaCnt session");
        }

        String massage = exception.getMessage() != null ? exception.getMessage() : "로그인 실패!";
        request.setAttribute(USER_ID, request.getParameter(USER_ID));
        request.setAttribute(CAPTCHA_CNT, String.valueOf(captchaCnt));
        request.getRequestDispatcher("/admin/login?error="+massage+"&username"+request.getParameter(USER_ID)).forward(request, response);
    }
}
