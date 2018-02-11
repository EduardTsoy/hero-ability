package com.github.eduardtsoy.heroability.incoming.part;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroIn {

    @NotNull
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
