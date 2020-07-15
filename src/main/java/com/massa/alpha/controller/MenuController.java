package com.massa.alpha.controller;

import com.massa.alpha.common.ResultMassage;
import com.massa.alpha.data.Admin;
import com.massa.alpha.data.Menu;
import com.massa.alpha.data.MenuFunc;
import com.massa.alpha.data.SessionAttrName;
import com.massa.alpha.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/menu")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    public MessageSource messageSource;

    @Autowired
    public MenuService menuService;

    /**
     * 메뉴 관리 화면
     */
    @RequestMapping(value="/search", method=RequestMethod.GET)
    public ModelAndView search(HttpSession session, HttpServletRequest request) {
        return new ModelAndView("admin/menu/search");
    }

    /**
     * 메뉴 상세 화면
     */
    @RequestMapping(value="/view", method=RequestMethod.GET)
    public ModelAndView view(@RequestParam("menuSeq") Integer menuSeq) {
        ModelAndView mav = new ModelAndView("admin/menu/view");

        try {
            Menu menu = menuService.view(menuSeq);

            List<MenuFunc> menuFuncList = menuService.menuFuncSearch(menuSeq);

            mav.addObject("menu", menu);
            mav.addObject("menuFuncList", menuFuncList);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return mav;
    }

    /**
     * 메뉴 수정 화면
     */
    @RequestMapping(value="/modify", method=RequestMethod.GET)
    public ModelAndView modifyForm(@RequestParam("menuSeq") Integer menuSeq) {
        ModelAndView mav = new ModelAndView("admin/menu/modify");

        try {
            Menu menu = menuService.view(menuSeq);

            mav.addObject("menu", menu);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return mav;
    }

    /**
     * 메뉴 기능 생성 화면
     */
    @RequestMapping(value="/func/create", method=RequestMethod.GET)
    public ModelAndView funcCreateForm(@RequestParam("menuSeq") Integer menuSeq) {
        ModelAndView mav = new ModelAndView("admin/menu/func/create");

        mav.addObject("menuSeq", menuSeq);
        mav.addObject("menuFunc", new MenuFunc());

        return mav;
    }

    /**
     * 메뉴 기능 상세 화면
     */
    @RequestMapping(value="/func/view", method=RequestMethod.GET)
    public ModelAndView viewFunc(@RequestParam("funcSeq") Integer funcSeq) {
        ModelAndView mav = new ModelAndView("admin/menu/func/view");

        try {
            MenuFunc menuFunc = menuService.viewFunc(funcSeq);

            mav.addObject("menuFunc", menuFunc);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return mav;
    }

    /**
     * 메뉴 기능 수정 화면
     */
    @RequestMapping(value="/func/modify", method=RequestMethod.GET)
    public ModelAndView modifyFunc(@RequestParam("funcSeq") Integer funcSeq) {
        ModelAndView mav = new ModelAndView("admin/menu/func/modify");

        try {
            MenuFunc menuFunc = menuService.viewFunc(funcSeq);

            mav.addObject("menuFunc", menuFunc);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return mav;
    }

    /**
     * 메뉴 조회
     */
    @RequestMapping(value = "/getRoot.json", method=RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getRoot() {
        List<Map<String, Object>> menuList = new ArrayList<>();

        try {
            menuList = menuService.getRoot();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return menuList;
    }

    /**
     * 사용자별 인증 메뉴 조회
     */
    @PostMapping(value = "/adminAuthMenu.json")
    @ResponseBody
    public List<Map<String, Object>> adminAuthMenu(HttpSession httpSession) {
        List<Map<String, Object>> menuList = new ArrayList<>();

        try {
            Admin admin = (Admin)httpSession.getAttribute(SessionAttrName.ADMIN_INFO.toString());

            menuList = menuService.adminAuthMenu(admin.getAdminGroupSeq());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return menuList;
    }

    /**
     * 메뉴 전체 수정 ( 추가 / 메뉴명변경 / 삭제 )
     */
    @RequestMapping(value="/orderChange.json", method=RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> orderChange(@RequestBody List<Map<String, Object>> menuArray) {
        List<Map<String, Object>> menuList = new ArrayList<>();

        try {
            menuList = menuService.orderChange(menuArray);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return menuList;
    }

    /**
     * 메뉴 정보 수정
     */
    @RequestMapping(value="/modify.json", method=RequestMethod.POST)
    public ModelAndView modifyJson(Menu inputMenu) {
        try {
            menuService.modify(inputMenu);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return view(inputMenu.getMenuSeq());
    }

    /**
     * 메뉴 기능 생성
     */
    @RequestMapping(value="/func/create.json", method=RequestMethod.POST)
    @ResponseBody
    public String createFunc(MenuFunc inputMenuFunc) {
        try {
            ResultMassage resultMassage = menuService.createFunc(inputMenuFunc);

            return resultMassage.getMessage();
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return ResultMassage.FAIL.getMessage();
    }

    /**
     * 메뉴 기능 수정
     */
    @RequestMapping(value="/func/modify.json", method=RequestMethod.POST)
    @ResponseBody
    public String modifyFunc(MenuFunc inputMenuFunc) {
        try {
            ResultMassage resultMassage = menuService.modifyFunc(inputMenuFunc);

            return resultMassage.getMessage();
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return ResultMassage.FAIL.getMessage();
    }

    /**
     * 메뉴 기능 삭제
     */
    @RequestMapping(value="/func/remove.json", method=RequestMethod.POST)
    public ModelAndView removeFunc(@RequestParam("funcSeq") Integer funcSeq,
                                   @RequestParam("menuSeq") Integer menuSeq) {
        try {
            menuService.removeFunc(funcSeq);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return view(menuSeq);
    }
}
