package com.github.eduardtsoy.heroability.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Heroes {

    // Hypermedia navigation
    private String next;

    // Data
    private List<Hero> data = new ArrayList<>();
}
