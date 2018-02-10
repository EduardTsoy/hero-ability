package com.github.eduardtsoy.heroability.endpoint;

import com.github.eduardtsoy.heroability.dto.HeroDTO;
import com.github.eduardtsoy.heroability.repository.HeroData;
import com.github.eduardtsoy.heroability.repository.HeroRepository;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.impl.LinkImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Path(HeroEndpoint.HEROS)
@Produces({MediaType.APPLICATION_JSON})
@Api(value = HeroEndpoint.HEROS, description = "Welcome to Heroes API")
public class HeroEndpoint {

    static final String HEROS = "/heros";

    @Context
    private UriInfo uriInfo;

    private final HeroRepository heroRepository;

    @Autowired
    public HeroEndpoint(@Nonnull final HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    @GET
    @ApiOperation("Get a list of heroes")
    public List<HeroDTO> getHeroList() {
        return heroRepository
                .findAll().stream()
                .map(this::convertDataToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Get a hero")
    public Response getHero(@NotNull @PathParam("id") final Long id) {
        final HeroData heroData = heroRepository.findOne(id);
        if (heroData == null) {
            return Response.status(NOT_FOUND).build();
        }
        final HeroDTO result = convertDataToDTO(heroData);
        return Response.ok(result).build();
    }

    /* ------------------
     * PRIVATE METHODS
     */

    private HeroDTO convertDataToDTO(@Nonnull final HeroData data) {
        final HeroDTO result = new HeroDTO(
                data.getId(),
                data.getName(),
                data.getRealName(),
                data.getHealth(),
                data.getArmour(),
                data.getShield()
        );
        addHypermedia(result);
        return result;
    }

    private void addHypermedia(final HeroDTO result) {
        final List<Link> links = new ArrayList<>();

        final LinkImpl selfLink = new LinkImpl();
        selfLink.setRel("self");
        selfLink.setTitle("Hero '" + result.getName() + "'");
        final String absPath = uriInfo.getAbsolutePath().toString();
        selfLink.setHref(absPath);
        links.add(selfLink);

        final LinkImpl abilitiesLink = new LinkImpl();
        abilitiesLink.setRel("abilities");
        abilitiesLink.setTitle("Abilities of hero '" + result.getName() + "'");
        abilitiesLink.setHref(uriInfo.getAbsolutePathBuilder().path("abilities").toString());
        links.add(abilitiesLink);

        final LinkImpl heroesLink = new LinkImpl();
        heroesLink.setRel("list");
        heroesLink.setTitle("Hero list");
        heroesLink.setHref(uriInfo.getBaseUriBuilder().path(HEROS).toString());
        links.add(heroesLink);

        result.setLinks(links);
    }

}
