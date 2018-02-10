package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.endpoint.HeroEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {HeroAbilityApp.class, HeroEndpoint.class})
public class ProjectConfig {
}
