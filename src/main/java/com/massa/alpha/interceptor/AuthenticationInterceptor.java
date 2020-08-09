package com.massa.alpha.interceptor;

import com.massa.alpha.data.AdminGroupAuth;
import com.massa.alpha.data.MenuFunc;
import com.massa.alpha.data.SessionAttrName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean isCheckURL = false;

        String uri = request.getRequestURI();

        HttpSession httpSession = request.getSession(true);

        List<AdminGroupAuth> adminGroupAuthList = (List)httpSession.getAttribute(SessionAttrName.ADMIN_GROUP_AUTH.toString());
        List<MenuFunc> menuFuncList = (List)httpSession.getAttribute(SessionAttrName.MENU_FUNC.toString());

        httpSession.setAttribute(SessionAttrName.MENU_AUTH.toString(), "N");

        /* 메뉴 테이블에서 URL존재 여부 체크 START */
        /*for(AdminGroupAuth adminGroupAuth : adminGroupAuthList) {
            if(StringUtils.equals(adminGroupAuth.getMenu().getUrl(), uri)) {
                isCheckURL = true;

                break;
            }
        }*/
        /* 메뉴 테이블에서 URL존재 여부 체크 END */

        for (MenuFunc menuFunc : menuFuncList) {
            if (StringUtils.equals(menuFunc.getUrl(), uri)) {
                isCheckURL = true;

                this.menuAuthSet(httpSession, menuFunc.getMenuSeq(), adminGroupAuthList);

                break;
            }
        }

        if(isCheckURL) {
            logger.debug("OOO 등록 URL [{}]", uri);
        } else {
            logger.debug("XXX 미등록 URL [{}]", uri);
            response.setContentType("text/html; charset=UTF-8");

            PrintWriter printwriter = response.getWriter();
            printwriter.println("<script>alert('등록된 URL이 아닙니다.'); history.back(-1);</script>");
            printwriter.flush();
            printwriter.close();

            return false;
        }

        return true;
    }

    private void menuAuthSet(HttpSession httpSession, Integer menuSeq, List<AdminGroupAuth> adminGroupAuthList) {
        try {
            for (AdminGroupAuth adminGroupAuth : adminGroupAuthList) {
                if (adminGroupAuth.getMenuSeq().equals(menuSeq)) {
                    httpSession.setAttribute(SessionAttrName.MENU_AUTH.toString(), adminGroupAuth.getAuth());

                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}