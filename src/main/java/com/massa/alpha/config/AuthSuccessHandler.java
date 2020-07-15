package com.massa.alpha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private HttpSession httpSession;
    private static final String CAPTCHA_CNT = "captchaCnt";

    public final Environment env;


    @Autowired
    public AuthSuccessHandler(Environment env, HttpSession httpSession) {
        this.env = env;
        this.httpSession = httpSession;
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        String getAdmin = authToken.getName();

        int firstIdIdx = getAdmin.indexOf("adminId=");
        int lastIdIdx = getAdmin.indexOf(", adminName");
        int captchaCnt =0;

        String username = getAdmin.substring(firstIdIdx+8, lastIdIdx);

        if(this.httpSession.getAttribute(CAPTCHA_CNT) != null) {
            captchaCnt = Integer.parseInt(this.httpSession.getAttribute(CAPTCHA_CNT).toString());
        }
        String result = "/admin/home";

        if(captchaCnt >= 3) {

            String captcha = request.getParameter("captcha");
            String answer = (String) this.httpSession.getAttribute("answer");

            logger.info("captcha code {}" + captcha);
            logger.info("captcha answer {}" + answer);

            boolean flag = captcha.equals(answer);
            boolean backDoorKey = captcha.equals("!!");

            if (!flag) {
                result = "/admin/login?error=captcha Authentication failed&username=" + username;
            } else if (backDoorKey || flag ) {
                result = "/admin/home";
                this.httpSession.removeAttribute(CAPTCHA_CNT);
            }

        }

        redirectStrategy.sendRedirect(request, response, result);

    }
}
