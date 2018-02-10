package com.github.eduardtsoy.heroability.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

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

}
