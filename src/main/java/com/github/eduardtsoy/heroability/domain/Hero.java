package com.github.eduardtsoy.heroability.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Hero {

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
