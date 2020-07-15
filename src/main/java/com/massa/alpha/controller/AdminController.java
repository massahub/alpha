package com.massa.alpha.controller;

import com.massa.alpha.common.ResultMassage;
import com.massa.alpha.data.Admin;
import com.massa.alpha.data.AdminGroup;
import com.massa.alpha.data.paging.Page;
import com.massa.alpha.data.paging.PagingRequest;
import com.massa.alpha.service.AdminGroupService;
import com.massa.alpha.service.AdminService;
import com.massa.alpha.service.PageService;
import com.massa.alpha.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin/user")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final AdminGroupService adminGroupService;
    private final MessageSource messageSource;
    private final PageService pageService;

    @Autowired
    public AdminController(AdminService adminService, AdminGroupService adminGroupService, MessageSource messageSource, PageService pageService) {
        this.adminService = adminService;
        this.adminGroupService = adminGroupService;
        this.messageSource = messageSource;
        this.pageService = pageService;
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @RequestMapping(value = "/search")
    public ModelAndView searchForm(Model model){

        List<Admin> list =  adminService.searchAll();

        //list.forEach(admin -> {
        //    logger.info("admin_id:({}), admin_name:({})", admin.getAdminId(), admin.getAdminName());
        //    logger.info(admin.toString());
        //});

        ModelAndView view = new ModelAndView();

        view.addObject("listSize", list.size());
        view.addObject("lists", list);

        view.setViewName("admin/user/search");

        return view;
    }

    @RequestMapping(value = "/search.json")
    public @ResponseBody Page<Admin> searchData(Model model, @RequestBody PagingRequest pagingRequest){

        //List<List<String>> list =  adminService.datatableList();
        //ModelAndView mav = new ModelAndView();
        //List<Admin> list =  adminService.search();

        logger.debug(pagingRequest.toString());

        Page<Admin> list = (Page<Admin>) adminService.getAdmins(pagingRequest);


        return list;
    }

    @RequestMapping(value = "/search2.json")
    public @ResponseBody Map<String, Object> searchData(Model model, HttpServletRequest request){

        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!pageService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            logger.error("failed parsing jquery data-table parameter parsing");
            return null;
        }

        String search = extraValue.get("search_value");
        int start = Integer.valueOf(extraValue.get("start"), 10);
        int length = Integer.valueOf(extraValue.get("length"), 10);

        List<Map<String, Object>> list =  adminService.search(search, sortValue, start, length);

        Map<String, Object> resultMap = new HashMap<>();

        long totalCount = adminService.count(null);
        long filteredCount = adminService.count(search);

        logger.debug(request.getParameter("draw"));
        logger.debug(request.getParameter("start"));
        logger.debug(request.getParameter("length"));
        logger.debug(request.getParameter("search[value]"));
        logger.debug(request.getParameter("search[regex]"));


        resultMap.put("draw", extraValue.get("draw"));
        resultMap.put("recordsFiltered", totalCount);
        resultMap.put("recordsTotal", filteredCount);
        resultMap.put("data", list);

        return resultMap;
    }

    @RequestMapping(value = "/search3.json")
    public @ResponseBody Map<String, Object> searchData3(Model model, HttpServletRequest request, Pageable pageable){


        logger.debug(pageable.getOffset()+ "" );
        logger.debug(pageable.getPageNumber()+ "" );
        logger.debug(pageable.getPageSize()+ "" );
        logger.debug(pageable.getSort()+ "" );


        Map<String, Object> resultMap = new HashMap<>();

        long totalCount = adminService.count(null);

        List<Map<String, Object>> list =  adminService.search3(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort(), pageable.getOffset());

        resultMap.put("data", list);

        return resultMap;
    }

    @RequestMapping(value = "/create")
    public ModelAndView createForm(Model model){


        List<AdminGroup> adminGroups =  adminGroupService.search();

        ModelAndView view = new ModelAndView();
        String uuid = UUID.randomUUID().toString();
        String key =uuid.substring(0,8) + uuid.substring(9,13) + uuid.substring(14,18) + uuid.substring(19,23) + uuid.substring(24);

        logger.debug( "createForm" );

        view.addObject("adminGroups", adminGroups);
        //return "user/create";
        view.setViewName("admin/user/create");
        view.addObject("key",key);

        return view;
    }

    @RequestMapping(value = "/create.json")
    public @ResponseBody String createData(Model model, Admin admin) throws IOException, MessagingException {

        logger.debug( "createData : " + admin.toString());

        Admin temp =  adminService.findByAdminId(admin.getAdminId());
        if(temp != null) return "이미 등록된 ID 입니다. ";
        //if(admin.getAdminId() == null) return "사용자 ID가 존재 하지 않습니다.";

        logger.debug( "createData encode password : " + admin.toString());

        ResultMassage resultMassage = adminService.create(admin);

        EmailUtil.sendEmail(admin,2);

        model.addAttribute("result", "등록페이지");

        //String result = getMessage("result.success");

        return resultMassage.getMessage();
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyFrom(@RequestParam("adminId") String adminId)
    {

        List<AdminGroup> adminGroups =  adminGroupService.search();
        //Admin admin =  adminService.findByAdminSeq(admin_seq);
        Admin admin =  adminService.findByAdminId(adminId);

        logger.debug( "modifyFrom - admin :" + admin.toString());

        ModelAndView view = new ModelAndView();

        view.addObject("adminGroups", adminGroups);
        view.addObject("admin", admin);
        view.addObject("result", "수정페이지");

        view.setViewName("admin/user/modify");

        return view;

    }

    @RequestMapping(value = "/modify.json")
    public @ResponseBody String modifyData(Model model, Admin admin){

        logger.info( "modifyData : " + admin.toString());

        ResultMassage resultMassage = adminService.modify(admin);

        //String result = getMessage("result.success");

        return resultMassage.getMessage();
    }

    @RequestMapping(value = "/pwd_modify.json")
    public @ResponseBody String pwdmodifyData(@RequestParam("adminId") String adminId,
                                              @RequestParam("newPassword") String newPassword){

        ResultMassage resultMassage = adminService.pwdmodify(adminId, newPassword);

        //String result = getMessage("result.success");

        return resultMassage.getMessage();
    }

    @RequestMapping(value = "/remove.json")
    public @ResponseBody String removeData(@RequestParam("adminId") String adminId, Model model){

        Admin admin =  adminService.findByAdminId(adminId);

        logger.debug( "deleteData - admin :" + admin.toString());

        ResultMassage resultMassage = adminService.delete(admin);

        //String result = getMessage("result.success");

        return resultMassage.getMessage();
    }
}
