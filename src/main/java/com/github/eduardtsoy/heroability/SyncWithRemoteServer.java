package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.incoming.AbilitiesIn;
import com.github.eduardtsoy.heroability.incoming.part.AbilityIn;
import com.github.eduardtsoy.heroability.incoming.part.HeroIn;
import com.github.eduardtsoy.heroability.incoming.HeroesIn;
import com.github.eduardtsoy.heroability.repository.AbilityData;
import com.github.eduardtsoy.heroability.repository.AbilityRepository;
import com.github.eduardtsoy.heroability.repository.HeroData;
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
        final Map<Long, HeroData> savedHeroes = heroRepository
                .findAll().stream()
                .collect(Collectors.toMap(HeroData::getId, Function.identity()));
        do {
            final WebTarget target = client.target(pageLink);
            log.debug("// Loading from " + pageLink);
            final Response response = target.request(MediaType.APPLICATION_JSON).get();
            if (Response.Status.Family.REDIRECTION.equals(response.getStatusInfo().getFamily())) {
                pageLink = response.getLocation();
            } else {
                final HeroesIn heroes = response.readEntity(HeroesIn.class);
                heroes.getData().forEach(hero -> {
                    if (savedHeroes.containsKey(hero.getId())) {
                        final HeroData existing = savedHeroes.get(hero.getId());
                        log.debug(existing.toString());
                        existing.setName(hero.getName());
                        existing.setRealName(hero.getRealName());
                        existing.setHealth(hero.getHealth());
                        existing.setArmour(hero.getArmour());
                        existing.setShield(hero.getShield());
                        heroRepository.save(existing);
                    } else {
                        heroRepository.save(convertDtoToData(hero));
                    }
                });
                if (heroes.getNext() == null) {
                    break;
                }
                pageLink = URI.create(heroes.getNext());
            }
        } while (true);
    }

    private HeroData convertDtoToData(final HeroIn dto) {
        final HeroData result = new HeroData();
        result.setId(dto.getId());
        result.setName(dto.getName());
        result.setRealName(dto.getRealName());
        result.setHealth(dto.getHealth());
        result.setArmour(dto.getArmour());
        result.setShield(dto.getShield());
        return result;
    }

    private void syncAbilities() {
        URI pageLink = URI.create(SOURCE_API_BASE_URI + ABILITY_URI_PATH);
        final Map<Long, AbilityData> savedAbilities = abilityRepository
                .findAll().stream()
                .collect(Collectors.toMap(AbilityData::getId, Function.identity()));
        do {
            final WebTarget target = client.target(pageLink);
            log.debug("// Loading from " + pageLink);
            final Response response = target.request(MediaType.APPLICATION_JSON).get();
            if (Response.Status.Family.REDIRECTION.equals(response.getStatusInfo().getFamily())) {
                pageLink = response.getLocation();
            } else {
                final AbilitiesIn abilities = response.readEntity(AbilitiesIn.class);
                abilities.getData().forEach(ability -> {
                    if (savedAbilities.containsKey(ability.getId())) {
                        final AbilityData existing = savedAbilities.get(ability.getId());
                        log.debug(existing.toString());
                        existing.setName(ability.getName());
                        existing.setDescription(ability.getDescription());
                        existing.setUltimate(ability.getUltimate());
                        abilityRepository.save(existing);
                    } else {
                        abilityRepository.save(convertDtoToData(ability));
                    }
                });
                if (abilities.getNext() == null) {
                    break;
                }
                pageLink = URI.create(abilities.getNext());
            }
        } while (true);
    }

    private AbilityData convertDtoToData(final AbilityIn dto) {
        final AbilityData result = new AbilityData();
        result.setId(dto.getId());
        result.setName(dto.getName());
        result.setDescription(dto.getDescription());
        result.setUltimate(dto.getUltimate());
        return result;
    }

}
