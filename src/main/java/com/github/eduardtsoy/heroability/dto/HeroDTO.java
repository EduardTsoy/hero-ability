package com.github.eduardtsoy.heroability.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.component.impl.EntityImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Siren4JEntity
public class HeroDTO extends EntityImpl {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("health")
    private Integer health;

    @JsonProperty("armour")
    private Integer armour;

    @JsonProperty("shield")
    private Integer shield;

}
