package com.github.eduardtsoy.heroability.dto;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.resource.CollectionResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Siren4JEntity
public class AbilitiesDTO extends BaseDTO {

    @Nonnull
    private CollectionResource<AbilityDTO> abilities = new CollectionResource<>();

}
