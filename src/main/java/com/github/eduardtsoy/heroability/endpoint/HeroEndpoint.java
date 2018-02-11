package com.github.eduardtsoy.heroability.endpoint;

import com.github.eduardtsoy.heroability.dto.AbilitiesDTO;
import com.github.eduardtsoy.heroability.dto.AbilityDTO;
import com.github.eduardtsoy.heroability.dto.HeroDTO;
import com.github.eduardtsoy.heroability.dto.HeroesDTO;
import com.github.eduardtsoy.heroability.repository.AbilityData;
import com.github.eduardtsoy.heroability.repository.HeroData;
import com.github.eduardtsoy.heroability.repository.HeroRepository;
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
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Path(HeroEndpoint.HEROES_PATH)
@Produces({MediaType.APPLICATION_JSON})
@Api(value = HeroEndpoint.HEROES_PATH, description = "Welcome to Heroes API")
public class HeroEndpoint {

    static final String HEROES_PATH = "/heros";

    public static final String HERO_ABILITIES_PATH = "abilities";

    @Context
    private UriInfo uriInfo;

    private final HeroRepository heroRepository;

    @Autowired
    public HeroEndpoint(final @Nonnull HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    @GET
    @ApiOperation("Get a list of heroes")
    public HeroesDTO getHeroList() {
        final HeroesDTO result = new HeroesDTO();
        result.getHeroes().addAll(heroRepository
                .findAll().stream()
                .map(this::convertDataToHeroesDTO)
                .peek(dto -> dto.safeLinks().add(LinkGen.getHeroLink(uriInfo, dto, "self")))
                .collect(Collectors.toList()));
        addHypermedia(result);
        return result;
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Get information about a hero")
    public Response getHero(@NotNull @PathParam("id") final Long id) {
        final HeroData heroData = heroRepository.findOne(id);
        if (heroData == null) {
            return Response.status(NOT_FOUND).build();
        }
        final HeroDTO result = convertDataToHeroesDTO(heroData);
        addHypermedia(result);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}/abilities")
    @ApiOperation("Get information about hero's abilities")
    public Response getHeroAbilities(@NotNull @PathParam("id") final Long id) {
        final HeroData heroData = heroRepository.findOne(id);
        if (heroData == null) {
            return Response.status(NOT_FOUND).build();
        }
        final AbilitiesDTO result = new AbilitiesDTO();
        result.getAbilities().addAll(
                heroData.getAbilities().stream()
                        .map(data -> convertDataToAbilityDTO(data))
                        .peek(dto -> addHypermedia(dto))
                        .collect(Collectors.toList()));
        addHypermedia(result, convertDataToHeroesDTO(heroData));
        return Response.ok(result).build();
    }

    /* ------------------
     * PRIVATE METHODS
     */

    private HeroDTO convertDataToHeroesDTO(final @Nonnull HeroData data) {
        return new HeroDTO(
                data.getId(),
                data.getName(),
                data.getRealName(),
                data.getHealth(),
                data.getArmour(),
                data.getShield()
        );
    }

    private AbilityDTO convertDataToAbilityDTO(final AbilityData data) {
        return new AbilityDTO(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getUltimate()
        );
    }

    private void addHypermedia(final @Nonnull HeroDTO dto) {
        dto.safeLinks().add(LinkGen.getHeroLink(uriInfo, dto, "self"));
        dto.safeLinks().add(LinkGen.getHeroListLink(uriInfo, "list"));
        dto.safeLinks().add(LinkGen.getHeroAbilitiesLink(uriInfo, dto, "abilities"));
    }

    private void addHypermedia(final @Nonnull HeroesDTO dto) {
        dto.safeLinks().add(LinkGen.getHeroListLink(uriInfo, "self"));
        dto.safeLinks().add(LinkGen.getApiRootLink(uriInfo));
    }

    private void addHypermedia(final @Nonnull AbilitiesDTO dto,
                               final @Nonnull HeroDTO hero) {
        dto.safeLinks().add(LinkGen.getHeroAbilitiesLink(uriInfo, hero, "self"));
        dto.safeLinks().add(LinkGen.getHeroLink(uriInfo, hero, "hero"));
    }

    private void addHypermedia(final @Nonnull AbilityDTO dto) {
        dto.safeLinks().add(LinkGen.getAbilityLink(uriInfo, dto, "self"));
    }

}
