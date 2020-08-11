package com.massa.alpha.service;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
public class CheckService {

    private static final Logger logger = LoggerFactory.getLogger(CheckService.class);

    public void loginMarket(HttpServletRequest req) throws Exception {
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
        final String LOGIN_FORM_URL = "https://seller.shopee.ph/account/signin";
        final String LOGIN_ACTION_URL = "https://github.com/session";
        final String USERNAME = "masspacer@naver.com";
        final String PASSWORD = "qndhkauddPfmf1!";

        logger.info("loginMarket invoked!");

        Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute();

        Document loginDoc = loginForm.parse();

        //logger.info((String) loginDoc.html());

        HashMap<String, String> cookies = new HashMap<>(loginForm.cookies());

        String authToken = loginDoc.select(".signin > form > div:nth-child(1) > div > div:nth-child(1) > div > div > input").attr("");

        logger.info("auth token = ", authToken);

        HashMap<String, String> formData = new HashMap<>();
        formData.put("commit", "Sign in");
        formData.put("utf8", "e2 9c 93");
        formData.put("login", USERNAME);
        formData.put("password", PASSWORD);
        formData.put("authenticity_token", authToken);

        /*Connection.Response homePage = Jsoup.connect(LOGIN_ACTION_URL)
                .cookies(cookies)
                .data(formData)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();*/

        //logger.info();
    }
}
