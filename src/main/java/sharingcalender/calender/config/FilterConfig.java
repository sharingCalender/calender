package sharingcalender.calender.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sharingcalender.calender.filter.JwtAuthenticationFilter;

@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter = new FilterRegistrationBean<>();

        jwtAuthenticationFilter.setFilter(new JwtAuthenticationFilter());
        jwtAuthenticationFilter.addUrlPatterns("/*");
        jwtAuthenticationFilter.setOrder(1);

        return jwtAuthenticationFilter;
    }
}
