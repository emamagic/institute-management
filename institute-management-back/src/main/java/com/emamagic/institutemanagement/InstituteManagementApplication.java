package com.emamagic.institutemanagement;

import com.emamagic.institutemanagement.feature.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AppAuditorAware")
@EnableMethodSecurity
@EnableCaching
public class InstituteManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstituteManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserService userSvc) {
        return (String... _) -> userSvc.updateAdminPassword();
    }
}
