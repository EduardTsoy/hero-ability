package com.github.eduardtsoy.heroability.repository;

import com.github.eduardtsoy.heroability.domain.Ability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbilityRepository extends JpaRepository<Ability, Long> {
}
