package com.github.eduardtsoy.heroability.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroDTO {

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
