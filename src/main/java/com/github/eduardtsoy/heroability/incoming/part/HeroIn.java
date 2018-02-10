package com.github.eduardtsoy.heroability.incoming.part;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long id;
    private String name;
    private String realName;
    private Integer health;
    private Integer armour;
    private Integer shield;

}
