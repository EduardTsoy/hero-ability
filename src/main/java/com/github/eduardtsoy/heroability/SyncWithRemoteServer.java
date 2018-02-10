package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.domain.Abilities;
import com.github.eduardtsoy.heroability.domain.Ability;
import com.github.eduardtsoy.heroability.domain.Hero;
import com.github.eduardtsoy.heroability.domain.Heroes;
import com.github.eduardtsoy.heroability.repository.AbilityRepository;
import com.github.eduardtsoy.heroability.repository.HeroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SyncWithRemoteServer {

    private static final String SOURCE_API_BASE_URI = "https://overwatch-api.net/api/v1";
    private static final String HERO_URI_PATH = "/hero";
    private static final String ABILITY_URI_PATH = "/ability";
    private final HeroRepository heroRepository;
    private final AbilityRepository abilityRepository;

    private final Client client = ClientBuilder.newClient();

    @Autowired
    public SyncWithRemoteServer(@Nonnull final HeroRepository heroRepository,
                                @Nonnull final AbilityRepository abilityRepository) {
        this.heroRepository = heroRepository;
        this.abilityRepository = abilityRepository;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void syncDatabase() {
        syncHeroes();
        syncAbilities();
        heroRepository.flush();
    }

    private void syncHeroes() {
        URI pageLink = URI.create(SOURCE_API_BASE_URI + HERO_URI_PATH);
        final Map<Long, Hero> savedHeroes = heroRepository
                .findAll().stream()
                .collect(Collectors.toMap(Hero::getId, Function.identity()));
        do {
            final WebTarget target = client.target(pageLink);
            log.debug("// Loading from " + pageLink);
            final Response response = target.request(MediaType.APPLICATION_JSON).get();
            if (Response.Status.Family.REDIRECTION.equals(response.getStatusInfo().getFamily())) {
                pageLink = response.getLocation();
            } else {
                final Heroes heroes = response.readEntity(Heroes.class);
                heroes.getData().forEach(hero -> {
                    if (savedHeroes.containsKey(hero.getId())) {
                        final Hero existing = savedHeroes.get(hero.getId());
                        log.debug(existing.toString());
                        existing.setName(hero.getName());
                        existing.setRealName(hero.getRealName());
                        existing.setHealth(hero.getHealth());
                        existing.setArmour(hero.getArmour());
                        existing.setShield(hero.getShield());
                        heroRepository.save(existing);
                    } else {
                        heroRepository.save(hero);
                    }
                });
                if (heroes.getNext() == null) {
                    break;
                }
                pageLink = URI.create(heroes.getNext());
            }
        } while (true);
    }

    private void syncAbilities() {
        URI pageLink = URI.create(SOURCE_API_BASE_URI + ABILITY_URI_PATH);
        final Map<Long, Ability> savedAbilities = abilityRepository
                .findAll().stream()
                .collect(Collectors.toMap(Ability::getId, Function.identity()));
        do {
            final WebTarget target = client.target(pageLink);
            log.debug("// Loading from " + pageLink);
            final Response response = target.request(MediaType.APPLICATION_JSON).get();
            if (Response.Status.Family.REDIRECTION.equals(response.getStatusInfo().getFamily())) {
                pageLink = response.getLocation();
            } else {
                final Abilities abilities = response.readEntity(Abilities.class);
                abilities.getData().forEach(ability -> {
                    if (savedAbilities.containsKey(ability.getId())) {
                        final Ability existing = savedAbilities.get(ability.getId());
                        log.debug(existing.toString());
                        existing.setName(ability.getName());
                        existing.setDescription(ability.getDescription());
                        existing.setUltimate(ability.getUltimate());
                        abilityRepository.save(existing);
                    } else {
                        abilityRepository.save(ability);
                    }
                });
                if (abilities.getNext() == null) {
                    break;
                }
                pageLink = URI.create(abilities.getNext());
            }
        } while (true);
    }

}
