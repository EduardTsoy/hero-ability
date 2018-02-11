package com.github.eduardtsoy.heroability;

import com.github.eduardtsoy.heroability.service.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Component
@Slf4j
public class SynchronizationWorker {

    private final SyncService syncService;

    @Autowired
    public SynchronizationWorker(final @Nonnull SyncService syncService) {
        this.syncService = syncService;
    }

    @Scheduled(fixedDelay = 60000) // or cron: @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    synchronized public void syncDatabase() {
        syncService.syncHeroes();
        syncService.syncAbilities();
    }

}
