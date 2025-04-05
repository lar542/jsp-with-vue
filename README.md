## JSP에서 Vue 사용하기
다른 라이브러리를 사용함을 가정하여 `wro4j`를 사용하며, `vue3-sfc-loader`를 이용해 빌드 없이 Vue 파일을 로드할 수 있다.

1. JSP 경로를 지정하기 위해 View Resolver 설정
```java
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
	
	@Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
      registry.jsp("/WEB-INF/views/", ".jsp");
  }
...
```
2. Spring Boot 내장 톰캣은 JSP 렌더링을 기본적으로 지원하지 않기 때문에 JSP 관련 의존성 추가
```xml
<!-- JSP를 렌더링하기 위한 톰캣 라이브러리 -->
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
</dependency>

<!-- JSP가 EL 사용 가능하도록 설정 -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>
```
3. `wro4j` 의존성 추가<br>
`wro4j` : 리소스 파일(JS, CSS)을 최적화(병합, 압축)하는 오픈소스 라이브러리
```xml
<!-- wro4j core 라이브러리 -->
<dependency>
    <groupId>ro.isdc.wro4j</groupId>
    <artifactId>wro4j-core</artifactId>
    <version>1.9.0</version>
</dependency>
<!-- wro4j 태그 라이브러리 -->
<dependency>
    <groupId>com.github.lifus</groupId>
    <artifactId>wro4j-runtime-taglib</artifactId>
    <version>0.2.0</version>
</dependency>
```
4. `wro4j`에서 그룹화할 리소스들을 정의하는 `wro.xml` 설정 파일 추가. `src/msin/webapp/WEB-INF` 아래에 추가한다.<br>
`vue3-sfc-loader.js` : 번들링 없이 Vue 파일을 동적으로 로드해서 실행할 수 있게 해주는 라이브러리
```xml
<groups xmlns="http://www.isdc.ro/wro">
    <group name="vue">
        <js>/assets/dev/vue/vue-3.2.37.js</js>
        <js>/assets/dev/vue/vue3-sfc-loader.js</js>
    </group>
</groups>
```
5. 리소스 최적화 관련 설정 파일인 `wro.properties` 파일은 `wro.xml`과 같은 위치에 추가한다.
```xml
resourceWatcherUpdatePeriod=1
debug=true
disableCache=true
ignoreMissingResources=true
jmxEnabled=false
parallelPreprocessing=false
hashStrategy=MD5
namingStrategy=hashEncoder
optimizedResourcesRootStrategy=predefined
optimizedResourcesRoot=/assets/wro/
...
```
6. 가이드에 따라 설정 파일에 리스너와 필터 설정 추가
```java
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
```
7. JSP에서 태그 라이브러리 선언 후 로드하기
```html
<%@ taglib prefix="wro" uri="https://github.com/lifus/wro4j-runtime-taglib" %>

<wro:script group="vue"/>
<wro:style group="css"/>
```
