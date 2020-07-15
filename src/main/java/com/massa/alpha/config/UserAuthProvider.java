package com.massa.alpha.config;

import com.massa.alpha.data.Admin;
import com.massa.alpha.data.AdminGroupAuth;
import com.massa.alpha.data.MenuFunc;
import com.massa.alpha.data.SessionAttrName;
import com.massa.alpha.service.AdminGroupService;
import com.massa.alpha.service.AdminService;
import com.massa.alpha.service.MenuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 인증 프로바이더
 * 로그인 시 사용자가 입력한 아이디와 비밀번호를 확인하고 해당 권한을 주는 class
 */
@Component
public class UserAuthProvider implements AuthenticationProvider {
    Logger logger = LogManager.getLogger(UserAuthProvider.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminGroupService adminGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    HttpSession httpSession;

    @Override
    public Authentication authenticate(Authentication authentication) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //로그인시 사용자가 입력한 아이디와 비밀번호를 확인하고 해당 권한을 주는 클래스
        ////유저가 입력한 정보를 이이디비번으으로만든다.(로그인한 유저아이디비번정보를담는다)
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        String userId = authToken.getName();
        String userPassword = authToken.getCredentials().toString();

        Admin admin = adminService.findByAdminId(userId);

        if(admin == null) {
            throw new UsernameNotFoundException("일치하는 아이디 정보가 없습니다. 아이디를 다시 확인해 주세요");
        }
        if (!admin.getMcode().equals("1")){
            throw  new UsernameNotFoundException("이메일 인증을 완료해주세요.");
        }

        // 이용자가 로그인 폼에서 입력한 비밀번호와 DB로부터 가져온 암호화된 비밀번호를 비교한다
        //rawPassword: 암호화 되지않은 값
        //encodedPassword : 암호화된 값
        if(passwordEncoder.matches(userPassword, admin.getPassword())){
            logger.info("matches succ");
        }else{
            logger.info("matches fail");
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        List<AdminGroupAuth> adminGroupAuthList = adminGroupService.searchAdminGroupAuth(admin.getAdminGroupSeq());
        List<MenuFunc> menuFuncList = menuService.menuFuncSearch();



        this.httpSession.setAttribute(SessionAttrName.ADMIN_INFO.toString(), admin);
        this.httpSession.setAttribute(SessionAttrName.ADMIN_GROUP_AUTH.toString(), adminGroupAuthList);
        this.httpSession.setAttribute(SessionAttrName.MENU_FUNC.toString(), menuFuncList);
        /* 권한 관리를 위한 Session 처리 END */

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        String role = null;
        switch (admin.getAdminGroupSeq()){
            case 1 :
                role = "ROLE_ADMIN";
                break;
            case 2 :
                role = "ROLE_USER";
                break;
            default:
                role = "ROLE_NONE";
        }

        authorities.add(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(admin, null, authorities);
    }

    @Override
    public boolean supports(Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

