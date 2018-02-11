package com.github.eduardtsoy.heroability.service;

import org.springframework.transaction.annotation.Transactional;

public interface SyncService {

    void syncHeroes();

    @Transactional
    void syncAbilities();
}
