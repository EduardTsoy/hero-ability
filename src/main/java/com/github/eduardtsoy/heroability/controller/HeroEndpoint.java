package com.github.eduardtsoy.heroability.controller;

import com.github.eduardtsoy.heroability.dto.HeroDTO;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Path("/heros")
@Produces({MediaType.APPLICATION_JSON})
@Api(value = "/heros", description = "Welcome to Hero API")
public class HeroEndpoint {

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
        return new HeroDTO(
                data.getId(),
                data.getName(),
                data.getRealName(),
                data.getHealth(),
                data.getArmour(),
                data.getShield()
        );
    }

}
