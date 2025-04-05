package com.example.jspwithvue.config;

import com.github.lifus.wro4j_runtime_taglib.servlet.TaglibServletContextListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.isdc.wro.http.WroContextFilter;
import ro.isdc.wro.http.WroFilter;
import ro.isdc.wro.http.WroServletContextListener;

import java.util.Arrays;

@Configuration
public class WebConfig {

    // wro4j 서블릿 리스너
    @Bean
    public ServletListenerRegistrationBean<WroServletContextListener> wroServletContextListener() {
        return new ServletListenerRegistrationBean<>(new WroServletContextListener());
    }

    // wro4j 태그 라이브러리 리스너
    @Bean
    public ServletListenerRegistrationBean<TaglibServletContextListener> taglibServletContextListener() {
        return new ServletListenerRegistrationBean<>(new TaglibServletContextListener());
    }

    // wro4j 내부 컨텍스트 관련 필터
    @Bean
    public FilterRegistrationBean<WroContextFilter> wro4jContextFilter() {
        FilterRegistrationBean<WroContextFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new WroContextFilter());
        filterRegBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegBean;
    }


    // 정적 리소스를 최적화하는 필터
    @Bean
    public FilterRegistrationBean<WroFilter> wro4jFilter() {
        FilterRegistrationBean<WroFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new WroFilter());
        filterRegBean.setUrlPatterns(Arrays.asList("/assets/wro/*")); // 리소스 최적화하는 요청 URL 패턴
        return filterRegBean;
    }

}
