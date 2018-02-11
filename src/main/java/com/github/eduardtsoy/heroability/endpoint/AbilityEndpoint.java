package com.github.eduardtsoy.heroability.endpoint;

import com.github.eduardtsoy.heroability.dto.AbilitiesDTO;
import com.github.eduardtsoy.heroability.dto.AbilityDTO;
import com.github.eduardtsoy.heroability.repository.AbilityData;
import com.github.eduardtsoy.heroability.repository.AbilityRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Path(AbilityEndpoint.ABILITIES_PATH)
@Produces({MediaType.APPLICATION_JSON})
@Api(value = AbilityEndpoint.ABILITIES_PATH, description = "Welcome to Abilities API")
@Slf4j
public class AbilityEndpoint {

    static final String ABILITIES_PATH = "/abilities";

    @Context
    private UriInfo uriInfo;

    private final AbilityRepository abilityRepository;

    @Autowired
    public AbilityEndpoint(final @Nonnull AbilityRepository abilityRepository) {
        this.abilityRepository = abilityRepository;
    }

    @GET
    @ApiOperation("Get a list of abilities")
    public AbilitiesDTO getAbilityList() {
        final AbilitiesDTO result = new AbilitiesDTO();
        result.getAbilities().addAll(abilityRepository
                .findAll().stream()
                .map(this::convertDataToDTO)
                .peek(dto -> dto.safeLinks().add(LinkGen.getAbilityLink(uriInfo, dto, "self")))
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
            log.warn("// Ability [id = " + id + "] " + NOT_FOUND);
            return Response.status(NOT_FOUND).build();
        }
        final AbilityDTO result = convertDataToDTO(abilityData);
        addHypermedia(result);
        return Response.ok(result).build();
    }

    /* ------------------
     * PRIVATE METHODS
     */

    private AbilityDTO convertDataToDTO(final @Nonnull AbilityData data) {
        return new AbilityDTO(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getUltimate()
        );
    }

    private void addHypermedia(final @Nonnull AbilitiesDTO dto) {
        dto.safeLinks().add(LinkGen.getAbilityListLink(uriInfo, "self"));
        dto.safeLinks().add(LinkGen.getApiRootLink(uriInfo));
    }

    private void addHypermedia(final @Nonnull AbilityDTO dto) {
        dto.safeLinks().add(LinkGen.getAbilityLink(uriInfo, dto, "self"));
        dto.safeLinks().add(LinkGen.getAbilityListLink(uriInfo, "list"));
    }

}
