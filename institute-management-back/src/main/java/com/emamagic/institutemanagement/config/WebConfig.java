package com.emamagic.institutemanagement.config;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.emamagic.institutemanagement.config.SecurityConfig.PUBLIC_ENDPOINTS;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final ProfileCompletionInterceptor profileCompletionInterceptor;

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(profileCompletionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(PUBLIC_ENDPOINTS)
                .excludePathPatterns("/users/profile/**" ,"/static/**");
    }
}
