package com.massa.alpha.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class SessionListener implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.debug("Session create");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.debug("Session out");
    }
}
