package com.github.eduardtsoy.heroability.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/heros")
@Produces({MediaType.APPLICATION_JSON})
@Api(value = "/heros", description = "Welcome to Hero API")
public class HeroEndpoint {

    @GET
    @ApiOperation("Get a list of heroes")
    public Response getHeroList() {
        return Response.ok("OK").build(); //TODO replace this stub to something useful
    }

}
