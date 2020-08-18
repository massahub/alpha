package com.massa.alpha.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class MarginService {

    private static final Logger logger = LoggerFactory.getLogger(MarginService.class);

    public String marginInfo(HttpServletRequest req) throws Exception {

        String buyPrice = req.getParameter("buyPrice");
        String sellPrice = req.getParameter("sellPrice");
        String weight = req.getParameter("weight");

        Map<String, Integer> globalExBuy = new HashMap<>();
        Map<String, Integer> globalExSell = new HashMap<>();

        String result = exchangeInfo(buyPrice);

        return "";
    }

    public String exchangeInfo(String Price) throws Exception {
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";

        String URL = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query="+Price+"krw+php";

        logger.info("URL : " + URL);

        Document doc = Jsoup.connect(URL).get();

        //logger.info(doc.toString());

        //Elements elements = doc.select("#ds_to_money").val();

        String value = doc.select("#ds_to_money").val();

        System.out.println("value : " + value);

        /*HashMap<String, String> formData = new HashMap<>();
        formData.put("commit", "Sign in");
        formData.put("utf8", "e2 9c 93");
        formData.put("login", USERNAME);
        formData.put("password", PASSWORD);
        formData.put("authenticity_token", authToken);
        */
        /*Connection.Response homePage = Jsoup.connect(LOGIN_ACTION_URL)
                .cookies(cookies)
                .data(formData)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();*/

        //logger.info();

        return value;
    }
}
