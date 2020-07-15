package com.massa.alpha.config;

import com.massa.alpha.interceptor.AuthenticationInterceptor;
import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String ADMIN_LOGIN = "/admin/login";
    private static final String DIRECTORY_ADMIN_ALL = "/admin/**";

    private Environment env;

    @Autowired
    public WebMvcConfig(Environment env) {

        this.env = env;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns(DIRECTORY_ADMIN_ALL)
                .excludePathPatterns("/admin/home")         //홈 페이지 예외 처리
                .excludePathPatterns("/admin/dashboard")    //대쉬보드 페이지 예외 처리
                .excludePathPatterns("/admin/menu")         //메뉴 페이지 예외 처리
                .excludePathPatterns(ADMIN_LOGIN)        //로그인 페이지 예외 처리
                .excludePathPatterns("/admin/images/*")     //이미지 디렉토리 예외 처리
        ;


        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String location = env.getProperty("resources.static.fileupload.location");
        String uriPath = env.getProperty("resources.static.fileupload.uri_path");

        registry.addResourceHandler(uriPath+"**")
                .addResourceLocations("file:///"+ location); // "file:///" 접두어를 빼먹으면 안된다,

    }

    @Bean
    public MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }

    @Bean // 세션에 지역설정. default는 KOREAN = 'ko'
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREAN);
        return slr;
    }

    @Bean // 지역설정을 변경하는 인터셉터. 요청시 파라미터에 lang 정보를 지정하면 언어가 변경됨.
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean // yml 파일을 참조하는 MessageSource 선언
    public MessageSource messageSource(
            @Value("${spring.messages.basename}") String basename,
            @Value("${spring.messages.encoding}") String encoding) {
        YamlMessageSource ms = new YamlMessageSource();

        ms.setBasename(basename);
        ms.setDefaultEncoding(encoding);
        ms.setAlwaysUseMessageFormat(true);
        ms.setUseCodeAsDefaultMessage(true);
        ms.setFallbackToSystemLocale(true);
        return ms;
    }

    private static class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}
