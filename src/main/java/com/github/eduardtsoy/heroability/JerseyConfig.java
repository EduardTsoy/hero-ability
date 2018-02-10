package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.endpoint.ApiRootEndpoint;
import com.github.eduardtsoy.heroability.endpoint.HeroEndpoint;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath(JerseyConfig.CONTEXT_PATH)
public class JerseyConfig extends ResourceConfig {

    static final String CONTEXT_PATH = "/api";

    public JerseyConfig() {
        configureEndpoints();
        configureSwagger();
    }

    private void configureEndpoints() {
        // WADL support built into JAX-RS generates web API description in XML format
        // See it by sending a GET request to http://localhost:8080/api/application.wadl with http header Accept: applicaton/xml
        register(WadlResource.class);

        register(ApiRootEndpoint.class);
        register(HeroEndpoint.class);
        // TODO: register(AbilityEndpoint.class);
    }

    // Swagger auto-generates API description in JSON or YAML format
    // See http://localhost:8080/api/swagger.json and http://localhost:8080/api/swagger.json
    private void configureSwagger() {
        final BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        // todo: improvement: try to obtain find out scheme(s), host and port from Spring Boot
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath(CONTEXT_PATH);
        beanConfig.setResourcePackage(JerseyConfig.class.getPackage().getName());
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);

        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
    }

}
