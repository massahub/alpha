package com.massa.alpha.util;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SimpleCaptchaUtil {

    @Autowired
    private HttpSession httpSession;

    public void main (HttpServletRequest req, HttpServletResponse res) {

        Captcha captcha = new Captcha.Builder(148, 48)
                .addText()
                .addNoise().addNoise().addNoise()
                .addBackground()
                .build();

        String answer = captcha.getAnswer();

        this.httpSession = req.getSession();
        this.httpSession.setAttribute("answer", answer);

        res.setHeader("Cache-Conrol", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Max-Age", 0);
        res.setContentType("image/png");
        CaptchaServletUtil.writeImage(res, captcha.getImage());

    }

} //end class
