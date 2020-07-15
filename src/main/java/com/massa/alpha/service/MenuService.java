package com.massa.alpha.service;

import com.massa.alpha.common.ResultMassage;
import com.massa.alpha.data.AdminGroupAuth;
import com.massa.alpha.data.Menu;
import com.massa.alpha.data.MenuFunc;
import com.massa.alpha.repository.AdminGroupAuthRepository;
import com.massa.alpha.repository.MenuFuncRepository;
import com.massa.alpha.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
public class MenuService {
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    public MenuRepository menuRepository;

    @Autowired
    public MenuFuncRepository menuFuncRepository;

    @Autowired
    public AdminGroupAuthRepository adminGroupAuthRepository;

    /**
     * 메뉴 관리에서 사용될 메뉴 목록 조회
     * @return
     */
    public List<Map<String, Object>> getRoot() {
        List<Map<String, Object>> menuList = new ArrayList<>();
        Map<String, Object> menuMap = null;

        try {
            List<Menu> selectMenuList = menuRepository.findAll(new Sort(Sort.Direction.ASC, "sort"));

            for (Menu menu : selectMenuList) {
                menuMap = new HashMap<String, Object>();

                menuMap.put("id", menu.getMenuSeq());

                if(menu.getParentSeq() == 0) {
                    menuMap.put("parent", "#");
                } else {
                    menuMap.put("parent", menu.getParentSeq());
                }

                menuMap.put("text", menu.getTitle());
                menuMap.put("type", menu.getType());
                menuMap.put("url", menu.getUrl());

                menuList.add(menuMap);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return menuList;
    }

    /**
     * 권한 관리에서 사용될 메뉴 목록 조회
     * @return
     */
    public List<Menu> getMenuList() {
        try {
            this.requestMappingCheck("");

            List<Menu> menuList = menuRepository.findByParentSeqNot(0, new Sort(Sort.Direction.ASC, "sort"));

            return menuList;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * 권한 관리에서 사용될 메뉴 목록 조회
     * @return
     */
    public List<Map<String, Object>> adminAuthMenu(Integer adminGroupSeq) {
        List<Map<String, Object>> menuList = new ArrayList<>();
        Map<String, Object> menuMap = null;

        try {
            List<AdminGroupAuth> adminGroupAuthList = adminGroupAuthRepository.findByAdminGroupSeq(adminGroupSeq, new Sort(Sort.Direction.ASC, "menu.sort"));

            for (AdminGroupAuth adminGroupAuth : adminGroupAuthList) {
                menuMap = new HashMap<>();
/*
                if(adminGroupAuth.getMenu().getParentSeq() == 0) {
                    menuMap.put("id", adminGroupAuth.getMenu().getMenuSeq());

                    if(adminGroupAuth.getMenu().getParentSeq() == 0) {
                        menuMap.put("parent", "#");
                    } else {
                        menuMap.put("parent", adminGroupAuth.getMenu().getParentSeq());
                    }

                    menuMap.put("text", adminGroupAuth.getMenu().getTitle());
                    menuMap.put("type", adminGroupAuth.getMenu().getType());
                    menuMap.put("url", adminGroupAuth.getMenu().getUrl());

                    menuList.add(menuMap);

                    continue;
                }
*/
                if(adminGroupAuth.getAuth().indexOf("R") > -1) {
                    menuMap.put("id", adminGroupAuth.getMenu().getMenuSeq());

                    if (adminGroupAuth.getMenu().getParentSeq() == 0) {
                        menuMap.put("parent", "#");
                    } else {
                        menuMap.put("parent", adminGroupAuth.getMenu().getParentSeq());
                    }

                    menuMap.put("text", adminGroupAuth.getMenu().getTitle());
                    menuMap.put("type", adminGroupAuth.getMenu().getType());
                    menuMap.put("url", adminGroupAuth.getMenu().getUrl());

                    menuList.add(menuMap);
                }
            }

            return menuList;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public Menu view(Integer id) {
        try {
            Optional<Menu> optionalMenu = menuRepository.findById(id);

            if (!optionalMenu.isPresent()) {
                return null;
            }

            return optionalMenu.get();
        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public void modify(Menu inputMenu) {
        try {
            Optional<Menu> optionalMenu = menuRepository.findById(inputMenu.getMenuSeq());

            if(optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();
                menu.setTitle(inputMenu.getTitle());
                menu.setType(inputMenu.getType());
                menu.setUrl(inputMenu.getUrl());
                menu.setMenuIcon(inputMenu.getMenuIcon());
                menu.setDescription(inputMenu.getDescription());
                menu.setModDate(new Date());

                menuRepository.save(menu);
            } else {
                logger.info("No menu information");
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    public List<Map<String, Object>> orderChange(List<Map<String, Object>> menuArray) {
        List<Map<String, Object>> menuList = new ArrayList<>();
        Map<String, Object> menuMap = null;
        Menu menu = null;

        try {
            List<Menu> selectMenuList = menuRepository.findAll(new Sort(Sort.Direction.ASC, "sort"));

            if(selectMenuList.size() > menuArray.size()) {
                for(int i=0; i<selectMenuList.size(); i++) {
                    menu = selectMenuList.get(i);

                    this.removeMenu(menu, menuArray);
                }
            }

            for(int i=0; i<menuArray.size(); i++) {
                menuMap = menuArray.get(i);

                if (StringUtils.equals("#", menuMap.get("parent").toString())) {
                    menuMap.put("parent", "0");
                }

                if(StringUtils.equals("fa fa-folder", menuMap.get("icon").toString())) {
                    menuMap.put("type", "DIRECTORY");
                } else {
                    menuMap.put("type", "LEAF");
                }

                boolean isNumber = this.isNumberic(menuMap.get("id").toString());

                if(isNumber) {
                    menu = this.view(Integer.parseInt(menuMap.get("id").toString()));
                    menu.setMenuSeq(Integer.parseInt(menuMap.get("id").toString()));
                    menu.setParentSeq(Integer.parseInt(menuMap.get("parent").toString()));
                    menu.setTitle(menuMap.get("text").toString());
                    menu.setType(menuMap.get("type").toString());
                    menu.setSort((i+1));
                    menu.setModDate(new Date());
                } else {
                    menu = new Menu();
                    menu.setParentSeq(Integer.parseInt(menuMap.get("parent").toString()));
                    menu.setTitle(menuMap.get("text").toString());
                    menu.setType(menuMap.get("type").toString());
                    menu.setSort((i+1));
                    menu.setRegDate(new Date());
                    menu.setModDate(new Date());
                }

                menuRepository.save(menu);
            }

            menuList = this.getRoot();
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return menuList;
    }

    public List<MenuFunc> menuFuncSearch() {
        List<MenuFunc> menuFuncList = new ArrayList<>();
        try {
            menuFuncList = menuFuncRepository.findAll();
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return menuFuncList;
    }

    public List<MenuFunc> menuFuncSearch(int menuSeq) {
        List<MenuFunc> menuFuncList = new ArrayList<>();
        try {
            menuFuncList = menuFuncRepository.findByMenuSeq(menuSeq, new Sort(Sort.Direction.DESC, "regDate"));
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return menuFuncList;
    }

    public ResultMassage createFunc(MenuFunc inputMenuFunc) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            inputMenuFunc.setRegDate(new Date());
            inputMenuFunc.setModDate(new Date());

            if(!this.requestMappingCheck(inputMenuFunc.getUrl())) {
                return ResultMassage.FAIL_MENU_URL;
            }

            menuFuncRepository.save(inputMenuFunc);

            return ResultMassage.SUCCESS;
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResultMassage.FAIL;
        }
    }

    public MenuFunc viewFunc(int funcSeq) {
        try {
            Optional<MenuFunc> optionalMenuFunc = menuFuncRepository.findById(funcSeq);

            if (!optionalMenuFunc.isPresent()) {
                return null;
            }

            return optionalMenuFunc.get();
        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public ResultMassage modifyFunc(MenuFunc inputMenuFunc) {
        try {
            if(!this.requestMappingCheck(inputMenuFunc.getUrl())) {
                return ResultMassage.FAIL_MENU_URL;
            }

            MenuFunc selectMenuFunc = this.viewFunc(inputMenuFunc.getFuncSeq());

            inputMenuFunc.setRegDate(selectMenuFunc.getRegDate());
            inputMenuFunc.setModDate(new Date());

            menuFuncRepository.save(inputMenuFunc);

            return ResultMassage.SUCCESS;
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResultMassage.FAIL;
        }
    }

    public void removeFunc(int funcSeq) {
        try {
            menuFuncRepository.deleteById(funcSeq);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void removeMenu(Menu menu, List<Map<String, Object>> menuArray) {
        try {
            Map<String, Object> treeMenu = null;
            boolean isExist = false;

            for(int i=0; i<menuArray.size(); i++) {
                treeMenu = menuArray.get(i);

                if(StringUtils.equals(String.valueOf(menu.getMenuSeq()), treeMenu.get("id"))) {
                    isExist = true;
                    break;
                }
            }

            if(!isExist) {
                menuRepository.deleteById(menu.getMenuSeq());
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    private boolean isNumberic(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    private boolean requestMappingCheck(String requestUrl) {
        String requestMappingUrl = "";
        boolean bool = false;

        try {
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
                RequestMappingInfo key = entry.getKey();

                requestMappingUrl = key.getPatternsCondition().getPatterns().toArray()[0].toString();

                if(StringUtils.equals(requestUrl, requestMappingUrl)) {
                    bool = true;
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return bool;

    }
}
