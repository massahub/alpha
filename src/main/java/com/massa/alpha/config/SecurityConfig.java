package com.massa.alpha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private UserAuthProvider userAuthProvider;
    private AuthSuccessHandler authSuccessHandler;
    private AuthFailureHandler authFailureHandler;
    private Environment env;
    private static final String LOGIN_PATH = "/admin/login";
    @Autowired
    public SecurityConfig(UserAuthProvider userAuthProvider,
                          AuthSuccessHandler authSuccessHandler,
                          AuthFailureHandler authFailureHandler, Environment env) {
        this.userAuthProvider = userAuthProvider;
        this.authSuccessHandler = authSuccessHandler;
        this.authFailureHandler = authFailureHandler;
        this.env = env;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api", "/jpa", "/mybatis", "common/", "images/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(10) //session 허용 갯수
                .maxSessionsPreventsLogin(true) //동일한 사용자 로그인시 x, false 일 경우 기존 사용자 session 종료
                .expiredUrl(LOGIN_PATH) //session 만료시 이동 페이지
                .sessionRegistry(sessionRegistry());

        http.authorizeRequests()
                //.antMatchers("/", "/admin/login", "/api", "/jpa", "/mybatis").permitAll() //permitAll 무조건 접근 허용
                //.antMatchers("/captcha/**").permitAll()
                .antMatchers("/admin/menu/**", "/admin/**").authenticated() // 인증된 사용자의 접근을 허용
                .and();

        http.formLogin()
                .loginPage(LOGIN_PATH) //기본 로그인 페이지  url
                .loginProcessingUrl("/admin/authenticate") //Post로 로그인을 처리할 Url
                //.failureUrl("/admin/login?error=true") // default
                //.defaultSuccessUrl("/admin/home", true)
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .usernameParameter("userId")
                .passwordParameter("userPassword")
                .permitAll()
                .and();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                //.logoutUrl("/logout")
                .logoutSuccessUrl(LOGIN_PATH)
                .invalidateHttpSession(true) //로그아웃시 세션 제거
                .deleteCookies("JSESSIONID") //쿠키 제거
                .clearAuthentication(true) //권한정보 제거
                .permitAll()
                .and();


        http.csrf().disable(); // csrf 토큰 필수로 되어 있으나 현재는 off
        http.authenticationProvider(userAuthProvider);
    }
    //실제 인증을 진행할 Provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthProvider);
    }

    @Bean
    public SessionRegistry sessionRegistry() { // Register HttpSessionEventPublisher
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
        //상위 처럼 설정하면 logout시 spring security 내부적으로 session을 삭제처리 된다.
    }
}
