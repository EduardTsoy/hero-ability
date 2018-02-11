package com.github.eduardtsoy.heroability.endpoint;

import com.github.eduardtsoy.heroability.dto.RootDTO;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Component
@Path(ApiRootEndpoint.ROOT)
@Produces({MediaType.APPLICATION_JSON})
@Api(description = "Welcome to API root")
public class ApiRootEndpoint {

    static final String ROOT = "";

    @Context
    private UriInfo uriInfo;

    public ApiRootEndpoint() {
    }

    @GET
    public RootDTO getRoot() {
        final RootDTO result = new RootDTO();
        addHypermedia(result);
        return result;
    }

    private void addHypermedia(final @Nonnull RootDTO dto) {
        dto.safeLinks().add(LinkGen.getApiRootLink(uriInfo));
        dto.safeLinks().add(LinkGen.getHeroListLink(uriInfo, "hero-api"));
        dto.safeLinks().add(LinkGen.getAbilityListLink(uriInfo, "ability-api"));
    }

}
