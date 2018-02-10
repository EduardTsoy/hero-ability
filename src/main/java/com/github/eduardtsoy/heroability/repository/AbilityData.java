package com.github.eduardtsoy.heroability.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

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

}
