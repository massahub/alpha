package com.massa.alpha.controller;

import com.massa.alpha.common.ResultMassage;
import com.massa.alpha.data.Admin;
import com.massa.alpha.service.AdminService;
import com.massa.alpha.util.EmailUtil;
import com.massa.alpha.util.SimpleCaptchaUtil;
import com.massa.alpha.util.ZipFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    private AdminService adminService;
    private HttpSession httpSession;

    private static final String CAPTCHA_CNT = "captchaCnt";

    @Autowired
    public CommonController(AdminService adminService, HttpSession httpSession){
        this.adminService = adminService;
        this.httpSession = httpSession;
    }


    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    String filename = null;

    /**
     * zip파일 다운로드 API
     * 지금은 파일리스트와 파일명을 메서드 안에서 세팅해서 넣지만 화면이 생기면 값을 받아와서 처리 해야 함
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/downloadZip")
    public void downloadZip(HttpServletResponse response) throws Exception{
        List<String> sourceFiles = new ArrayList<>();
        sourceFiles.add("변경계획서_20191216.docx");
        sourceFiles.add("변경계획서_20190828.docx");
        sourceFiles.add("변경계획서_20190820.docx");

        filename="MyZipFile";

        //다운받을 파일명 지정 안할 시 디폴트명으로 다운로드
        ZipFileUtil.downloadZip(response, sourceFiles, filename);

    }

    @GetMapping("/simpleCaptcha")
    public @ResponseBody void captcha(HttpServletRequest req, HttpServletResponse res) {

        int captchaCnt = 0;

        if(this.httpSession.getAttribute(CAPTCHA_CNT) != null) {
            captchaCnt = Integer.parseInt(this.httpSession.getAttribute(CAPTCHA_CNT).toString());

            logger.info("captchaCnt : {}", captchaCnt);
        }

        if(captchaCnt >= 3){

            SimpleCaptchaUtil simpleCaptchaUtil = new SimpleCaptchaUtil();

            simpleCaptchaUtil.main(req, res);

            logger.info("answer {}", httpSession.getAttribute("answer"));
        }

    }

    @PostMapping("/captchaCnt")
    public @ResponseBody Map<String, String> captchaCnt() {

        int captchaCnt = 0;
        Map<String, String> map = new HashMap<>();

        if(this.httpSession.getAttribute(CAPTCHA_CNT) != null) {
            captchaCnt = Integer.parseInt(this.httpSession.getAttribute(CAPTCHA_CNT).toString());

            logger.info("captchaCnt {}", captchaCnt);

        }

        map.put(CAPTCHA_CNT,String.valueOf(captchaCnt));

        return map;
    }

    @PostMapping("/closing")
    public void closing (HttpServletRequest req) {

        if(req.getParameter("close").equals("close")) {
            this.httpSession.invalidate();
        }
    }

    @RequestMapping(value="/findidpwd", method = RequestMethod.GET)
    public ModelAndView find(HttpServletRequest req){
        ModelAndView mav = new ModelAndView("admin/find");
        return mav;
    }

    @RequestMapping(value="/isuser",method = RequestMethod.GET)
    @ResponseBody
    public int isuser(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "user_id",defaultValue = "") String userid,
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value="email", defaultValue = "") String email)
    {
        if(!userid.isEmpty()){
            //비밀번호 찾기
            try{
                adminService.isuser_id(userid);
                return 1;
            }catch (Exception e){
                return 0;
            }
        }else{
            //아이디 찾기
            try {
                return (int)adminService.isuser_pnemail(phone,name,email);
            }catch (Exception e){
                return 0;
            }
        }
    }

    @RequestMapping(value="/findpwd", method=RequestMethod.POST)
    public String findpwd(HttpServletRequest req, HttpServletResponse res) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/login");
        String admin_id = req.getParameter("user_id");
        String pwd = "";

        char[] arr = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
                ,'0','1','2','3','4','5','6','7','8','9','!','@','#','$','%','^','&','*'};

        for(int i=0; i<8; i++){
            pwd+=arr[(int)(Math.random()*44)];
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encPassword = passwordEncoder.encode(pwd);

        try {
            adminService.temp_pwd(admin_id,encPassword);
            Admin admin = adminService.findByAdminId(admin_id);
            admin.setPassword(pwd);
            return EmailUtil.sendEmail(admin,1).getMessage();
        }catch (Exception e){
            return ResultMassage.FAIL_TEMP_PWD.getMessage();
        }
    }

    @RequestMapping(value="/findid", method=RequestMethod.GET)
    public String findid(HttpServletRequest req) throws Exception {
        String phone = req.getParameter("phone");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String id = "";
        List<Admin> admin = adminService.findid(phone, name,email);
        for( int i=0; i<admin.size(); i++ ){
            id+=admin.get(i).getAdminId()+"/";
        }
        return id;
    }

//    @RequestMapping(value = "/checkemail/{admin_id}/{mcode}", method = RequestMethod.GET)
//    public ModelAndView getUserByEmail(HttpServletResponse res,
//                                       @PathVariable("admin_id") String admin_id,
//                                       @PathVariable("mcode") String mcode) throws IOException {
//        ModelAndView mav = new ModelAndView("/admin/login");
//
//        Admin admin = adminService.findByAdminId(admin_id);
//        if(admin.getMcode().equals(mcode))
//            adminService.check_email(mcode);
//
//        res.setCharacterEncoding("utf-8");
//        res.setContentType("text/html; charset=utf-8");
//
//        PrintWriter writer = res.getWriter();
//        if(admin.getMcode().equals(mcode)) {
//            if (adminService.check_email(mcode) == 1) {
//                writer.println("<script language='javascript'>" +
//                        "alert('이메일 인증이 완료되었습니다.');" +
//                        "</script>");
//            } else {
//                writer.println("<script language='javascript'>" +
//                        "alert('이메일 인증에 실패하었습니다.');" +
//                        "</script>");
//            }
//        }else{
//            writer.println("<script language='javascript'>" +
//                    "alert('잘못된 인증주소 입니다.');" +
//                    "</script>");
//        }
//        writer.flush();
//
//        return mav;
//    }



    @RequestMapping(value = "/checkemail", method = RequestMethod.POST)
    public ModelAndView getUserByEmail(HttpServletResponse res, HttpServletRequest req) throws IOException {
        ModelAndView mav = new ModelAndView("/admin/login");

        String admin_id = req.getParameter("admin_id");
        String mcode = req.getParameter("mcode");

        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html; charset=utf-8");

        PrintWriter writer = res.getWriter();

        try {
            Admin admin = adminService.isuser_id(admin_id);
            if(admin.getMcode().equals(mcode)){
                adminService.succ_email(mcode);
                writer.println("<script language='javascript'>" +
                        "alert('이메일 인증이 완료되었습니다.');" +
                        "</script>");
            }
        }catch(Exception e){
            writer.println("<script language='javascript'>" +
                    "alert('잘못된 인증주소 입니다.');" +
                    "</script>");
        }
        writer.flush();

        return mav;
    }
}