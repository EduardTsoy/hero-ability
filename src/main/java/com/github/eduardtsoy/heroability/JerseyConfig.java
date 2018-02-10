package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.controller.HeroEndpoint;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        configureEndpoints();
        configureSwagger();
    }

    private void configureEndpoints() {
        register(WadlResource.class); // WADL support built into JAX-RS

        register(HeroEndpoint.class);
        // TODO: register(AbilityEndpoint.class);
    }

    // FIXME: Swagger seems to be not working yet
    private void configureSwagger() {
        final BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        // todo: retrieve schemes, host and port from Spring Boot configuration
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api/heros");
        beanConfig.setResourcePackage("com.github.eduardtsoy");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);

        register(ApiListingResource.class);
    }

}
