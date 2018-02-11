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
@Entity(name = "hero")
public class HeroData {

    @Version
    private Integer version;

    @Id
    @NotNull
    private Long id;
    private String name;
    private String realName;
    private Integer health;
    private Integer armour;
    private Integer shield;

    @ManyToMany(mappedBy = "heroes", fetch = FetchType.EAGER)
    private List<AbilityData> abilities = new ArrayList<>();

}
