package com.github.eduardtsoy.heroability.endpoint;

import com.github.eduardtsoy.heroability.dto.AbilitiesDTO;
import com.github.eduardtsoy.heroability.dto.AbilityDTO;
import com.github.eduardtsoy.heroability.repository.AbilityData;
import com.github.eduardtsoy.heroability.repository.AbilityRepository;
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
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Path(AbilityEndpoint.ABILITIES_PATH)
@Produces({MediaType.APPLICATION_JSON})
@Api(value = AbilityEndpoint.ABILITIES_PATH, description = "Welcome to Abilities API")
public class AbilityEndpoint {

    static final String ABILITIES_PATH = "/abilities";

    @Context
    private UriInfo uriInfo;

    private final AbilityRepository abilityRepository;

    @Autowired
    public AbilityEndpoint(@Nonnull final AbilityRepository abilityRepository) {
        this.abilityRepository = abilityRepository;
    }

    @GET
    @ApiOperation("Get a list of abilities")
    public AbilitiesDTO getAbilityList() {
        final AbilitiesDTO result = new AbilitiesDTO();
        result.getAbilities().addAll(abilityRepository
                .findAll().stream()
                .map(this::convertDataToDTO)
                .peek(this::addSelfLink)
                .collect(Collectors.toList()));
        addHypermedia(result);
        return result;
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Get information on an ability")
    public Response getAbility(@NotNull @PathParam("id") final Long id) {
        final AbilityData abilityData = abilityRepository.findOne(id);
        if (abilityData == null) {
            return Response.status(NOT_FOUND).build();
        }
        final AbilityDTO result = convertDataToDTO(abilityData);
        addHypermedia(result);
        return Response.ok(result).build();
    }

    /* ------------------
     * PRIVATE METHODS
     */

    private AbilityDTO convertDataToDTO(@Nonnull final AbilityData data) {
        return new AbilityDTO(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getUltimate()
        );
    }

    private void addSelfLink(@Nonnull final AbilityDTO result) {
        final List<Link> links = new ArrayList<>();

        final LinkImpl selfLink = new LinkImpl();
        selfLink.setRel("self");
        selfLink.setTitle("'" + result.getName() + "' ability");
        selfLink.setHref(getSelfUri(result).toString());
        links.add(selfLink);

        if (result.getLinks() == null) {
            result.setLinks(new ArrayList<>());
        }
        result.getLinks().addAll(links);
    }

    private UriBuilder getSelfUri(final @Nonnull AbilityDTO result) {
        return uriInfo.getBaseUriBuilder().path(ABILITIES_PATH).path(result.getId().toString());
    }

    private void addHypermedia(@Nonnull final AbilityDTO result) {
        addSelfLink(result);

        final List<Link> links = new ArrayList<>();

        final LinkImpl listLink = new LinkImpl();
        listLink.setRel("list");
        listLink.setTitle("Abilities list");
        listLink.setHref(uriInfo.getBaseUriBuilder().path(ABILITIES_PATH).toString());
        links.add(listLink);

        result.getLinks().addAll(links);
    }

    private void addHypermedia(@Nonnull final AbilitiesDTO result) {
        final List<Link> links = new ArrayList<>();

        final LinkImpl selfLink = new LinkImpl();
        selfLink.setRel("self");
        selfLink.setTitle("Abilities list");
        selfLink.setHref(uriInfo.getBaseUriBuilder().path(ABILITIES_PATH).toString());
        links.add(selfLink);

        final LinkImpl apiRootLink = new LinkImpl();
        apiRootLink.setRel("api-root");
        apiRootLink.setTitle("APIs root");
        apiRootLink.setHref(uriInfo.getBaseUri().toString());
        links.add(apiRootLink);

        if (result.getLinks() == null) {
            result.setLinks(new ArrayList<>());
        }
        result.getLinks().addAll(links);
    }

}
