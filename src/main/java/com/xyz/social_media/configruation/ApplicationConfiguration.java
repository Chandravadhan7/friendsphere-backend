package com.xyz.social_media.configruation;

import com.xyz.social_media.filters.MyFilter;
import com.xyz.social_media.repository.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class ApplicationConfiguration {

    private SessionRepo sessionRepo;

    @Autowired
    public ApplicationConfiguration(SessionRepo sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @Bean
    public RestTemplate createBean(){
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
    @Bean
    public FilterRegistrationBean<MyFilter> myFilterRegistrationBean() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFilter(sessionRepo));
        registrationBean.addUrlPatterns("/*"); // Apply filter to specific URL patterns
        registrationBean.setOrder(1); // Set filter order
        return registrationBean;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://social-media0282.s3-website.ap-south-1.amazonaws.com")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
