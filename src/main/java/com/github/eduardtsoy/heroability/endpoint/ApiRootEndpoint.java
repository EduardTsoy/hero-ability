package com.github.eduardtsoy.heroability.endpoint;

import com.github.eduardtsoy.heroability.dto.RootDTO;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.impl.LinkImpl;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Component
@Path(ApiRootEndpoint.ROOT)
@Produces({MediaType.APPLICATION_JSON})
@Api(description = "Welcome to API root")
public class ApiRootEndpoint {

    static final String ROOT = "";
    @Context
    private UriInfo uriInfo;

    @GET
    public RootDTO getRoot() {
        final RootDTO result = new RootDTO();
        addHypermedia(result);
        return result;
    }

    private void addHypermedia(final RootDTO result) {
        final List<Link> links = new ArrayList<>();

        final LinkImpl selfLink = new LinkImpl();
        selfLink.setRel("self");
        selfLink.setTitle("root");
        selfLink.setHref(uriInfo.getBaseUriBuilder().toString());
        links.add(selfLink);

        final LinkImpl heroesApiLink = new LinkImpl();
        heroesApiLink.setRel("hero-api");
        heroesApiLink.setTitle("Hero list");
        heroesApiLink.setHref(uriInfo.getBaseUriBuilder().path(HeroEndpoint.HEROES_PATH).toString());
        links.add(heroesApiLink);

        final LinkImpl abilitiesApiLink = new LinkImpl();
        abilitiesApiLink.setRel("abilities-api");
        abilitiesApiLink.setTitle("Ability list");
        abilitiesApiLink.setHref(uriInfo.getBaseUriBuilder().path("abilities").toString());
        links.add(abilitiesApiLink);

        result.setLinks(links);
    }

}
