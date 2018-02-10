package com.github.eduardtsoy.heroability.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbilityDTO {

    private Long id;
    private String name;
    private String description;
    private Boolean ultimate;

}
