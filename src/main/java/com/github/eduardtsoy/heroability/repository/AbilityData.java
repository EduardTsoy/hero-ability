package com.github.eduardtsoy.heroability.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ability")
public class AbilityData {

    @Version
    private Integer version;

    @Id
    @NotNull
    private Long id;
    private String name;
    private String description;
    @Column(name = "is_ultimate")
    private Boolean ultimate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hero_ability",
               joinColumns = @JoinColumn(name = "hero_id"),
               inverseJoinColumns = @JoinColumn(name = "ability_id"))
    private List<HeroData> heroes = new ArrayList<>();

}
