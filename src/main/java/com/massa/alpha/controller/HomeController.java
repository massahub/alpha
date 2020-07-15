package com.massa.alpha.controller;

import com.massa.alpha.data.Admin;
import com.massa.alpha.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/admin")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final AdminService adminService;
    private final MessageSource messageSource;

    @Autowired
    public HomeController(AdminService adminService, MessageSource messageSource) {
        this.adminService = adminService;
        this.messageSource = messageSource;
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @GetMapping(value = "")
    public ModelAndView homeRoot(Model model){

        ModelAndView mav = new ModelAndView("home");

        return mav;
    }

    @GetMapping(value = "/index")
    public ModelAndView myTest(Model model){

        ModelAndView mav = new ModelAndView("index");

        logger.debug( getMessage("error.404"));
        logger.debug( getMessage("error.500"));

        model.addAttribute("value_1", "#표시가필요합니다.");
        model.addAttribute("value_2", "$표시가필요합니다.");

        mav.addObject("message", "mobilepark2019!");

        return mav;
    }

    /**
     * 관리자 사이트 Home
     */
    @GetMapping(value = "/home")
    public ModelAndView home(Model model, Principal principal, Authentication authentication){

        ModelAndView view = new ModelAndView();

        //로그인한 사용자 정보 가져오기
        String user = principal.getName();

        logger.debug("principal.getName : " + user);

        Admin admintemp1 = (Admin) authentication.getPrincipal();
        String admin_id = admintemp1.getAdminId();

        logger.debug("getPrincipal.Admin.getAdminId : " + admin_id);

        Admin admintemp2 = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String admin_name = admintemp2.getAdminName();

        logger.debug("getPrincipal.Admin.getAdminName : " + admin_name);



        view.addObject("login_id", admin_id);
        view.addObject("login_name", admin_name);
        view.setViewName("admin/home");


        return view;
    }

    @RequestMapping("/dashboard")
    public ModelAndView dashboard()
    {
        ModelAndView mav = new ModelAndView("admin/dashboard");


        return mav;
    }

    @GetMapping("/menu")
    public ModelAndView menu() {

        ModelAndView mav = new ModelAndView("admin/menu");
        return mav;
    }


    /**
     * 로그인 화면
     * @param error ( 로그인 오류 )
     */
    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(required=false) String error,@RequestParam(required=false) String username,@RequestParam(required=false) String password, HttpServletRequest req) {

        ModelAndView mav = new ModelAndView();

        mav.addObject("error", error);
        mav.addObject("re_id", username);
        return mav;
    }

    /**
     * 로그인 인증
     */
    /*@PostMapping("/authenticate")
    public String authenticate(HttpServletRequest req) {
        logger.info("authenticate successfully");
        return "authenticate successfully";
    }*/
}
