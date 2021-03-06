package com.github.eduardtsoy.heroability.service;

import com.github.eduardtsoy.heroability.incoming.AbilitiesIn;
import com.github.eduardtsoy.heroability.incoming.HeroesIn;
import com.github.eduardtsoy.heroability.incoming.part.AbilityIn;
import com.github.eduardtsoy.heroability.incoming.part.HeroIn;
import com.github.eduardtsoy.heroability.repository.AbilityData;
import com.github.eduardtsoy.heroability.repository.AbilityRepository;
import com.github.eduardtsoy.heroability.repository.HeroData;
import com.github.eduardtsoy.heroability.repository.HeroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import static javax.ws.rs.core.Response.Status.Family.REDIRECTION;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

@Service
@Slf4j
public class SyncServiceImpl implements SyncService {

    private static final String SOURCE_API_BASE_URI = "https://overwatch-api.net/api/v1";
    private static final String HERO_URI_PATH = "/hero";
    private static final String ABILITY_URI_PATH = "/ability";

    private final HeroRepository heroRepository;
    private final AbilityRepository abilityRepository;

    private final Client client = ClientBuilder.newClient();

    @Autowired
    public SyncServiceImpl(final @Nonnull HeroRepository heroRepository,
                           final @Nonnull AbilityRepository abilityRepository) {
        this.heroRepository = heroRepository;
        this.abilityRepository = abilityRepository;
    }

    @Override
    @Transactional
    public void syncHeroes() {
        URI pageLink = URI.create(SOURCE_API_BASE_URI + HERO_URI_PATH);
        Map<Long, HeroData> fromOurDatabase = null;
        Response.Status.Family statusFamily;
        int countTotal = 0;
        int countNew = 0;
        // Loop through pages
        do {
            final WebTarget target = client.target(pageLink);
            log.debug("// Loading " + pageLink);
            final Response response = target.request(MediaType.APPLICATION_JSON).get();
            statusFamily = response.getStatusInfo().getFamily();
            if (SUCCESSFUL.equals(statusFamily)) {
                if (fromOurDatabase == null) {
                    fromOurDatabase = heroRepository
                            .findAll().stream()
                            .collect(Collectors
                                    .toMap(HeroData::getId, Function.identity()));
                }
                final HeroesIn heroes = response.readEntity(HeroesIn.class);
                log.debug("// " + heroes);
                final Map<Long, HeroData> localData = fromOurDatabase;
                countTotal += heroes.getData().size();
                for (HeroIn received : heroes.getData()) {
                    if (localData.containsKey(received.getId())) {
                        final HeroData existing = localData.get(received.getId());
                        existing.setName(received.getName());
                        existing.setRealName(received.getRealName());
                        existing.setHealth(received.getHealth());
                        existing.setArmour(received.getArmour());
                        existing.setShield(received.getShield());
                        heroRepository.save(existing);
                    } else {
                        countNew++;
                        heroRepository.save(convertHeroToJpaEntity(received));
                    }
                }
                if (heroes.getNext() == null) {
                    break;
                }
                pageLink = URI.create(heroes.getNext());
            } else if (REDIRECTION.equals(statusFamily)) {
                pageLink = response.getLocation();
            } else {
                log.error("// Received " + statusFamily + " response from remote web API: " + response);
                break;
            }
        } while (true);
        log.info("// Synced " + countTotal + " heroes from remote web API in total" +
                (countNew > 0 ? ", " + countNew + " of them have been inserted" : ""));
    }

    @Override
    @Transactional
    public void syncAbilities() {
        URI pageLink = URI.create(SOURCE_API_BASE_URI + ABILITY_URI_PATH);
        Map<Long, AbilityData> fromOurDatabase = null;
        Response.Status.Family statusFamily;
        int countTotal = 0;
        int countNew = 0;
        int countTotalAssociations = 0;
        int countNewAssociations = 0;
        // Loop through pages
        do {
            final WebTarget target = client.target(pageLink);
            log.debug("// Loading " + pageLink);
            final Response response = target.request(MediaType.APPLICATION_JSON).get();
            statusFamily = response.getStatusInfo().getFamily();
            if (SUCCESSFUL.equals(statusFamily)) {
                if (fromOurDatabase == null) {
                    fromOurDatabase = abilityRepository
                            .findAll().stream()
                            .collect(Collectors
                                    .toMap(AbilityData::getId, Function.identity()));
                }
                final AbilitiesIn abilities = response.readEntity(AbilitiesIn.class);
                final Map<Long, AbilityData> localData = fromOurDatabase;
                countTotal += abilities.getData().size();
                for (AbilityIn received : abilities.getData()) {
                    if (received.getHero() != null) {
                        countTotalAssociations++;
                    }
                    if (localData.containsKey(received.getId())) {
                        final AbilityData existing = localData.get(received.getId());
                        existing.setName(received.getName());
                        existing.setDescription(received.getDescription());
                        existing.setUltimate(received.getUltimate());
                        abilityRepository.saveAndFlush(existing);
                    } else {
                        countNew++;
                        final AbilityData newAbility = abilityRepository
                                .saveAndFlush(convertAbilityToJpaEntity(received));
                        if (received.getHero() != null) {
                            countNewAssociations++;
                            final HeroData hero = heroRepository.findOne(received.getHero().getId());
                            if (hero != null) {
                                newAbility.getHeroes().add(hero);
                                // log.debug("// " + newAbility);
                                abilityRepository.saveAndFlush(newAbility);
                            } else {
                                log.error("// Cannot find Hero [id = " + received.getHero()
                                                                                 .getId() + "] in our local database");
                            }
                        }

                    }
                }
                if (abilities.getNext() == null) {
                    break;
                }
                pageLink = URI.create(abilities.getNext());
            } else if (REDIRECTION.equals(statusFamily)) {
                pageLink = response.getLocation();
            } else {
                log.error("// Received " + statusFamily + " response from remote web API: " + response);
                break;
            }
        } while (true);
        log.info("// Synced " + countTotal + " abilities from remote web API in total" +
                (countNew > 0 ? ", " + countNew + " of them have been inserted" : ""));
        log.info("// Synced " + countTotalAssociations + " associations from remote web API in total" +
                (countNewAssociations > 0 ? ", " + countNewAssociations + " of them have been inserted" : ""));
    }

    /* ------------------
     * PRIVATE METHODS
     */

    private HeroData convertHeroToJpaEntity(final HeroIn received) {
        final HeroData result = new HeroData();
        result.setId(received.getId());
        result.setName(received.getName());
        result.setRealName(received.getRealName());
        result.setHealth(received.getHealth());
        result.setArmour(received.getArmour());
        result.setShield(received.getShield());
        return result;
    }

    private AbilityData convertAbilityToJpaEntity(final @Nonnull AbilityIn received) {
        final AbilityData result = new AbilityData();
        result.setId(received.getId());
        result.setName(received.getName());
        result.setDescription(received.getDescription());
        result.setUltimate(received.getUltimate());
        return result;
    }
}
