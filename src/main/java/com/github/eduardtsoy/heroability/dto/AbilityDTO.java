package com.github.eduardtsoy.heroability.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.component.impl.EntityImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Siren4JEntity
public class AbilityDTO extends BaseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_ultimate")
    private Boolean ultimate;

}
