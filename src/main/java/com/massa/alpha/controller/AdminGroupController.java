package com.massa.alpha.controller;

import com.massa.alpha.data.AdminGroup;
import com.massa.alpha.data.AdminGroupAuth;
import com.massa.alpha.service.AdminGroupService;
import com.massa.alpha.service.MenuService;
import com.massa.alpha.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/group")
public class AdminGroupController {
    private static final Logger logger = LoggerFactory.getLogger(AdminGroupController.class);

    @Autowired
    public AdminGroupService adminGroupService;

    @Autowired
    public MenuService menuService;

    /**
     * 그룹 목록 화면
     */
    @RequestMapping(value="/search", method=RequestMethod.GET)
    public ModelAndView search() {
        return new ModelAndView("admin/group/search");
    }

    /**
     * 그룹 생성 화면
     */
    @RequestMapping(value="/create", method=RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("admin/group/create");

        try {
            List<AdminGroupAuth> menuAuthList = adminGroupService.getMenuAuthList(0);

            mav.addObject("menuAuthList", menuAuthList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return mav;
    }

    /**
     * 그룹 상세 화면
     */
    @RequestMapping(value="/view", method=RequestMethod.GET)
    public ModelAndView view(@RequestParam("adminGroupSeq") Integer adminGroupSeq) {
        ModelAndView mav = new ModelAndView("admin/group/view");

        try {
            AdminGroup adminGroup = adminGroupService.view(adminGroupSeq);

            List<AdminGroupAuth> menuAuthList = adminGroupService.getMenuAuthList(adminGroupSeq);

            mav.addObject("adminGroup", adminGroup);
            mav.addObject("menuAuthList", menuAuthList);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return mav;
    }

    /**
     * 그룹 수정 화면
     */
    @RequestMapping(value="/modify", method=RequestMethod.GET)
    public ModelAndView modify(@RequestParam("adminGroupSeq") Integer adminGroupSeq) {
        ModelAndView mav = new ModelAndView("admin/group/modify");

        try {
            AdminGroup adminGroup = adminGroupService.view(adminGroupSeq);

            List<AdminGroupAuth> menuAuthList = adminGroupService.getMenuAuthList(adminGroupSeq);

            mav.addObject("adminGroup", adminGroup);
            mav.addObject("menuAuthList", menuAuthList);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return mav;
    }

    /**
     * 그룹 목록 조회
     */
    @RequestMapping(value="/search.json", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<List<String>> datatable = new ArrayList<>();
        List<String> row;

        try {
            List<AdminGroup> adminGroupList = adminGroupService.search();

            for(AdminGroup adminGroup : adminGroupList) {
                row = new ArrayList<>();

                row.add(adminGroup.getAdminGroupSeq().toString());
                row.add(adminGroup.getName());
                row.add(adminGroup.getDescription());
                row.add(DateUtil.dateToString(adminGroup.getRegDate(), "yyyy-MM-dd HH:MM"));
                row.add("");

                datatable.add(row);
            }

            map.put("draw", request.getParameter("draw"));
            map.put("recordsTotal", adminGroupList.size());
            map.put("recordsFiltered", adminGroupList.size());
            map.put("data", datatable);

        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return map;
    }

    /**
     * 그룹 생성
     */
    @RequestMapping(value="/create.json", method=RequestMethod.POST)
    public ModelAndView create(@RequestParam("name") String name,
                               @RequestParam("description") String description,
                               @RequestParam("arrayAuth") String auth) {
        AdminGroup adminGroup = new AdminGroup();

        try {
            adminGroup = adminGroupService.create(name, description, auth);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return this.view(adminGroup.getAdminGroupSeq());
    }

    /**
     * 그룹 수정
     */
    @RequestMapping(value="/modify.json", method=RequestMethod.POST)
    public ModelAndView remove(@RequestParam("adminGroupSeq") Integer adminGroupSeq,
                               @RequestParam("name") String name,
                               @RequestParam("description") String description,
                               @RequestParam("arrayAuth") String auth) {
        AdminGroup adminGroup = new AdminGroup();

        try {
            adminGroup = adminGroupService.modify(adminGroupSeq, name, description, auth);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return this.view(adminGroup.getAdminGroupSeq());
    }

    /**
     * 그룹 삭제
     */
    @RequestMapping(value="/remove.json", method=RequestMethod.POST)
    public ModelAndView remove(@RequestParam("adminGroupSeq") Integer adminGroupSeq) {
        try {
            adminGroupService.remove(adminGroupSeq);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return this.search();
    }

}
