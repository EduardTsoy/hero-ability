package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.controller.HeroEndpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {HeroAbilityApplication.class, HeroEndpoint.class})
public class HeroAbilityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HeroAbilityApplication.class, args);
    }
}
