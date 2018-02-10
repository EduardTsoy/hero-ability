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

    private final HeroRepository heroRepository;

    @Autowired
    public SyncWithRemoteServer(final HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void syncDatabase() {
        log.trace(" // syncDatabase started...");
        final Client client = ClientBuilder.newClient();
        final WebTarget base = client.target("https://overwatch-api.net/api/v1");
        final WebTarget heroWeb = base.path("/hero");
        final Heroes heroes = heroWeb.request(MediaType.APPLICATION_JSON)
                                     .get(Heroes.class);
        // TODO: User heroRepository.findAll() to optimize performance
        heroes.getData().forEach(hero -> {
            log.info(hero.toString());
            final Hero existing = heroRepository.findOne(hero.getId());
            if (existing != null) {
                existing.setName(hero.getName());
                existing.setRealName(hero.getRealName());
                existing.setHealth(hero.getHealth());
                existing.setArmour(hero.getArmour());
                existing.setShield(hero.getShield());
                heroRepository.save(existing);
            } else {
                heroRepository.save(hero);
            }
        }); // TODO: debug
        heroRepository.flush();
        log.trace(" // ...syncDatabase finished");
    }
}
