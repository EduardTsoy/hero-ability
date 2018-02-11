package com.github.eduardtsoy.heroability.endpoint;

import com.github.eduardtsoy.heroability.dto.AbilityDTO;
import com.github.eduardtsoy.heroability.dto.HeroDTO;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.impl.LinkImpl;

import javax.annotation.Nonnull;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static com.github.eduardtsoy.heroability.endpoint.AbilityEndpoint.ABILITIES_PATH;
import static com.github.eduardtsoy.heroability.endpoint.HeroEndpoint.HEROES_PATH;
import static com.github.eduardtsoy.heroability.endpoint.HeroEndpoint.HERO_ABILITIES_PATH;

public class LinkGen {

    public static LinkImpl getApiRootLink(final @Nonnull UriInfo uriInfo) {
        final LinkImpl apiRootLink = new LinkImpl();
        apiRootLink.setRel("api-root");
        apiRootLink.setTitle("API root");
        apiRootLink.setHref(uriInfo.getBaseUri().toString());
        return apiRootLink;
    }

    public static LinkImpl getHeroListLink(final @Nonnull UriInfo uriInfo,
                                           final String rel) {
        final LinkImpl selfLink = new LinkImpl();
        selfLink.setRel(rel);
        selfLink.setTitle("Hero list");
        selfLink.setHref(uriInfo.getBaseUriBuilder().path(HEROES_PATH).toString());
        return selfLink;
    }

    public static LinkImpl getHeroLink(final @Nonnull UriInfo uriInfo,
                                       final @Nonnull HeroDTO dto,
                                       final @Nonnull String rel) {
        final LinkImpl selfLink = new LinkImpl();
        selfLink.setRel(rel);
        selfLink.setTitle("Hero '" + dto.getName() + "'");
        selfLink.setHref(getHeroUri(uriInfo, dto).toString());
        return selfLink;
    }

    public static Link getHeroAbilitiesLink(final @Nonnull UriInfo uriInfo,
                                            final @Nonnull HeroDTO hero,
                                            final @Nonnull String rel) {
        final LinkImpl result = new LinkImpl();
        result.setRel(rel);
        result.setTitle("Abilities of hero '" + hero.getName() + "'");
        result.setHref(getHeroUri(uriInfo, hero).path(HERO_ABILITIES_PATH).toString());
        return result;
    }

    public static LinkImpl getAbilityListLink(final @Nonnull UriInfo uriInfo,
                                              final @Nonnull String rel) {
        final LinkImpl listLink = new LinkImpl();
        listLink.setRel(rel);
        listLink.setTitle("Abilities list");
        listLink.setHref(uriInfo.getBaseUriBuilder().path(ABILITIES_PATH).toString());
        return listLink;
    }

    public static LinkImpl getAbilityLink(final @Nonnull UriInfo uriInfo,
                                          final @Nonnull AbilityDTO dto,
                                          final @Nonnull String rel) {
        final LinkImpl result = new LinkImpl();
        result.setRel(rel);
        result.setTitle("'" + dto.getName() + "' ability");
        final UriBuilder path = uriInfo.getBaseUriBuilder().path(ABILITIES_PATH).path(dto.getId().toString());
        result.setHref(path.toString());
        return result;
    }

    /* ------------------
     * PRIVATE METHODS
     */

    private static UriBuilder getHeroUri(final @Nonnull UriInfo uriInfo,
                                         final @Nonnull HeroDTO hero) {
        return uriInfo.getBaseUriBuilder()
                      .path(HEROES_PATH)
                      .path(String.valueOf(hero.getId()));
    }

}
