package com.github.eduardtsoy.heroability.repository;

import com.github.eduardtsoy.heroability.domain.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {
}
