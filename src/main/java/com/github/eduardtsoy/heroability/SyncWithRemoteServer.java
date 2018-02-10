package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.domain.Hero;
import com.github.eduardtsoy.heroability.domain.Heroes;
import com.github.eduardtsoy.heroability.repository.HeroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Component
@Slf4j
public class SyncWithRemoteServer {

    private static final String OVERWATCH_API_BASE_URI = "https://overwatch-api.net/api/v1";
    private static final String HERO_URI_PATH = "/hero";
    private static final String ABILITY_URI_PATH = "/ability";
    private final HeroRepository heroRepository;

    private final Client client = ClientBuilder.newClient();

    @Autowired
    public SyncWithRemoteServer(final HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void syncDatabase() {
        log.trace(" // syncDatabase started...");
        String pageLink = OVERWATCH_API_BASE_URI + HERO_URI_PATH;
        do {
            final WebTarget target = client.target(pageLink);
            final Heroes heroes = target
                    .request(MediaType.APPLICATION_JSON)
                    .get(Heroes.class);
            heroes.getData().forEach(hero -> {
                // TODO: For better performance, use heroRepository.findAll() instead of findOne()
                final Hero existing = heroRepository.findOne(hero.getId());
                if (existing != null) {
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
            }); // TODO: test & debug
            pageLink = heroes.getNext();
        } while (pageLink != null);
        heroRepository.flush();
        log.trace(" // ...syncDatabase finished");
    }
}
